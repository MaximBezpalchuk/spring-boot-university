package com.foxminded.university.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class LectureDto {

    private Integer id;
    private String cathedraName;
    private List<String> groupNames;
    private TeacherDto teacherDto;
    private Integer audienceRoom;
    private LocalDate date;
    private String subjectName;
    private LocalTime start;
    private LocalTime end;

    public LectureDto(Integer id, String cathedraName, List<String> groupNames, TeacherDto teacherDto, Integer audienceRoom, LocalDate date, String subjectName, LocalTime start, LocalTime end) {
        this.id = id;
        this.cathedraName = cathedraName;
        this.groupNames = groupNames;
        this.teacherDto = teacherDto;
        this.audienceRoom = audienceRoom;
        this.date = date;
        this.subjectName = subjectName;
        this.start = start;
        this.end = end;
    }

    public LectureDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCathedraName() {
        return cathedraName;
    }

    public void setCathedraName(String cathedraName) {
        this.cathedraName = cathedraName;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(List<String> groupNames) {
        this.groupNames = groupNames;
    }

    public TeacherDto getTeacherDto() {
        return teacherDto;
    }

    public void setTeacherDto(TeacherDto teacherDto) {
        this.teacherDto = teacherDto;
    }

    public Integer getAudienceRoom() {
        return audienceRoom;
    }

    public void setAudienceRoom(Integer audienceRoom) {
        this.audienceRoom = audienceRoom;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
