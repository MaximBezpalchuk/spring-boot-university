package com.foxminded.university.exception;

public class AfterHoursException extends ServiceException {

	private static final long serialVersionUID = -6303613547720565133L;

	public AfterHoursException(String errorMessage) {
		super(errorMessage);
	}
}
