package com.aicera.leavetracker.leave;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLeaveRequest(
		@NotNull UUID employeeId,
		@NotNull LeaveType type,
		@NotNull LocalDate startDate,
		@NotNull LocalDate endDate,
		@Size(max = 200) String reason) {
}
