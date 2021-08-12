package com.foxminded.university.model;

import java.util.Objects;

public class Cathedra {

	private int id;
	private String name;

	private Cathedra(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
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

	public void setName(String name) {
		this.name = name;
	}

	public static Builder build(String name) {
		return new Builder(name);
	}

	public static class Builder {
		private int id;
		private final String name; // required field

		public Builder(String name) {
			this.name = name;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Cathedra build() {
			return new Cathedra(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cathedra other = (Cathedra) obj;
		return id == other.id && Objects.equals(name, other.name);
	}

}
