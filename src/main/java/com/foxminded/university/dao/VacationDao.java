package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Vacation;

public interface VacationDao extends GenericDao<Vacation> {

	List<Vacation> findByTeacherId(int id);
}
