package com.foxminded.university.dto;

public class SubjectDto {

    private Integer id;
    private String name;
    private String description;
    private String cathedraName;

    public SubjectDto(Integer id, String name, String description, String cathedraName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cathedraName = cathedraName;
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

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }
}
