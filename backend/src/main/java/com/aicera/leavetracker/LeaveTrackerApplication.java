package com.aicera.leavetracker;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeaveTrackerApplication {

	static {
		// Windows reports India as "Asia/Calcutta", which PostgreSQL 16 rejects. Use UTC.
		// A static block runs whenever the class loads, so tests get it too (they skip main).
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(LeaveTrackerApplication.class, args);
	}

}
