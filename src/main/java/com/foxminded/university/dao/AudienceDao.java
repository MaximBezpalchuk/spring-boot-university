package com.foxminded.university.dao;

import java.util.Optional;

import com.foxminded.university.model.Audience;

public interface AudienceDao extends GenericDao<Audience> {

	Optional<Audience> findByRoom(int room);

}
