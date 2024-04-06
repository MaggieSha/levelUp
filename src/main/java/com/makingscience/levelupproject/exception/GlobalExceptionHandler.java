package com.makingscience.levelupproject.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Value("${spring.application.name}")
    private String applicationName;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validExceptionHandler(MethodArgumentNotValidException ex,
                                                        HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        log.error("Exception is {}",ex);
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(ResponseStatusException.class)
    public  ResponseEntity<Object> responseStatusExceptionHandler(ResponseStatusException exception,
                                     HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = exception.getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        log.error("Exception is {}",exception);
        return new ResponseEntity<>(ExceptionBody.of(exception, applicationName,
                methodName, exceptionName), exception.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        log.error("Exception is {}",ex);
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, methodName, exceptionName),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> otherExceptionHandler(Exception ex,
                                                        HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        log.error("Exception is {}",ex);
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedExceptionHandler(AccessDeniedException ex,
                                                               HandlerMethod handlerMethod, HttpServletRequest request){
        String exceptionName = ex.getClass().getName();
        String MethodName = handlerMethod.getMethod().getName();
        log.error("Exception is {}",ex);
        return new ResponseEntity<>(ExceptionBody.of(ex, applicationName, MethodName, exceptionName),
                HttpStatus.FORBIDDEN);
    }

}
