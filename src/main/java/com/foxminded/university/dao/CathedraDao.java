package com.foxminded.university.dao;

import com.foxminded.university.model.Cathedra;

public interface CathedraDao extends GenericDao<Cathedra> {
	
	Cathedra findByName(String name);
}
