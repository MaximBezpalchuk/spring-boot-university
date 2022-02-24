package com.foxminded.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SubjectDto {

    private Integer id;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;
    @NotBlank(message = "{Name.subject.notBlank}")
    private String name;
    @NotBlank(message = "{Description.subject.notBlank}")
    private String description;

    public SubjectDto(Integer id, CathedraDto cathedra, String name, String description) {
        this.id = id;
        this.cathedra = cathedra;
        this.name = name;
        this.description = description;
    }

    public SubjectDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CathedraDto getCathedra() {
        return cathedra;
    }

    public void setCathedra(CathedraDto cathedra) {
        this.cathedra = cathedra;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubjectDto that = (SubjectDto) o;
        return id.equals(that.id) &&
            cathedra.equals(that.cathedra) &&
            name.equals(that.name) &&
            description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cathedra, name, description);
    }
}
