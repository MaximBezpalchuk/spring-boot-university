package com.foxminded.university.dao;

import com.foxminded.university.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupDao extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);
}
