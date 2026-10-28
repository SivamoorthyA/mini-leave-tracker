package com.aicera.leavetracker.employee;

import java.util.UUID;

public record EmployeeResponse(UUID id, String name, String email) {

	static EmployeeResponse from(Employee employee) {
		return new EmployeeResponse(employee.getId(), employee.getName(), employee.getEmail());
	}
}
