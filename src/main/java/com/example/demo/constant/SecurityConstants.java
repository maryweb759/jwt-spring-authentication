package com.example.demo.constant;

public class SecurityConstants {
 
	public static final long EXPIRATION_TIME = 432_000_000; // 5 days expressed in milliseconds
	public static final String TOKEN_PREFIX =  "Bearer ";
	public static final String JWT_TOKEN_HEADER = "Jwt-Token";
	// if token tempared 
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    // the provided company name
    public static final String GET_ARRAYS_LLC = "LEARNING Meriem, LLC";
    // who is gonna be using the token
    public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    // if they have token but they are forbiden to access the resource 
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
   // if they dont have permission to access 
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    // if method is option we dont to check or take any actions
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    // urls to be access without security
    public static final String[] PUBLIC_URLS = { "/user/login", "/user/register", "/user/image/**" };
}
