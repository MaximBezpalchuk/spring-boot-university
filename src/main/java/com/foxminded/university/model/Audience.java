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

}
