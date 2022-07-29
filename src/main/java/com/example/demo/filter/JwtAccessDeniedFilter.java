package com.example.demo.filter;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.constant.SecurityConstants;
import com.example.demo.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAccessDeniedFilter implements AccessDeniedHandler {
    private  ObjectMapper  objectMapper; 
    
    public JwtAccessDeniedFilter(ObjectMapper  objectMapper) {
  	  this.objectMapper = objectMapper;
    }
    
    
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		 var httpResponse = new HttpResponse();
   	  httpResponse.setHttpStatus(UNAUTHORIZED);
   	  httpResponse.setHttpStatusCode(UNAUTHORIZED.value());
   	  httpResponse.setMessage(SecurityConstants.ACCESS_DENIED_MESSAGE);
   	  httpResponse.setReason(UNAUTHORIZED.getReasonPhrase().toUpperCase());
   	 // String JsonString = objectMapper.writeValueAsString(httpResponse);
         response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         //response.sendError(UNAUTHORIZED.value(), JsonString);
   		OutputStream outputStream = response.getOutputStream();
		objectMapper.writeValue(outputStream, httpResponse);
	}

}
