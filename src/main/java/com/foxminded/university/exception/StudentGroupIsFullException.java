package com.foxminded.university.exception;

public class StudentGroupIsFullException extends Exception {

	private static final long serialVersionUID = 6837761256895714271L;

	public StudentGroupIsFullException(String errorMessage) {
		super(errorMessage);
	}
}
