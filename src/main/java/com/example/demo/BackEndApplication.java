package com.example.demo;
import java.io.File;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.example.demo.constant.FileConstant;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

@SpringBootApplication
public class BackEndApplication {
	@Autowired
    protected UserRepository userRepository;
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
    protected User user;
	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args); 
		// this will create a folder on the path specified
		new File(FileConstant.USER_FOLDER).mkdirs();
	
//		List<String> listData= new ArrayList<String>();
//		listData.add("name");
//		listData.add("after name");
//		String [] stringaa = listData.stream().toArray(String[]::new);
			//	System.out.println(stringaa); 
       
	} 
	@PostConstruct
	public void createUser() {
		User user = new User();
		user.setEmail("????##@example2.com");
		user.setFirstName("m2");
		user.setLastName("baks"); 
		user.setPassword("{noop}badpass");
		//user.setUserId(33);
		user.setActive(true);
		user.setNotLocked(true);
		user.setJoinDate(LocalDateTime.now());
		user.setLastLoginDate(LocalDateTime.now());
		user.setLastLoginDateDisplay(LocalDateTime.now()); 
		user.setRole("ROLE_Manager");
		user.setUserName("m3");
		user.setAuthorities(new String[] {"user:read", "user:add"}); 
		//System.out.println(user);
		//this.userRepository.save(user); 
//		User name = this.userRepository.findByuserName("m").get();
//		System.out.println("name" + name.toString());
	} 

}
