package com.example.demo.domain;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class HttpResponse {
    private  LocalDateTime timestamp = LocalDateTime.now();
	private int httpStatusCode; //200, 400, 500 
    private HttpStatus httpStatus; 
    private String reason; // msg from HttpStatus
    private String message; // msg that developper send back 
    
    public HttpResponse() {
    	
    }

	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
		this.httpStatusCode = httpStatusCode;
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
		this.timestamp = LocalDateTime.now();
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	} 
    
    
}
