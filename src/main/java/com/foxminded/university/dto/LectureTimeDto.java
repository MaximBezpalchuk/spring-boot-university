package com.foxminded.university.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

public class LectureTimeDto {

    private Integer id;
    @NotNull(message = "{LectureTime.start.notNull}")
    private LocalTime start;
    @NotNull(message = "{LectureTime.end.notNull}")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTimeDto that = (LectureTimeDto) o;
        return id.equals(that.id) &&
            start.equals(that.start) &&
            end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end);
    }
}
