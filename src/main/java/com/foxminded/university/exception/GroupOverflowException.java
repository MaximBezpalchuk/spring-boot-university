package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class GroupOverflowException extends ServiceException {

	private static final long serialVersionUID = 6837761256895714271L;

	public GroupOverflowException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
