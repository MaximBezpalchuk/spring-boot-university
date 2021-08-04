package com.foxminded.university.model;

import java.time.LocalTime;
import java.util.Objects;

public class LectureTime {

	private int id;
	private LocalTime start;
	private LocalTime end;

	public LectureTime(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		return Objects.hash(end, id, start);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LectureTime other = (LectureTime) obj;
		return Objects.equals(end, other.end) && id == other.id && Objects.equals(start, other.start);
	}

}
