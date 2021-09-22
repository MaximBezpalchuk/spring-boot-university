package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.exception.DaoException;

public interface GenericDao<T> {

	List<T> findAll();

	T findById(int id) throws DaoException;

	void save(T entity);

	void deleteById(int id);
}
