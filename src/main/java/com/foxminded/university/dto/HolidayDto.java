package com.foxminded.university.dto;

import java.time.LocalDate;

public class HolidayDto {

    private Integer id;
    private String name;
    private LocalDate date;
    private String cathedraName;

    public HolidayDto(Integer id, String name, LocalDate date, String cathedraName) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cathedraName = cathedraName;
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

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }
}
