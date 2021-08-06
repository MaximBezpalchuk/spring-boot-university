package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Holiday {
	private int id;

	private String name;
	private LocalDate date;
	private Cathedra cathedra;

	public Holiday(String name, LocalDate date, Cathedra cathedra) {
		this.name = name;
		this.date = date;
		this.cathedra = cathedra;
	}

	public Cathedra getCathedra() {
		return cathedra;
	}

	public void setCathedra(Cathedra cathedra) {
		this.cathedra = cathedra;
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
		return Objects.hash(cathedra, date, id, name);
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
		return Objects.equals(cathedra, other.cathedra) && Objects.equals(date, other.date) && id == other.id
				&& Objects.equals(name, other.name);
	}

}
