package com.foxminded.university.dto;

public class GroupDto {

    private Integer id;
    private String name;
    private CathedraDto cathedraDto;

    public GroupDto(Integer id, String name, CathedraDto cathedraDto) {
        this.id = id;
        this.name = name;
        this.cathedraDto = cathedraDto;
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

    public CathedraDto getCathedraDto() {
        return cathedraDto;
    }

    public void setCathedraDto(CathedraDto cathedraDto) {
        this.cathedraDto = cathedraDto;
    }
}
