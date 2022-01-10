package com.foxminded.university.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id")
    private Cathedra cathedra;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "lectures_groups", joinColumns = @JoinColumn(name = "lecture_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audience_id", referencedColumnName = "id")
    private Audience audience;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Lecture date should be entered")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_time_id", referencedColumnName = "id")
    private LectureTime time;

    private Lecture(int id, Cathedra cathedra, List<Group> groups, Teacher teacher, Audience audience, LocalDate date,
                    Subject subject, LectureTime time) {
        this.id = id;
        this.cathedra = cathedra;
        this.teacher = teacher;
        this.audience = audience;
        this.date = date;
        this.subject = subject;
        this.time = time;
        this.groups = groups;
    }

    public Lecture() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cathedra getCathedra() {
        return cathedra;
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public LectureTime getTime() {
        return time;
    }

    public void setTime(LectureTime time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(audience, cathedra, date, groups, id, subject, teacher, time);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lecture other = (Lecture) obj;
        return Objects.equals(audience, other.audience) && Objects.equals(cathedra, other.cathedra)
                && Objects.equals(date, other.date) && Objects.equals(groups, other.groups) && id == other.id
                && Objects.equals(subject, other.subject) && Objects.equals(teacher, other.teacher)
                && Objects.equals(time, other.time);
    }

    public static class Builder {

        private int id;
        private Cathedra cathedra;
        private List<Group> groups = new ArrayList<>();
        private Teacher teacher;
        private Audience audience;
        private LocalDate date;
        private Subject subject;
        private LectureTime time;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder cathedra(Cathedra cathedra) {
            this.cathedra = cathedra;
            return this;
        }

        public Builder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder time(LectureTime time) {
            this.time = time;
            return this;
        }

        public Builder audience(Audience audience) {
            this.audience = audience;
            return this;
        }

        public Builder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder group(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public Lecture build() {
            return new Lecture(id, cathedra, groups, teacher, audience, date, subject, time);
        }
    }

}
