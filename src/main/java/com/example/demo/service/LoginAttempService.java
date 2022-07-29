package com.example.demo.service;

public interface LoginAttempService {

	 int MAX_ATTEMPTS = 5;
	 int ATTEMPT_INCREMENT = 1;
    
    void loginFailed(String userName);
    
    void loginSuccessed(String userName); 
    
    boolean hasExceededMaxAttempts(String username); 
    
    void evictCache(String userName);

}
