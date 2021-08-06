//TODO: delete subjects list, asking for subjects from subjects_table where cathedra_id=?
//TODO: delete groups list, asking for groups from groups_table where cathedra_id=?
//TODO: delete teachers list, asking for teachers from teachers_table where cathedra_id=?
//TODO: delete lectures list, asking for lectures from lectures_table where cathedra_id=?
//TODO: delete holiday list, asking for holidays from holidays_table where cathedra_id=?
//TODO: delete audience list, asking for audiences from audiences_table where cathedra_id=?
//TODO: delete lecture_times list, no need here

package com.foxminded.university.model;

import java.util.Objects;

public class Cathedra {
	private int id;

	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Cathedra(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
