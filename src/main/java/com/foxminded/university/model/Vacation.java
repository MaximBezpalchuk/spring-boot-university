package com.foxminded.university.model;

import java.time.LocalDate;

public class Vacation {

	private String description;
	private LocalDate start;
	private LocalDate finish;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getFinish() {
		return finish;
	}

	public void setFinish(LocalDate finish) {
		this.finish = finish;
	}

}
