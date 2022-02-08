package com.foxminded.university.dto;

import java.time.LocalDate;

public class HolidayDto {

    private Integer id;
    private String name;
    private LocalDate date;
    private CathedraDto cathedraDto;

    public HolidayDto(Integer id, String name, LocalDate date, CathedraDto cathedraDto) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cathedraDto = cathedraDto;
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

    public CathedraDto getCathedraDto() {
        return cathedraDto;
    }

    public void setCathedraDto(CathedraDto cathedraDto) {
        this.cathedraDto = cathedraDto;
    }
}
