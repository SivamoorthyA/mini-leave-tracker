package com.aicera.leavetracker.leave;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

	private final LeaveService leaveService;

	public LeaveController(LeaveService leaveService) {
		this.leaveService = leaveService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LeaveResponse create(@Valid @RequestBody CreateLeaveRequest request) {
		return leaveService.create(request);
	}

	@GetMapping
	public List<LeaveResponse> findAll() {
		return leaveService.findAll();
	}
}
