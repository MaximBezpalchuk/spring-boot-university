package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class ServiceLayerException extends RuntimeException {

	private static final long serialVersionUID = 1061524633610399643L;

	@Nullable
	private final Object[] args;

	public ServiceLayerException(String errorMessage, @Nullable Object... args) {
		super(errorMessage);
		this.args = args;	
	}

	public Object[] getArgs() {
		return args;
	}
}
