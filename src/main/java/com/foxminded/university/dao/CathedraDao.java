package com.foxminded.university.dao;

import java.util.Optional;

import com.foxminded.university.model.Cathedra;

public interface CathedraDao extends GenericDao<Cathedra> {

	Optional<Cathedra> findByName(String name);
}
