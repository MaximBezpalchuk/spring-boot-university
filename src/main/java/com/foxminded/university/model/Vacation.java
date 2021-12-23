package com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@NamedQueries(
    {
        @NamedQuery(
            name = "Vacation.findByTeacherId",
            query = "FROM Vacation WHERE teacher.id=:teacher_id"
        ),
        @NamedQuery(
            name = "Vacation.findByTeacherIdAndYear",
            query = "FROM Vacation WHERE teacher.id=:teacher_id AND EXTRACT(YEAR FROM start)=:year"
        )
    })
@NamedNativeQueries(
    {
        @NamedNativeQuery(
            name = "Vacation.findByPeriodAndTeacher",
            query = "SELECT * FROM vacations WHERE start=:start AND finish=:end AND teacher_id=:teacher_id"
        ),
        @NamedNativeQuery(
            name = "Vacation.findByDateInPeriodAndTeacher",
            query = "SELECT * FROM vacations WHERE start >=:date AND finish<=:date AND teacher_id=:teacher_id"
        )
    })

@Entity
@Table(name = "vacations")
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @Column(name = "finish")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    private Vacation(int id, LocalDate start, LocalDate end, Teacher teacher) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }

    public Vacation() {
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int id;
        private LocalDate start;
        private LocalDate end;
        private Teacher teacher;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder start(LocalDate start) {
            this.start = start;
            return this;
        }

        public Builder end(LocalDate end) {
            this.end = end;
            return this;
        }

        public Builder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Vacation build() {
            return new Vacation(id, start, end, teacher);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(end, id, start, teacher);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vacation other = (Vacation) obj;
        return Objects.equals(end, other.end) && id == other.id && Objects.equals(start, other.start)
            && Objects.equals(teacher, other.teacher);
    }

}
