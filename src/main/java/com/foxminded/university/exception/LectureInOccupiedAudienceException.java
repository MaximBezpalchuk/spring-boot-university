package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureInOccupiedAudienceException extends ServiceException {

	private static final long serialVersionUID = 671307787028933096L;

	public LectureInOccupiedAudienceException(String errorMessage, @Nullable Object... args) {
		super(errorMessage, args);
	}
}
