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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;
		private LocalDate start;
		private LocalDate end;
		private Teacher teacher;

		// .start().end().teacher()

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder start(LocalDate start) {
			this.start = start;
			return this;
		}

		public Builder end(LocalDate end) {
			this.end = end;
			return this;
		}

		public Builder teacher(Teacher teacher) {
			this.teacher = teacher;
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
