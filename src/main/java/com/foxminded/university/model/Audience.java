package com.foxminded.university.model;

import java.util.Objects;

public class Audience {

	private int id;
	private int room;
	private int capacity;
	private Cathedra cathedra;

	private Audience(int id, int room, int capacity, Cathedra cathedra) {
		this.id = id;
		this.room = room;
		this.capacity = capacity;
		this.cathedra = cathedra;
	}
	
	public Audience() {}

	public void setRoom(int room) {
		this.room = room;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Cathedra getCathedra() {
		return cathedra;
	}

	public void setCathedra(Cathedra cathedra) {
		this.cathedra = cathedra;
	}

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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private int id;
		private int room;
		private int capacity;
		private Cathedra cathedra;

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder room(int room) {
			this.room = room;
			return this;
		}

		public Builder capacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public Builder cathedra(Cathedra cathedra) {
			this.cathedra = cathedra;
			return this;
		}

		public Audience build() {
			return new Audience(id, room, capacity, cathedra);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, cathedra, id, room);
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
		return capacity == other.capacity && Objects.equals(cathedra, other.cathedra) && id == other.id
				&& room == other.room;
	}

}
