package com.foxminded.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class HolidayDto {

    private Integer id;
    @NotBlank(message = "{Name.holiday.notBlank}")
    private String name;
    @NotNull(message = "{Date.notNull}")
    private LocalDate date;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;

    public HolidayDto(Integer id, String name, LocalDate date, CathedraDto cathedra) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cathedra = cathedra;
    }

    public HolidayDto() {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CathedraDto getCathedra() {
        return cathedra;
    }

    public void setCathedra(CathedraDto cathedra) {
        this.cathedra = cathedra;
    }
}
