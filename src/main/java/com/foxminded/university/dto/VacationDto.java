package com.foxminded.university.dto;

import com.foxminded.university.validation.CorrectPeriod;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@CorrectPeriod
public class VacationDto {

    private Integer id;
    @NotNull(message = "{Start.vacation.notNull}")
    private LocalDate start;
    @NotNull(message = "{End.vacation.notNull}")
    private LocalDate end;
    @NotNull(message = "{Teacher.notNull}")
    private TeacherDto teacher;

    public VacationDto(Integer id, LocalDate start, LocalDate end, TeacherDto teacher) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
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

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacherDto) {
        this.teacher = teacherDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacationDto that = (VacationDto) o;
        return id.equals(that.id) &&
            start.equals(that.start) &&
            end.equals(that.end) &&
            teacher.equals(that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, teacher);
    }
}
