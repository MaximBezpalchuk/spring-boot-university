package com.foxminded.university.model.dto;

public class CathedraDto {

    private Integer id;
    private String name;

    public CathedraDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CathedraDto() {
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
}
