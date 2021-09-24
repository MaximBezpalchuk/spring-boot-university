package com.foxminded.university.exception;

public class LectureTimeNotCorrectException extends Exception {

	private static final long serialVersionUID = 792552899851516825L;

	public LectureTimeNotCorrectException(String errorMessage) {
		super(errorMessage);
	}
}
