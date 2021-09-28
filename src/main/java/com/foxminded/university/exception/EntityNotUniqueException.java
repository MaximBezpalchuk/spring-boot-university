package com.foxminded.university.exception;

public class EntityNotUniqueException extends ServiceException {

	private static final long serialVersionUID = -682774621895081494L;

	public EntityNotUniqueException(String errorMessage) {
		super(errorMessage);
	}
}
