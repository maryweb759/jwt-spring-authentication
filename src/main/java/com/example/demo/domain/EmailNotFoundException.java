package com.example.demo.domain;

public class EmailNotFoundException extends RuntimeException {
	public EmailNotFoundException(String message) {
		 super(message);
	 }
}
