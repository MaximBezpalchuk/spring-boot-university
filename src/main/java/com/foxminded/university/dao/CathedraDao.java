package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CathedraDao extends JpaRepository<Cathedra, Integer> {

    Optional<Cathedra> findByName(String name);
}