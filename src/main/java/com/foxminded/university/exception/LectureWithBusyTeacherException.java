package com.foxminded.university.exception;

public class LectureWithBusyTeacherException extends Exception {

	private static final long serialVersionUID = -5519529451211215102L;

	public LectureWithBusyTeacherException(String errorMessage) {
		super(errorMessage);
	}
}