package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.Objects;

public class Vacation {

	private int id;
	private LocalDate start;
	private LocalDate end;
	private Teacher teacher;

	public Vacation(LocalDate start, LocalDate end, Teacher teacher) {
		this.start = start;
		this.end = end;
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
