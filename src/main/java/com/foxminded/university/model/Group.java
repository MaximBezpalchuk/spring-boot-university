package com.foxminded.university.model;

import java.util.Objects;

public class Group {

	private int id;
	private String name;
	private Cathedra cathedra;

	private Group(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.cathedra = builder.cathedra;
	}

	public void setName(String name) {
		this.name = name;
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

	public Cathedra getCathedra() {
		return cathedra;
	}

	public static Builder build(String name, Cathedra cathedra) {
		return new Builder(name, cathedra);
	}

	public static class Builder {

		private int id;
		private final String name; // required field
		private final Cathedra cathedra; // required field

		public Builder(String name, Cathedra cathedra) {
			this.name = name;
			this.cathedra = cathedra;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Group build() {
			return new Group(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(cathedra, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		return Objects.equals(cathedra, other.cathedra) && id == other.id && Objects.equals(name, other.name);
	}

}
