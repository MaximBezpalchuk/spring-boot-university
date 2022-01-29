package com.foxminded.university.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.validation.MinAge;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
@MinAge(20)
public class Teacher extends Person {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "{Cathedra.notNull}")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cathedra cathedra;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subjects_teachers", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    @NotEmpty(message = "{Subjects.teacher.notEmpty}")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Subject> subjects = new ArrayList<>();

    @Column
    @Enumerated(EnumType.STRING)
    private Degree degree;

    public Teacher(int id, String firstName, String lastName, String phone, String address, String email, Gender gender,
                   String postalCode, String education, LocalDate birthDate, Cathedra cathedra, List<Subject> subjects,
                   Degree degree) {
        super(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
        this.cathedra = cathedra;
        this.subjects = subjects;
        this.degree = degree;
    }

    public Teacher() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Cathedra getCathedra() {
        return cathedra;
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(cathedra, degree, subjects);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Teacher other = (Teacher) obj;
        return Objects.equals(cathedra, other.cathedra) && degree == other.degree
                && Objects.equals(subjects, other.subjects);
    }

    public static class Builder extends Person.Builder<Builder> {

        private Cathedra cathedra;
        private List<Subject> subjects = new ArrayList<>();
        private Degree degree;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder cathedra(Cathedra cathedra) {
            this.cathedra = cathedra;
            return this;
        }

        public Builder subjects(List<Subject> subjects) {
            this.subjects = subjects;
            return this;
        }

        public Builder degree(Degree degree) {
            this.degree = degree;
            return this;
        }

        public Teacher build() {
            return new Teacher(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate,
                    cathedra, subjects, degree);
        }

    }

}