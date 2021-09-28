package com.foxminded.university.exception;

public class LectureWithNotCompetentTeacherException extends ServiceException {

	private static final long serialVersionUID = -2006025556092963296L;

	public LectureWithNotCompetentTeacherException(String errorMessage) {
		super(errorMessage);
	}
}
