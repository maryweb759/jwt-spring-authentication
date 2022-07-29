package com.example.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.example.demo.service.LoginAttempService;

//@Component
//public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired 
//    private LoginAttempService loginAttempService;
//    
//	@Override
//	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//		// get username from event principal 
//		String userName = event.getAuthentication().getPrincipal().toString();
//		logger.debug("{} failed to login ", userName);
//		loginAttempService.loginFailed(userName);
//		
//	}
//
//}
