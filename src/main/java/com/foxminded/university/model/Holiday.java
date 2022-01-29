package com.foxminded.university.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "{Name.holiday.notBlank}")
    private String name;

    @Column
    @JsonSerialize(as = LocalDate.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{Date.notNull}")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "{Cathedra.notNull}")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cathedra cathedra;

    private Holiday(int id, String name, LocalDate date, Cathedra cathedra) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cathedra = cathedra;
    }

    public Holiday() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cathedra, date, id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Holiday other = (Holiday) obj;
        return Objects.equals(cathedra, other.cathedra) && Objects.equals(date, other.date) && id == other.id
                && Objects.equals(name, other.name);
    }

    public static class Builder {

        private int id;
        private String name;
        private LocalDate date;
        private Cathedra cathedra;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder cathedra(Cathedra cathedra) {
            this.cathedra = cathedra;
            return this;
        }

        public Holiday build() {
            return new Holiday(id, name, date, cathedra);
        }
    }

}
