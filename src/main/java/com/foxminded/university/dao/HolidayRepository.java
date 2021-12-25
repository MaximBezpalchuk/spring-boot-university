package com.foxminded.university.dao;

import com.foxminded.university.model.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    Optional<Holiday> findByNameAndDate(String name, LocalDate date);

    List<Holiday> findByDate(LocalDate date);

    Page<Holiday> findAll(Pageable pageable);
}
