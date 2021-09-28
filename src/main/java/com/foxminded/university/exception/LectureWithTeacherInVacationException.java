package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureWithTeacherInVacationException extends ServiceException {

	private static final long serialVersionUID = -15199716070488787L;

	public LectureWithTeacherInVacationException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
