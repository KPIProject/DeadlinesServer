package com.ddanilyuk.DeadlinesServer.extensions;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.transaction.NotSupportedException;


@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<RestMessage> handleException(NoHandlerFoundException ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("Exception", 404, "Unknown error");
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {DataException.class})
    public ResponseEntity<RestMessage> handleDataException(DataException ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("DataException", 404, "DataException");
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {NotSupportedException.class})
    public ResponseEntity<RestMessage> handleNotSupportedException(NotSupportedException ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("NotSupportedException", 405, "Method not supported");
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<RestMessage> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("HttpRequestMethodNotSupportedException", 405, "Method not allowed");
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Not work
    @ExceptionHandler(value = {HttpClientErrorException.NotFound.class})
    public ResponseEntity<RestMessage> handleMethodNotFoundException(HttpClientErrorException.NotFound ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("HttpClientErrorException.NotFound", 404, "Not found");
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ServiceException.class})
    public ResponseEntity<RestMessage> handleServiceException(ServiceException ex, WebRequest request) {
        RestMessage errorMessage = new RestMessage("Error", 404, ex.getMessage());
        return new ResponseEntity<RestMessage>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

// коли видалився юзер, пишеш сров нью. Створює сообщ з типом саксес з кодом 200 і видається сообщ що ти написав
    @ExceptionHandler(value = {SuccessException.class})
    public ResponseEntity<RestMessage> handleSuccessException(SuccessException ex, WebRequest request) {
        RestMessage successMessage = new RestMessage("Success", 200, ex.getMessage());
        return new ResponseEntity<RestMessage>(successMessage, new HttpHeaders(), HttpStatus.ACCEPTED);
    }

}
