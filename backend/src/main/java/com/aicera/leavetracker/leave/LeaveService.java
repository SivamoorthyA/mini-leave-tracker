package com.aicera.leavetracker.leave;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aicera.leavetracker.common.BadRequestException;
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

	public List<LeaveResponse> findAll() {
		return leaveRequestRepository.findAll().stream()
				.map(LeaveResponse::from)
				.toList();
	}
}
