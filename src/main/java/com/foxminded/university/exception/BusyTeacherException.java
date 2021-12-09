package com.foxminded.university.exception;

public class BusyTeacherException extends ServiceException {

    private static final long serialVersionUID = -5519529451211215102L;

    public BusyTeacherException(String errorMessage) {
        super(errorMessage);
    }
}