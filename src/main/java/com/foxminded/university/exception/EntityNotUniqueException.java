package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class EntityNotUniqueException extends ServiceException {

	private static final long serialVersionUID = -682774621895081494L;

	public EntityNotUniqueException(String errorMessage, @Nullable Object... args) {
		super(errorMessage, args);
	}
}
