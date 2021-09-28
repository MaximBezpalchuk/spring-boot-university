package com.foxminded.university.exception;

public class LectureWithTeacherInVacationException extends ServiceException {

	private static final long serialVersionUID = -15199716070488787L;

	public LectureWithTeacherInVacationException(String errorMessage) {
		super(errorMessage);
	}
}
