package com.rezdy.lunch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;
import java.util.Date;

@RestControllerAdvice
public class LunchExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(DateTimeParseException exception, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(new Date(), new StringBuilder().append("Expected DateFormat - yyyy-MM-dd ").append(exception.getMessage()).toString(), request.getDescription(false), HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)   
    public ResponseEntity<?> handleResourceException(ResourceNotFoundException  exception, WebRequest request) {
	    return new ResponseEntity<>(new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
    } 
   
   @ExceptionHandler(InvalidIngredientsListException.class)   
   public ResponseEntity<?> handleIngredientsException(InvalidIngredientsListException  exception, WebRequest request) {
	     return new ResponseEntity<>(new ErrorResponse(new Date(), exception.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
   }
}
