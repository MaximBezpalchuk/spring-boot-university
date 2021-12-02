package com.foxminded.university.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1061524633610399643L;

    public ServiceException(String errorMessage) {
        super(errorMessage);
    }

}
