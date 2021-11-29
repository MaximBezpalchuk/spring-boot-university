package com.foxminded.university.dao;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

	List<T> findAll();

	Optional<T> findById(int id);

	void save(T entity);

	void deleteById(int id);

	default <T> Optional<T> findOrEmpty(final DaoRetriever<T> retriever) {
		try {
			return Optional.of(retriever.retrieve());
		} catch (Exception ex) {
			return Optional.empty();
		}
	}
}
