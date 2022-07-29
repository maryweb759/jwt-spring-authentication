package com.example.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.example.demo.domain.UserPrincipal;
import com.example.demo.service.LoginAttempService;

@Component
//public class BruteForceDetectionListeners implements  ApplicationListener<AuthenticationSuccessEvent> {
public class BruteForceDetectionListeners {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired 
    private LoginAttempService loginAttempService;
    
    @EventListener
    public void onLoginFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getPrincipal().toString();
        logger.debug("{} failed to login", username);
        loginAttempService.loginFailed(username);
        //Object principal = event.getAuthentication().getPrincipal();
//        if(principal instanceof String) {
//            String userName = (String) event.getAuthentication().getPrincipal();
//          logger.debug("{} failed to login", userName);
//
//            loginAttempService.loginFailed(userName);
//        }
    }

//	@Override
    @EventListener
	public void onLogginSuccess(AuthenticationSuccessEvent event) {
	  Object principal = event.getAuthentication().getPrincipal();
	  if (principal instanceof UserPrincipal) {
		  String userName = ((UserPrincipal) principal).getUsername();
		  logger.debug("{} logged in successfully" , userName);
		  loginAttempService.loginSuccessed(userName);
//		  UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
//		  logger.debug("{} logged in successfully" , user.getUsername());
//
//		  loginAttempService.loginSuccessed(user.getUsername());
	  }
		
	}

	
    
    
}
