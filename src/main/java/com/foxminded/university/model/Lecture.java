package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecture {

	private int id;
	private Cathedra cathedra;
	private List<Group> groups = new ArrayList<>();
	private Teacher teacher;
	private Audience audience;
	private LocalDate date;
	private Subject subject;
	private LectureTime time;

	private Lecture(Builder builder) {
		this.id = builder.id;
		this.cathedra = builder.cathedra;
		this.teacher = builder.teacher;
		this.audience = builder.audience;
		this.date = builder.date;
		this.subject = builder.subject;
		this.time = builder.time;
		this.groups = builder.groups;
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

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

	public Audience getAudience() {
		return audience;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTime(LectureTime time) {
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public Subject getSubject() {
		return subject;
	}

	public LectureTime getTime() {
		return time;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;
		private Cathedra cathedra;
		private List<Group> groups = new ArrayList<>();
		private Teacher teacher;
		private Audience audience;
		private LocalDate date;
		private Subject subject;
		private LectureTime time;

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder cathedra(Cathedra cathedra) {
			this.cathedra = cathedra;
			return this;
		}

		public Builder subject(Subject subject) {
			this.subject = subject;
			return this;
		}

		public Builder date(LocalDate date) {
			this.date = date;
			return this;
		}

		public Builder time(LectureTime time) {
			this.time = time;
			return this;
		}

		public Builder audience(Audience audience) {
			this.audience = audience;
			return this;
		}

		public Builder teacher(Teacher teacher) {
			this.teacher = teacher;
			return this;
		}

		public Builder group(List<Group> groups) {
			this.groups = groups;
			return this;
		}

		public Lecture build() {
			return new Lecture(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(audience, cathedra, date, groups, id, subject, teacher, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lecture other = (Lecture) obj;
		return Objects.equals(audience, other.audience) && Objects.equals(cathedra, other.cathedra)
				&& Objects.equals(date, other.date) && Objects.equals(groups, other.groups) && id == other.id
				&& Objects.equals(subject, other.subject) && Objects.equals(teacher, other.teacher)
				&& Objects.equals(time, other.time);
	}

}
