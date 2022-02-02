package com.foxminded.university.dto;

public class AudienceDto {

    private Integer id;
    private Integer room;
    private Integer capacity;
    private String cathedraName;

    public AudienceDto(Integer id, Integer room, Integer capacity, String cathedraName) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.cathedraName = cathedraName;
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

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }
}
