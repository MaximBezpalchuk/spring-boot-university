package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Holiday {
	private int id;

	private String name;
	private LocalDate date;

	public Holiday(String name, LocalDate date) {
		this.name = name;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getDate() {
		return date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Holiday other = (Holiday) obj;
		return Objects.equals(date, other.date) && id == other.id && Objects.equals(name, other.name);
	}

}
