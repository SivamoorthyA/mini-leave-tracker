package com.aicera.leavetracker.employee;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<EmployeeResponse> findAll() {
		return employeeRepository.findAll().stream()
				.map(EmployeeResponse::from)
				.toList();
	}
}
