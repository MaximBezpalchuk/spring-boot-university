package com.foxminded.university.model;

import java.util.Objects;

public class Cathedra {

	private int id;
	private String name;

	private Cathedra(int id, String name) {
		this.id = id;
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

	public void setName(String name) {
		this.name = name;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private int id;
		private String name;

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Cathedra build() {
			return new Cathedra(id, name);
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
