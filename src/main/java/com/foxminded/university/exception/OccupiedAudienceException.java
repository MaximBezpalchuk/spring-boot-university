package com.foxminded.university.exception;

public class OccupiedAudienceException extends ServiceException {

	private static final long serialVersionUID = 671307787028933096L;

	public OccupiedAudienceException(String errorMessage) {
		super(errorMessage);
	}
}
