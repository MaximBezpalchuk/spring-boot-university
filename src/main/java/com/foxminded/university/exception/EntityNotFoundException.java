package com.foxminded.university.exception;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 498582879177323236L;

	public EntityNotFoundException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}
