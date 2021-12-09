package com.foxminded.university.exception;

public class NotCompetentTeacherException extends ServiceException {

    private static final long serialVersionUID = -2006025556092963296L;

    public NotCompetentTeacherException(String errorMessage) {
        super(errorMessage);
    }
}
