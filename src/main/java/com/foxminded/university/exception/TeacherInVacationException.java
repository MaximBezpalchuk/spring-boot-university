package com.foxminded.university.exception;

public class TeacherInVacationException extends ServiceException {

	private static final long serialVersionUID = -15199716070488787L;

	public TeacherInVacationException(String errorMessage) {
		super(errorMessage);
	}
}
