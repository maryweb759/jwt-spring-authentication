package com.example.demo.filter;

import java.io.IOException;
import java.io.OutputStream;

import com.example.demo.constant.SecurityConstants;
import com.example.demo.domain.HttpResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.fasterxml.jackson.databind.ObjectMapper;
// we call this class for authontication entry point  if the authentication is forbiden 
@Component
public class JwtAuthonticationEntryPoint extends Http403ForbiddenEntryPoint {
      private  ObjectMapper  objectMapper; 
      
      public JwtAuthonticationEntryPoint(ObjectMapper  objectMapper) {
    	  this.objectMapper = objectMapper;
      } 
      
      @Override
      public void commence(HttpServletRequest request, HttpServletResponse response, 
    		  AuthenticationException exception) throws IOException {
    	  var httpResponse = new HttpResponse();
    	  httpResponse.setHttpStatus(FORBIDDEN); 
    	  httpResponse.setMessage(SecurityConstants.FORBIDDEN_MESSAGE);
    	  httpResponse.setReason(FORBIDDEN.getReasonPhrase().toUpperCase());
    	  httpResponse.setHttpStatusCode(FORBIDDEN.value());
    	 // String JsonString = objectMapper.writeValueAsString(httpResponse);
    	  
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
         // response.sendError(403, JsonString);
    	 OutputStream outputStream = response.getOutputStream(); 
    	 objectMapper.writeValue(outputStream, httpResponse);
      }
      }
      

