package com.foxminded.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GroupDto {

    private Integer id;
    @NotBlank(message = "{Name.group.notBlank}")
    private String name;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;

    public GroupDto(Integer id, String name, CathedraDto cathedra) {
        this.id = id;
        this.name = name;
        this.cathedra = cathedra;
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

    public CathedraDto getCathedra() {
        return cathedra;
    }

    public void setCathedra(CathedraDto cathedra) {
        this.cathedra = cathedra;
    }
}
