package com.aicera.leavetracker.leave;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aicera.leavetracker.common.NotFoundException;

@WebMvcTest(LeaveController.class)
class LeaveControllerTest {

	private static final UUID EMPLOYEE_ID = UUID.fromString("1906bb71-e713-452b-b30b-67b7dde3bb59");

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LeaveService leaveService;

	private String body(String type, String start, String end) {
		return """
				{"employeeId":"%s","type":"%s","startDate":"%s","endDate":"%s","reason":"family function"}
				""".formatted(EMPLOYEE_ID, type, start, end);
	}

	@Test
	void postReturns201WithPendingLeave() throws Exception {
		var created = new LeaveResponse(UUID.randomUUID(), EMPLOYEE_ID, LeaveType.ANNUAL,
				LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 4), "family function", LeaveStatus.PENDING);
		when(leaveService.create(any(CreateLeaveRequest.class))).thenReturn(created);

		mockMvc.perform(post("/api/leaves").contentType(MediaType.APPLICATION_JSON)
				.content(body("ANNUAL", "2026-11-02", "2026-11-04")))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status").value("PENDING"))
				.andExpect(jsonPath("$.type").value("ANNUAL"))
				.andExpect(jsonPath("$.startDate").value("2026-11-02"));
	}

	@Test
	void postWithMissingFieldsReturns400() throws Exception {
		mockMvc.perform(post("/api/leaves").contentType(MediaType.APPLICATION_JSON)
				.content("{\"employeeId\":\"" + EMPLOYEE_ID + "\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.error").value("Bad Request"));
	}

	@Test
	void postWithUnknownTypeReturns400() throws Exception {
		mockMvc.perform(post("/api/leaves").contentType(MediaType.APPLICATION_JSON)
				.content(body("HOLIDAY", "2026-11-02", "2026-11-03")))
				.andExpect(status().isBadRequest());
	}

	@Test
	void postWithTooLongReasonReturns400() throws Exception {
		String longReason = "x".repeat(201);
		String json = """
				{"employeeId":"%s","type":"SICK","startDate":"2026-11-02","endDate":"2026-11-03","reason":"%s"}
				""".formatted(EMPLOYEE_ID, longReason);

		mockMvc.perform(post("/api/leaves").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	void postForUnknownEmployeeReturns404() throws Exception {
		when(leaveService.create(any(CreateLeaveRequest.class)))
				.thenThrow(new NotFoundException("Employee " + EMPLOYEE_ID + " not found"));

		mockMvc.perform(post("/api/leaves").contentType(MediaType.APPLICATION_JSON)
				.content(body("SICK", "2026-11-02", "2026-11-03")))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void getReturns200WithList() throws Exception {
		var leave = new LeaveResponse(UUID.randomUUID(), EMPLOYEE_ID, LeaveType.CASUAL,
				LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 2), null, LeaveStatus.PENDING);
		when(leaveService.findAll()).thenReturn(List.of(leave));

		mockMvc.perform(get("/api/leaves"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].type").value("CASUAL"))
				.andExpect(jsonPath("$.length()").value(1));
	}
}
