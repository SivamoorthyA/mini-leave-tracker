package com.aicera.leavetracker.leave;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aicera.leavetracker.common.BadRequestException;
import com.aicera.leavetracker.common.NotFoundException;
import com.aicera.leavetracker.employee.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

	@Mock
	private LeaveRequestRepository leaveRequestRepository;

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private LeaveService leaveService;

	private final UUID employeeId = UUID.randomUUID();

	@Test
	void createSavesLeaveAsPending() {
		var request = new CreateLeaveRequest(employeeId, LeaveType.ANNUAL,
				LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 4), "family function");
		when(employeeRepository.existsById(employeeId)).thenReturn(true);
		when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(call -> call.getArgument(0));

		LeaveResponse response = leaveService.create(request);

		assertThat(response.status()).isEqualTo(LeaveStatus.PENDING);
		assertThat(response.employeeId()).isEqualTo(employeeId);
		assertThat(response.type()).isEqualTo(LeaveType.ANNUAL);
	}

	@Test
	void createAllowsOneDayLeave() {
		var oneDay = LocalDate.of(2026, 11, 2);
		var request = new CreateLeaveRequest(employeeId, LeaveType.SICK, oneDay, oneDay, null);
		when(employeeRepository.existsById(employeeId)).thenReturn(true);
		when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(call -> call.getArgument(0));

		LeaveResponse response = leaveService.create(request);

		assertThat(response.startDate()).isEqualTo(response.endDate());
	}

	@Test
	void createRejectsEndDateBeforeStartDate() {
		var request = new CreateLeaveRequest(employeeId, LeaveType.SICK,
				LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 2), null);

		assertThatThrownBy(() -> leaveService.create(request))
				.isInstanceOf(BadRequestException.class)
				.hasMessage("endDate must not be before startDate");
		verify(leaveRequestRepository, never()).save(any());
	}

	@Test
	void createRejectsUnknownEmployee() {
		var request = new CreateLeaveRequest(employeeId, LeaveType.CASUAL,
				LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 3), null);
		when(employeeRepository.existsById(employeeId)).thenReturn(false);

		assertThatThrownBy(() -> leaveService.create(request))
				.isInstanceOf(NotFoundException.class);
		verify(leaveRequestRepository, never()).save(any());
	}

	@Test
	void findAllReturnsEveryLeave() {
		var leave = new LeaveRequest(employeeId, LeaveType.ANNUAL,
				LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 4), "trip");
		when(leaveRequestRepository.findAll()).thenReturn(List.of(leave));

		List<LeaveResponse> result = leaveService.findAll();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).reason()).isEqualTo("trip");
	}
}
