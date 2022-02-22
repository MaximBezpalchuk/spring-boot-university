package com.foxminded.university.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CathedraDto that = (CathedraDto) o;
        return id.equals(that.id) &&
            name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
