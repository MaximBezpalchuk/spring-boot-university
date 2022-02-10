package com.foxminded.university.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "audiences")
public class Audience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Range(min = 1, message = "{Room.audience.range}")
    private int room;

    @Column
    @Range(min = 1, message = "{Capacity.audience.range}")
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "{Cathedra.notNull}")
    private Cathedra cathedra;

    private Audience(int id, int room, int capacity, Cathedra cathedra) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.cathedra = cathedra;
    }

    public Audience() {
    }

    public static Builder builder() {
        return new Builder();
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

    public void setRoom(int room) {
        this.room = room;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

}
