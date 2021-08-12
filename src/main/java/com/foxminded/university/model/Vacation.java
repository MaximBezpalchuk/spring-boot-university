package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Vacation {

	private int id;
	private LocalDate start;
	private LocalDate end;
	private Teacher teacher;

	private Vacation(Builder builder) {
		this.id = builder.id;
		this.start = builder.start;
		this.end = builder.end;
		this.teacher = builder.teacher;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public LocalDate getStart() {
		return start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public static Builder build(LocalDate start, LocalDate end, Teacher teacher) {
		return new Builder(start, end, teacher);
	}

	public static class Builder {

		private int id;
		private final LocalDate start; // required field
		private final LocalDate end; // required field
		private final Teacher teacher; // required field

		public Builder(LocalDate start, LocalDate end, Teacher teacher) {
			this.start = start;
			this.end = end;
			this.teacher = teacher;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Vacation build() {
			return new Vacation(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(end, id, start, teacher);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vacation other = (Vacation) obj;
		return Objects.equals(end, other.end) && id == other.id && Objects.equals(start, other.start)
				&& Objects.equals(teacher, other.teacher);
	}

}
