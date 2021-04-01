package com.foxminded.university.model;

import java.time.LocalDate;

public class Holiday {

	private String description;
	private LocalDate date;

	public Holiday(String description, LocalDate date) {
		this.description = description;
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getDate() {
		return date;
	}

}
