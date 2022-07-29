package com.example.demo.domain;

public class EmailExistsException extends RuntimeException {

	public EmailExistsException(String message) {
        super(message);
    }
}
