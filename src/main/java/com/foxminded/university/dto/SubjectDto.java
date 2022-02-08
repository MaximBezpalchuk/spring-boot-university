package com.foxminded.university.dto;

public class SubjectDto {

    private Integer id;
    private String name;
    private String description;
    private CathedraDto cathedraDto;

    public SubjectDto(Integer id, String name, String description, CathedraDto cathedraDto) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cathedraDto = cathedraDto;
    }

    public SubjectDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CathedraDto getCathedraDto() {
        return cathedraDto;
    }

    public void setCathedraDto(CathedraDto cathedraDto) {
        this.cathedraDto = cathedraDto;
    }
}
