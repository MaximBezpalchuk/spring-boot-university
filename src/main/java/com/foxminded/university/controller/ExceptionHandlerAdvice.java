package com.foxminded.university.controller;

import com.foxminded.university.exception.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(value = {ServiceException.class})
    public ModelAndView exception(Exception exception, WebRequest request, Object handler, Exception ex) {
        ModelAndView model = new ModelAndView("exception");
        model.addObject("exceptionType", ex);
        model.addObject("handlerMethod", handler);
        return model;
    }
}