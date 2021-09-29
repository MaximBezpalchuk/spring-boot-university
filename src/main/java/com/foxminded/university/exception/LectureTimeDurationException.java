package com.foxminded.university.exception;

public class LectureTimeDurationException extends ServiceException {

	private static final long serialVersionUID = 792552899851516825L;

	public LectureTimeDurationException(String errorMessage) {
		super(errorMessage);
	}
}
