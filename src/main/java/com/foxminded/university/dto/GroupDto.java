package com.foxminded.university.dto;

public class GroupDto {

    private Integer id;
    private String name;
    private String cathedraName;

    public GroupDto(Integer id, String name, String cathedraName) {
        this.id = id;
        this.name = name;
        this.cathedraName = cathedraName;
    }

    public GroupDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }
}
