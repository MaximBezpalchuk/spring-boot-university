package com.foxminded.university.exception;

public class VacationDurationMoreThanMaxException extends Exception {

	private static final long serialVersionUID = 3371933798695849736L;

	public VacationDurationMoreThanMaxException(String errorMessage) {
		super(errorMessage);
	}
}