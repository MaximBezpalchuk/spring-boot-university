package com.foxminded.university.exception;

public class AudienceOverflowException extends ServiceException {

    private static final long serialVersionUID = -4852735498788343348L;

    public AudienceOverflowException(String errorMessage) {
        super(errorMessage);
    }
}
