package com.foxminded.university.dto;

import java.time.LocalTime;

public class LectureTimeDto {

    private Integer id;
    private LocalTime start;
    private LocalTime end;

    public LectureTimeDto(Integer id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public LectureTimeDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
