package com.example.demo.config;

import static com.example.demo.constant.SecurityConstants.GET_ARRAYS_LLC;
import static com.example.demo.constant.SecurityConstants.TOKEN_CANNOT_BE_VERIFIED;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

@Configuration
public class JWTConfig {

	// this will return JWT verifier  
	        @Bean
			public JWTVerifier jwtVerifier(@Value("${jwt.secret}") String secret) {
		        Algorithm algorithm = Algorithm.HMAC256(secret);
				//try {
					return JWT.require(algorithm)
							.withIssuer(GET_ARRAYS_LLC)
							.build(); //Reusable verifier instance
//				} catch (JWTVerificationException e) {
//				 throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
//				}
				
			} 
}
