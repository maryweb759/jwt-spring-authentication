package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.EmailExistsException;
import com.example.demo.domain.UserNotFoundException;

@RestController
@RequestMapping("/exceptions")
public class ExceptionController {

	@GetMapping("/emailExists")
    public String emailExistsException() throws EmailExistsException {
        throw new EmailExistsException("This email is already taken");
    }

    @GetMapping("/userNotFound")
    public String userNotFoundException() throws  UserNotFoundException {
        throw new UserNotFoundException("The user was not found");
    }
}
