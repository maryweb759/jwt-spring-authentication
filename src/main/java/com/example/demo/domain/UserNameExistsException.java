package com.example.demo.domain;

public class UserNameExistsException extends RuntimeException {
	public UserNameExistsException(String message) {
		super(message);
	}
}
