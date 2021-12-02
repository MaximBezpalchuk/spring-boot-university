package com.foxminded.university.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends Person {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(int id, String firstName, String lastName, String phone, String address, String email,
                   Gender gender, String postalCode, String education, LocalDate birthDate, Group group) {
        super(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate);
        this.group = group;
    }

    public Student() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(group);
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
        Student other = (Student) obj;
        return Objects.equals(group, other.group);
    }

    public static class Builder extends Person.Builder<Builder> {

        private Group group;

        @Override
        public Builder getThis() {
            return this;
        }

        public Builder group(Group group) {
            this.group = group;
            return this;
        }

        public Student build() {
            return new Student(id, firstName, lastName, phone, address, email, gender, postalCode, education, birthDate,
                    group);
        }
    }

}
