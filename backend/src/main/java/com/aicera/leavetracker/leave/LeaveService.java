package com.aicera.leavetracker.leave;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aicera.leavetracker.common.BadRequestException;
import com.aicera.leavetracker.common.ConflictException;
import com.aicera.leavetracker.common.NotFoundException;
import com.aicera.leavetracker.employee.EmployeeRepository;

@Service
public class LeaveService {

	private final LeaveRequestRepository leaveRequestRepository;
	private final EmployeeRepository employeeRepository;

	public LeaveService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
		this.employeeRepository = employeeRepository;
	}

	public LeaveResponse create(CreateLeaveRequest request) {
		if (request.endDate().isBefore(request.startDate())) {
			throw new BadRequestException("endDate must not be before startDate");
		}
		if (!employeeRepository.existsById(request.employeeId())) {
			throw new NotFoundException("Employee " + request.employeeId() + " not found");
		}
		LeaveRequest leave = new LeaveRequest(request.employeeId(), request.type(),
				request.startDate(), request.endDate(), request.reason());
		return LeaveResponse.from(leaveRequestRepository.save(leave));
	}

	@Transactional
	public LeaveResponse updateStatus(UUID id, LeaveStatus newStatus) {
		if (newStatus == LeaveStatus.PENDING) {
			throw new BadRequestException("status must be APPROVED or REJECTED");
		}
		LeaveRequest leave = leaveRequestRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Leave request " + id + " not found"));
		if (leave.getStatus() != LeaveStatus.PENDING) {
			throw new ConflictException("Leave request is already " + leave.getStatus());
		}
		leave.changeStatus(newStatus);
		return LeaveResponse.from(leaveRequestRepository.save(leave));
	}

	public List<LeaveResponse> findAll() {
		return leaveRequestRepository.findAll().stream()
				.map(LeaveResponse::from)
				.toList();
	}
}
