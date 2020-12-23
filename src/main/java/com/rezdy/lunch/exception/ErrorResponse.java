package com.rezdy.lunch.exception;

import java.util.Date;

/**
* This class is created to return custom error message to the client when exceptions occur
* @author madhu
*
*/

public class ErrorResponse {
	private Date timeStamp;
	private String errorMessage;
	private String status;
	private String details;
	
	public ErrorResponse(Date timeStamp,String errorMessage,String details, String status) {
		super();
		this.timeStamp = timeStamp;
		this.errorMessage = errorMessage;	
		this.details = details;
		this.status = status;
	}
	
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	  
}
