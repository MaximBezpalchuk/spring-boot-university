package com.foxminded.university.model;

public class Audience {

	private int room;
	private int capacity;

	public Audience(int room, int capacity) {
		this.room = room;
		this.capacity = capacity;
	}

	public int getRoom() {
		return room;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capacity;
		result = prime * result + room;
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
		Audience other = (Audience) obj;
		if (capacity != other.capacity)
			return false;
		if (room != other.room)
			return false;
		return true;
	}

	
}
