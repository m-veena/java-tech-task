package com.rezdy.lunch.exception;

/**
 * This class is created to handle exception when requested recipe/resource is not available in DB
 * @author madhu
 *
 */

public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	 public ResourceNotFoundException(String message, Throwable cause) {
		 super(message,cause);
		 
	 }
	 
	 public ResourceNotFoundException(String message) {
         super(message);
     }
	 
	 public ResourceNotFoundException(Throwable cause) {
         super(cause);
     }
	 
}
