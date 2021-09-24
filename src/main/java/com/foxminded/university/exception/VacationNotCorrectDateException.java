package com.foxminded.university.exception;

public class VacationNotCorrectDateException extends Exception {

	private static final long serialVersionUID = -6044831096455817694L;

	public VacationNotCorrectDateException(String errorMessage) {
		super(errorMessage);
	}
}