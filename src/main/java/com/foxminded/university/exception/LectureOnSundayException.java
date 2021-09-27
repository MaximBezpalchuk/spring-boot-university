package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureOnSundayException extends ServiceLayerException {

	private static final long serialVersionUID = -785481990391196322L;

	public LectureOnSundayException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
