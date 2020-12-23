package com.rezdy.lunch.exception;

/**
 * This exception is created to handle unexpected input ingredients from the client such as cheese%salad
 * Expected input needs to be cheese,bread
 * @author madhu
 *
 */

public class InvalidIngredientsListException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	 public InvalidIngredientsListException(String message, Throwable cause) {
		 super(message,cause);
		 
	 }
	 
	 public InvalidIngredientsListException(String message) {
         super(message);
     }
	 
	 public InvalidIngredientsListException(Throwable cause) {
         super(cause);
     }

}
