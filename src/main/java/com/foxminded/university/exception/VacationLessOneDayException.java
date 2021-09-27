package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class VacationLessOneDayException extends ServiceLayerException {

	private static final long serialVersionUID = 1703508329545329394L;

	public VacationLessOneDayException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
