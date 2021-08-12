package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Vacation;

public interface GenericVacationDao extends GenericDao<Vacation> {

	List<Vacation> findByTeacherId(int id);
}
