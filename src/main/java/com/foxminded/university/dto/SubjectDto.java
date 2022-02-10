package com.foxminded.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubjectDto {

    private Integer id;
    @NotBlank(message = "{Name.subject.notBlank}")
    private String name;
    @NotBlank(message = "{Description.subject.notBlank}")
    private String description;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;

    public SubjectDto(Integer id, String name, String description, CathedraDto cathedra) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cathedra = cathedra;
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

    public CathedraDto getCathedra() {
        return cathedra;
    }

    public void setCathedra(CathedraDto cathedra) {
        this.cathedra = cathedra;
    }
}
