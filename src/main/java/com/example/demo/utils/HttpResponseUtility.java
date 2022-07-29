package com.example.demo.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.domain.HttpResponse;

public class HttpResponseUtility {

	public static ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status, String message) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setHttpStatus(status);
		httpResponse.setHttpStatusCode(status.value());
		httpResponse.setReason(status.getReasonPhrase().toUpperCase());
		httpResponse.setMessage(message); 
		return new ResponseEntity<>(httpResponse, status);
    }
}
