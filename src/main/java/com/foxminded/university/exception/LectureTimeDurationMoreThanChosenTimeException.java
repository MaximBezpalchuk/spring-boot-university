package com.foxminded.university.exception;

public class LectureTimeDurationMoreThanChosenTimeException extends ServiceException {

	private static final long serialVersionUID = 8555248693604462716L;

	public LectureTimeDurationMoreThanChosenTimeException(String errorMessage) {
		super(errorMessage);
	}
}
