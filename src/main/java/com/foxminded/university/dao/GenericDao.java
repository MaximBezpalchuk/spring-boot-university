package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {

    List<T> findAll();

    Optional<T> findById(int id);

    void save(T entity);

    void delete(T entity);

    default <T> Optional<T> findOrEmpty(final DaoRetriever<T> retriever) {
        try {
            return Optional.of(retriever.retrieve());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
