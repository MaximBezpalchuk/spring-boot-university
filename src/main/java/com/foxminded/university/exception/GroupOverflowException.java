package com.foxminded.university.exception;

public class GroupOverflowException extends ServiceException {

	private static final long serialVersionUID = 6837761256895714271L;

	public GroupOverflowException(String errorMessage) {
		super(errorMessage);
	}
}
