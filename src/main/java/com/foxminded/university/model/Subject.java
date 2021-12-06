package com.foxminded.university.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	private Cathedra cathedra;
	@Column
	private String name;
	@Column
	private String description;

	private Subject(Builder builder) {
		this.id = builder.id;
		this.cathedra = builder.cathedra;
		this.name = builder.name;
		this.description = builder.description;
	}

	public Subject() {
	}

	public static Builder builder() {
		return new Builder();
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

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public static class Builder {

		private int id;
		private String name;
		private String description;
		private Cathedra cathedra;

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder cathedra(Cathedra cathedra) {
			this.cathedra = cathedra;
			return this;
		}

		public Subject build() {
			return new Subject(this);
		}
	}

}
