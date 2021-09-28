package com.foxminded.university.exception;

public class LectureOnHolidayException extends ServiceException {

	private static final long serialVersionUID = 5514958227818771916L;

	public LectureOnHolidayException(String errorMessage) {
		super(errorMessage);
	}
}
