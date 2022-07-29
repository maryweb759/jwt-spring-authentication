package com.example.demo.exception;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.BindException;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.controller.UserResource;
import com.example.demo.domain.EmailExistsException;
import com.example.demo.domain.EmailNotFoundException;
import com.example.demo.domain.HttpResponse;
import com.example.demo.domain.UserNameExistsException;
import com.example.demo.domain.UserNotFoundException;
import static com.example.demo.utils.HttpResponseUtility.createHttpResponse;

//@RestControllerAdvice(basePackageClasses = {UserResource.class})
@RestControllerAdvice
public class ExceptionHanding {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED ="Your account has been disabled. If this is an error, please contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission"; 
    
    // The @ExceptionHandler annotation indicates which type of Exception we want to handle.
    @ExceptionHandler({DisabledException.class}) 
    public ResponseEntity<HttpResponse> accountDisabledException() {
    	return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler({BadCredentialsException.class}) 
    public ResponseEntity<HttpResponse> badCredentialsException() {
    	return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }
    
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException exception) {
//        return createHttpResponse(BAD_REQUEST, "This page was not found");
//    }
    
    @ExceptionHandler({AccessDeniedException.class}) 
    public ResponseEntity<HttpResponse> accessDeniedException() {
    	return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }
    @ExceptionHandler({
    		EmailExistsException.class, EmailNotFoundException.class,
    		UserNameExistsException.class,UserNotFoundException.class
    		})
    public ResponseEntity<HttpResponse> badRequestExceptionHandler(Exception exception) {
        return createHttpResponse(BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(UNAUTHORIZED, exception.getMessage());
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod method = Objects.requireNonNull(exception.getSupportedHttpMethods().iterator().next());
    	return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, method));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
    	LOGGER.error(exception.getMessage(), exception);
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class}) 
    public ResponseEntity<HttpResponse> validationExceptionHandler(MethodArgumentNotValidException ex ) {
    	String fieldsWitherrors = ex.getBindingResult().getFieldErrors().stream()
    			.map(fe -> fe.getField() + ":" + fe.getDefaultMessage())
    			.collect(Collectors.joining(","));
    	String message = "Error(s) in parameters: [" + fieldsWitherrors + "]";
    	return createHttpResponse(BAD_REQUEST, message);
    }
    
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        //logger.error(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        //log.error(exception.getMessage());
    	
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }
//	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status, String message) {
//		HttpResponse httpResponse = new HttpResponse();
//		httpResponse.setHttpStatus(status);
//		httpResponse.setHttpStatusCode(status.value());
//		httpResponse.setReason(status.getReasonPhrase().toUpperCase());
//		httpResponse.setMessage(message); 
//		return new ResponseEntity<>(httpResponse, status);
//	}

}
