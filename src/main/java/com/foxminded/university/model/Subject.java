package com.foxminded.university.model;

import java.util.Objects;

public class Subject {

	private int id;
	private Cathedra cathedra;
	private String name;
	private String description;

	public Subject(Cathedra cathedra, String name, String description) {
		this.cathedra = cathedra;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cathedra getCathedra() {
		return cathedra;
	}

	public void setCathedra(Cathedra cathedra) {
		this.cathedra = cathedra;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cathedra, description, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject other = (Subject) obj;
		return Objects.equals(cathedra, other.cathedra) && Objects.equals(description, other.description)
				&& id == other.id && Objects.equals(name, other.name);
	}

}
