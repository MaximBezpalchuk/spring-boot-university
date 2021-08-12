package com.foxminded.university.dao;

import java.util.List;

public interface GenericDao<T> {

	List<T> findAll();

	T findById(int id);

	void save(T entity);

	void deleteById(int id);
}
