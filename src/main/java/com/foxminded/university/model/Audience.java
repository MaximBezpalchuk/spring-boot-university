package com.foxminded.university.model;

import java.util.Objects;

public class Audience {

	private int id;
	private int room;
	private int capacity;

	public Audience(int room, int capacity) {
		this.room = room;
		this.capacity = capacity;
	}
	
	public Audience() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoom() {
		return room;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, id, room);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Audience other = (Audience) obj;
		return capacity == other.capacity && id == other.id && room == other.room;
	}

}
