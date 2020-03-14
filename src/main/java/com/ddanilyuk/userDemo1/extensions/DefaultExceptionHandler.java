package com.ddanilyuk.userDemo1.extensions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestError> methodNotSupportErrorHandler(HttpServletRequest req, Exception e) {
        RestError error = new RestError("BadRequestException", 400, "Method not supported");
        return new ResponseEntity<RestError>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserExtension.class)
    public final ResponseEntity<RestError> handleNotFoundException(UserExtension ex, WebRequest request) {
        RestError error = new RestError("NotFoundException", 404, ex.getMessage());
        return new ResponseEntity<RestError>(error, HttpStatus.NOT_ACCEPTABLE);
    }

}
