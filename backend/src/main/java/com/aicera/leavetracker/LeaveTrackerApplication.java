package com.aicera.leavetracker;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeaveTrackerApplication {

	public static void main(String[] args) {
		// Windows reports India as "Asia/Calcutta", which PostgreSQL 16 rejects. Use UTC.
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(LeaveTrackerApplication.class, args);
	}

}
