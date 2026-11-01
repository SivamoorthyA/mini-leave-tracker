package com.aicera.leavetracker.leave;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "employee_id", nullable = false)
	private UUID employeeId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LeaveType type;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	private String reason;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LeaveStatus status;

	protected LeaveRequest() {
		// JPA needs an empty constructor
	}

	public LeaveRequest(UUID employeeId, LeaveType type, LocalDate startDate, LocalDate endDate, String reason) {
		this.employeeId = employeeId;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.status = LeaveStatus.PENDING;
	}

	public void changeStatus(LeaveStatus newStatus) {
		this.status = newStatus;
	}

	public UUID getId() {
		return id;
	}

	public UUID getEmployeeId() {
		return employeeId;
	}

	public LeaveType getType() {
		return type;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getReason() {
		return reason;
	}

	public LeaveStatus getStatus() {
		return status;
	}
}
