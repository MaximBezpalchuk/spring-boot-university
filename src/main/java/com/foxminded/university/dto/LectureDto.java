package com.foxminded.university.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class LectureDto {

    private Integer id;
    @NotNull(message = "{Cathedra.notNull}")
    private CathedraDto cathedra;
    @NotEmpty(message = "{Groups.lecture.notEmpty}")
    private List<GroupDto> groups;
    @NotNull(message = "{Teacher.notNull}")
    private TeacherDto teacher;
    @NotNull(message = "{Audience.notNull}")
    private AudienceDto audience;
    @NotNull(message = "{Date.notNull}")
    private LocalDate date;
    @NotNull(message = "{Subject.notNull}")
    private SubjectDto subject;
    @NotNull(message = "{LectureTime.notNull}")
    private LectureTimeDto time;

    public LectureDto(Integer id, CathedraDto cathedra, List<GroupDto> groups, TeacherDto teacher, AudienceDto audience, LocalDate date, SubjectDto subject, LectureTimeDto time) {
        this.id = id;
        this.cathedra = cathedra;
        this.groups = groups;
        this.teacher = teacher;
        this.audience = audience;
        this.date = date;
        this.subject = subject;
        this.time = time;
    }

    public LectureDto() {
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

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public TeacherDto getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDto teacher) {
        this.teacher = teacher;
    }

    public AudienceDto getAudience() {
        return audience;
    }

    public void setAudience(AudienceDto audience) {
        this.audience = audience;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public LectureTimeDto getTime() {
        return time;
    }

    public void setTime(LectureTimeDto time) {
        this.time = time;
    }
}
