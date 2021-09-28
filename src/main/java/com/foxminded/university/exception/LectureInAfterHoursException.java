package com.foxminded.university.exception;

public class LectureInAfterHoursException extends ServiceException {

	private static final long serialVersionUID = -6303613547720565133L;

	public LectureInAfterHoursException(String errorMessage) {
		super(errorMessage);
	}
}
