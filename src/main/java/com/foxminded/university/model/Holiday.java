package com.foxminded.university.model;

import java.time.LocalDate;

public class Holiday {

	private String description;
	private LocalDate date;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
