package com.foxminded.university.dto;

import java.time.LocalDate;

public class VacationDto {

    private Integer id;
    private LocalDate start;
    private LocalDate end;
    private TeacherDto teacherDto;

    public VacationDto(Integer id, LocalDate start, LocalDate end, TeacherDto teacherDto) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.teacherDto = teacherDto;
    }

    public VacationDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public TeacherDto getTeacherDto() {
        return teacherDto;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacherDto = teacherDto;
    }
}
