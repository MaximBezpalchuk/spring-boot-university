package com.foxminded.university.exception;

public class VacationLessOneDayException extends ServiceException {

	private static final long serialVersionUID = 1703508329545329394L;

	public VacationLessOneDayException(String errorMessage) {
		super(errorMessage);
	}
}
