package com.aicera.leavetracker.leave;

import java.time.LocalDate;
import java.util.UUID;

public record LeaveResponse(
		UUID id,
		UUID employeeId,
		LeaveType type,
		LocalDate startDate,
		LocalDate endDate,
		String reason,
		LeaveStatus status) {

	static LeaveResponse from(LeaveRequest leave) {
		return new LeaveResponse(leave.getId(), leave.getEmployeeId(), leave.getType(),
				leave.getStartDate(), leave.getEndDate(), leave.getReason(), leave.getStatus());
	}
}
