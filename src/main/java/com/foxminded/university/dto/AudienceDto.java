package com.foxminded.university.dto;

public class AudienceDto {

    private Integer id;
    private Integer room;
    private Integer capacity;
    private CathedraDto cathedraDto;

    public AudienceDto(Integer id, Integer room, Integer capacity, CathedraDto cathedraDto) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.cathedraDto = cathedraDto;
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

    public CathedraDto getCathedraDto() {
        return cathedraDto;
    }

    public void setCathedraDto(CathedraDto cathedraDto) {
        this.cathedraDto = cathedraDto;
    }
}
