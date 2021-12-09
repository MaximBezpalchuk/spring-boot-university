package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;

import java.util.Optional;

public interface CathedraDao extends GenericDao<Cathedra> {

    Optional<Cathedra> findByName(String name);
}
