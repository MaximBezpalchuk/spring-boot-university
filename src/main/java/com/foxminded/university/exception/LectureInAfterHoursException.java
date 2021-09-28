package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureInAfterHoursException extends ServiceException {

	private static final long serialVersionUID = -6303613547720565133L;

	public LectureInAfterHoursException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
