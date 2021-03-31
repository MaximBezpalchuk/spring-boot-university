package com.foxminded.university.model;

import java.time.LocalDate;

public class Vacation {

	private String description;
	private LocalDate start;
	private LocalDate end;

	public Vacation(String description, LocalDate start, LocalDate end) {
		this.description = description;
		this.start = start;
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}

}
