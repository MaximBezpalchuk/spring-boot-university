package com.foxminded.university.dao;

import com.foxminded.university.model.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Integer> {

    Optional<Audience> findByRoom(int room);
}