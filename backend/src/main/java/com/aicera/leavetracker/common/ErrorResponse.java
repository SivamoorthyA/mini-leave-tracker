package com.aicera.leavetracker.common;

public record ErrorResponse(int status, String error, String message) {
}
