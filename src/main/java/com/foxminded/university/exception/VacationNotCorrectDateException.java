package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class VacationNotCorrectDateException extends ServiceException {

	private static final long serialVersionUID = -6044831096455817694L;

	public VacationNotCorrectDateException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
