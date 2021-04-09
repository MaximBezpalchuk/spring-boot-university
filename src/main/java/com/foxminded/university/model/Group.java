package com.foxminded.university.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private Cathedra cathedra;
	private List<Student> students = new ArrayList<>();

	public Group(String name, Cathedra cathedra) {
		this.name = name;
		this.cathedra = cathedra;
	}

	public String getName() {
		return name;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Cathedra getCathedra() {
		return cathedra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cathedra == null) ? 0 : cathedra.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((students == null) ? 0 : students.hashCode());
		return result;
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
		if (cathedra == null) {
			if (other.cathedra != null)
				return false;
		} else if (!cathedra.equals(other.cathedra))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (students == null) {
			if (other.students != null)
				return false;
		} else if (!students.equals(other.students))
			return false;
		return true;
	}

}
