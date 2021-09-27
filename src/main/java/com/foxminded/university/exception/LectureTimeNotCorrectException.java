package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureTimeNotCorrectException extends ServiceLayerException {

	private static final long serialVersionUID = 792552899851516825L;

	public LectureTimeNotCorrectException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
