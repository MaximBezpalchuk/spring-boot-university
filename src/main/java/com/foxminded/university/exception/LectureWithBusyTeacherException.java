package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureWithBusyTeacherException extends ServiceException {

	private static final long serialVersionUID = -5519529451211215102L;

	public LectureWithBusyTeacherException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}