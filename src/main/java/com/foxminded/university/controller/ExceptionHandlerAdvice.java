package com.foxminded.university.controller;

import com.foxminded.university.exception.EntityNotFoundException;
import com.foxminded.university.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(value = {ServiceException.class})
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ModelAndView serviceExceptions(Object handler, Exception ex) {
        return callExceptionModelAndView(handler, ex);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFountExceptions(Object handler, Exception ex) {
        return callExceptionModelAndView(handler, ex);
    }

    private ModelAndView callExceptionModelAndView(Object handler, Exception ex) {
        ModelAndView model = new ModelAndView("exception");
        model.addObject("exceptionType", ex);
        model.addObject("handlerMethod", handler);
        return model;
    }
}