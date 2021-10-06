package com.foxminded.university.model;

import java.time.LocalTime;
import java.util.Objects;

public class LectureTime {

	private int id;
	private LocalTime start;
	private LocalTime end;

	private LectureTime(int id, LocalTime start, LocalTime end) {
		this.id = id;
		this.start = start;
		this.end = end;
	}
	
	public LectureTime() {}

	public void setStart(LocalTime start) {
		this.start = start;
	}

	public void setEnd(LocalTime end) {
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;
		private LocalTime start;
		private LocalTime end;

		public Builder start(LocalTime start) {
			this.start = start;
			return this;
		}

		public Builder end(LocalTime end) {
			this.end = end;
			return this;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public LectureTime build() {
			return new LectureTime(id, start, end);
		}
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
