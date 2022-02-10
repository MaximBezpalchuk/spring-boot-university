package com.foxminded.university.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class AudienceDto {

    private Integer id;
    @Range(min = 1, message = "{Room.audience.range}")
    private Integer room;
    @Range(min = 1, message = "{Capacity.audience.range}")
    private Integer capacity;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;

    public AudienceDto(Integer id, Integer room, Integer capacity, CathedraDto cathedra) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.cathedra = cathedra;
    }

    public AudienceDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public CathedraDto getCathedra() {
        return cathedra;
    }

    public void setCathedra(CathedraDto cathedra) {
        this.cathedra = cathedra;
    }
}
