package com.foxminded.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDto groupDto = (GroupDto) o;
        return id.equals(groupDto.id) &&
            name.equals(groupDto.name) &&
            cathedra.equals(groupDto.cathedra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cathedra);
    }
}
