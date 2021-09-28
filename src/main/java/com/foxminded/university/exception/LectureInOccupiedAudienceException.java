package com.foxminded.university.exception;

public class LectureInOccupiedAudienceException extends ServiceException {

	private static final long serialVersionUID = 671307787028933096L;

	public LectureInOccupiedAudienceException(String errorMessage) {
		super(errorMessage);
	}
}
