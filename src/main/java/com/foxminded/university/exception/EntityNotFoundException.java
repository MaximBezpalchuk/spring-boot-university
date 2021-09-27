package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class EntityNotFoundException extends ServiceLayerException {

	private static final long serialVersionUID = 498582879177323236L;

	public EntityNotFoundException(String errorMessage, @Nullable Object... args) {
		super(errorMessage, args);
	}
}
