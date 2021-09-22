package com.foxminded.university.exception;

public class DaoException extends RuntimeException {

	private static final long serialVersionUID = -5542267693784051034L;

	public DaoException(String errorMessage, Throwable error) {
		super(errorMessage, error);
	}
}
