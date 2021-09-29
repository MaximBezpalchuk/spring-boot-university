package com.foxminded.university.exception;

public class HolidayException extends ServiceException {

	private static final long serialVersionUID = 5514958227818771916L;

	public HolidayException(String errorMessage) {
		super(errorMessage);
	}
}
