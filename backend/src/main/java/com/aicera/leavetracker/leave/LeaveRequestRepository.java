package com.aicera.leavetracker.leave;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {

}
