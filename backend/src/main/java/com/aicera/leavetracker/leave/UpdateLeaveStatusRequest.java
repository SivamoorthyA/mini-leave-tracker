package com.aicera.leavetracker.leave;

import jakarta.validation.constraints.NotNull;

public record UpdateLeaveStatusRequest(@NotNull LeaveStatus status) {
}
