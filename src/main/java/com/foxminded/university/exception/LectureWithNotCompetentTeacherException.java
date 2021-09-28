package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureWithNotCompetentTeacherException extends ServiceException {

	private static final long serialVersionUID = -2006025556092963296L;

	public LectureWithNotCompetentTeacherException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
