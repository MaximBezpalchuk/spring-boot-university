package com.foxminded.university.model;

import java.util.Objects;

public class Subject {

	private int id;
	private Cathedra cathedra;
	private String name;
	private String description;

	private Subject(Builder builder) {
		this.id = builder.id;
		this.cathedra = builder.cathedra;
		this.name = builder.name;
		this.description = builder.description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
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

	public static class Builder {

		private int id;
		private final String name; // required field
		private final String description; // required field
		private final Cathedra cathedra; // required field

		public Builder(Cathedra cathedra, String name, String description) {
			this.cathedra = cathedra;
			this.name = name;
			this.description = description;
		}

		public Builder setId(int id) {
			this.id = id;
			return this;
		}

		public Subject build() {
			return new Subject(this);
		}
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
