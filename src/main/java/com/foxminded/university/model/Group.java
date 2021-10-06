package com.foxminded.university.model;

import java.util.Objects;

public class Group {

	private int id;
	private String name;
	private Cathedra cathedra;

	private Group(int id, String name, Cathedra cathedra) {
		this.id = id;
		this.name = name;
		this.cathedra = cathedra;
	}
	
	public Group() {}

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
	
	public void setCathedra(Cathedra cathedra) {
		this.cathedra = cathedra;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;
		private String name;
		private Cathedra cathedra;

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder cathedra(Cathedra cathedra) {
			this.cathedra = cathedra;
			return this;
		}

		public Group build() {
			return new Group(id, name, cathedra);
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
