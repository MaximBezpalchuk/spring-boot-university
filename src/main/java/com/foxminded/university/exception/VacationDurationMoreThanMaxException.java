package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class VacationDurationMoreThanMaxException extends ServiceLayerException {

	private static final long serialVersionUID = 3371933798695849736L;

	public VacationDurationMoreThanMaxException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
