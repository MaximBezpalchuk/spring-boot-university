package com.foxminded.university.exception;

public class DurationException extends ServiceException {

    private static final long serialVersionUID = 1703508329545329394L;

    public DurationException(String errorMessage) {
        super(errorMessage);
    }
}
