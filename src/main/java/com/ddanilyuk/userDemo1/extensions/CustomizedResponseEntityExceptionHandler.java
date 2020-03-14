//package com.ddanilyuk.userDemo1.extensions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.util.Date;
//
//@ControllerAdvice
//@RestController
//public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(UserExtension.class)
//    public final ResponseEntity<ExceptionResponse> handleNotFoundException(UserExtension ex, WebRequest request) {
////        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
////                request.getDescription(false), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
//        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), "", HttpStatus.NOT_ACCEPTABLE.toString());
//        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
//    }
//
//    @ExceptionHandler(MethodNotSupportException.class)
//    public final ResponseEntity<ExceptionResponse> handleNotSupportException(MethodNotSupportException ex, WebRequest request) {
////        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
////                request.getDescription(false), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase());
//        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Method is not support", "", HttpStatus.METHOD_NOT_ALLOWED.toString());
//        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
//    }
//}
