package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureTimeDurationMoreThanChosenTimeException extends ServiceException {

	private static final long serialVersionUID = 8555248693604462716L;

	public LectureTimeDurationMoreThanChosenTimeException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
