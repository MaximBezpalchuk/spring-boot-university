package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureOnHolidayException extends ServiceLayerException {

	private static final long serialVersionUID = 5514958227818771916L;

	public LectureOnHolidayException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
