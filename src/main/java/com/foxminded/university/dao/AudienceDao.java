package com.foxminded.university.dao;

import com.foxminded.university.model.Audience;

import java.util.Optional;

public interface AudienceDao extends GenericDao<Audience> {

    Optional<Audience> findByRoom(int room);

}
