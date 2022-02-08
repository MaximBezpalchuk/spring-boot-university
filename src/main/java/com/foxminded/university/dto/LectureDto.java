package com.foxminded.university.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class LectureDto {

    private Integer id;
    private CathedraDto cathedraDto;
    private List<GroupDto> groupDtos;
    private TeacherDto teacherDto;
    private AudienceDto audienceDto;
    private LocalDate date;
    private SubjectDto subjectDto;
    private LectureTimeDto lectureTimeDto;

    public LectureDto(Integer id, CathedraDto cathedraDto, List<GroupDto> groupDtos, TeacherDto teacherDto, AudienceDto audienceDto, LocalDate date, SubjectDto subjectDto, LectureTimeDto lectureTimeDto) {
        this.id = id;
        this.cathedraDto = cathedraDto;
        this.groupDtos = groupDtos;
        this.teacherDto = teacherDto;
        this.audienceDto = audienceDto;
        this.date = date;
        this.subjectDto = subjectDto;
        this.lectureTimeDto = lectureTimeDto;
    }

    public LectureDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CathedraDto getCathedraDto() {
        return cathedraDto;
    }

    public void setCathedraDto(CathedraDto cathedraDto) {
        this.cathedraDto = cathedraDto;
    }

    public List<GroupDto> getGroupDtos() {
        return groupDtos;
    }

    public void setGroupDtos(List<GroupDto> groupDtos) {
        this.groupDtos = groupDtos;
    }

    public TeacherDto getTeacherDto() {
        return teacherDto;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacherDto = teacherDto;
    }

    public AudienceDto getAudienceDto() {
        return audienceDto;
    }

    public void setAudienceDto(AudienceDto audienceDto) {
        this.audienceDto = audienceDto;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public SubjectDto getSubjectDto() {
        return subjectDto;
    }

    public void setSubjectDto(SubjectDto subjectDto) {
        this.subjectDto = subjectDto;
    }

    public LectureTimeDto getLectureTimeDto() {
        return lectureTimeDto;
    }

    public void setLectureTimeDto(LectureTimeDto lectureTimeDto) {
        this.lectureTimeDto = lectureTimeDto;
    }
}
