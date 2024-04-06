package com.makingscience.levelupproject.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
public class ExceptionBody {

    private String application_name;
    private String method_name;
    private String exception_name;
    private String status;
    private String message;
    private String timestamp;

    private ExceptionBody(String msg, String application_name, String method_name, String exception_name, String status) {
        this.message = msg;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss").withLocale(
                Locale.ENGLISH
        ));
        this.method_name = method_name;
        this.application_name = application_name;
        this.exception_name = exception_name;
        this.status = status;
    }


    public static ExceptionBody of(MethodArgumentNotValidException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.BAD_REQUEST.name();
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        String msg = objectErrorList.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()).toString();

        return new ExceptionBody(msg, application_name, method_name, exception_name, status);
    }


    public static ExceptionBody of(ResponseStatusException exception, String application_name, String method_name, String exception_name) {
        var status = exception.getStatusCode();
        String message = exception.getMessage();
        var exceptionBody = new ExceptionBody(message, application_name, method_name, exception_name, status.toString());
        return exceptionBody;
    }


    public static ExceptionBody of(MethodArgumentTypeMismatchException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.BAD_REQUEST.name();
        String msg = "Failed to convert value to required type " + e.getRequiredType();
        var exceptionBody = new ExceptionBody(msg, application_name, method_name, exception_name, status);
        return exceptionBody;
    }


    public static ExceptionBody of(Exception e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR.name();
        String msg = e.getMessage();

        var exceptionBody = new ExceptionBody(msg, application_name, method_name, exception_name, status);
        return exceptionBody;
    }

    public static ExceptionBody of(AccessDeniedException e, String application_name, String method_name, String exception_name) {
        var status = HttpStatus.FORBIDDEN.name();
        String msg = e.getMessage();

        var exceptionBody = new ExceptionBody(msg, application_name, method_name, exception_name, status);
        return exceptionBody;
    }


}
