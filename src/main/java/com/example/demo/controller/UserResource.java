package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import  com.example.demo.constant.SecurityConstants;
import com.example.demo.domain.HttpResponse;
import com.example.demo.domain.User;
import com.example.demo.domain.UserPrincipal;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTTokenProvider;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("user")
public class UserResource {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	private  UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    
    
	 @GetMapping("home")
	    public String showUser() {
	        return "Application works";
	    } 
	 
	 @PostMapping("register")
	 public User register(@RequestBody User user) {
	        return userService.register(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
	    } 
	 @PostMapping("add")
	    public User addNewUser(@RequestBody @Valid UserDto userDto) {
	        logger.debug("User DTO: {}", userDto);
	        return userService.addNewUser(userDto);
	    }
	 
	 @PutMapping("/{currentUsername}")
	    public User updateUser(@PathVariable String currentUsername,@RequestBody @Valid UserDto userDto) {
	        logger.debug("User DTO: {}", userDto);
	        return userService.updateUser(currentUsername, userDto);
	    }
	 @GetMapping("/{username}")
	    public User findUser(@PathVariable String username) {
	        return userService.findByUserName(username);
	    }
	 @GetMapping
	 public Page<User> getAllUsers(Pageable pageable) {
	        return userService.findAll(pageable); 
	    }
	  	 @PostMapping("/resetPassword/{email}")
	    public HttpResponse resetPassword(@PathVariable String email, @RequestBody String password) {
	        userService.resetPassword(email, password);
	        HttpResponse http =new  HttpResponse();
	        http.setHttpStatusCode(OK.value());
	        http.setHttpStatus(OK);        
	        http.setReason(OK.getReasonPhrase());
	        http.setMessage("Password reset successfully. Check your email for new password");
	           return http;     
	    }
	 @DeleteMapping("{id}")
	    public HttpResponse deleteUser(@PathVariable long id){
	        userService.deleteUser(id);
	        HttpResponse http =new  HttpResponse();
	        http.setHttpStatusCode(OK.value());
	        http.setHttpStatus(OK);        
	        http.setReason(OK.getReasonPhrase());
	        http.setMessage("User deleted successfully");
	           return http;  
	           	    }
	 
	 @PostMapping("login")
	    public ResponseEntity<HttpResponse>  login(@RequestBody User user) {
		  authenticate(user.getUserName(), user.getPassword());
		 UserDetails userDetails = userService.loadUserByUsername(user.getUserName());

         HttpResponse httpResponse = new HttpResponse();
		 httpResponse.setHttpStatus(OK);
		 httpResponse.setReason(OK.getReasonPhrase().toUpperCase());
		 httpResponse.setMessage("user logged in successfully");
		 httpResponse.setHttpStatusCode(OK.value()); 
		 return ResponseEntity.ok()
				 .header(SecurityConstants.JWT_TOKEN_HEADER, jwtTokenProvider.generateToken(userDetails))
				 .body(httpResponse);
		  	 
//			 User loginUser = userService.findByUserName(user.getUserName());
//			 
//	        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
//	        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
//	        return new ResponseEntity<>(loginUser, jwtHeader, OK);	
	 } 
	 
	 
	 private HttpHeaders getJwtHeader(UserPrincipal user) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(SecurityConstants.JWT_TOKEN_HEADER, jwtTokenProvider.generateToken(user));
	        return headers;
	    }
	 
	 @PutMapping("{username}/profileImage")
	    public User updateUserProfileImage(@PathVariable String username, MultipartFile profileImage) {
	        return userService.updateProfileImage(username, profileImage);
	    } 
	 @GetMapping(path = "{username}/image/profile", produces = MediaType.IMAGE_JPEG_VALUE)
	    public byte[] getProfileImage(@PathVariable String username) throws IOException {
	        byte[] profileImage = userService.getProfileImage(username);
	        logger.debug("File size: {}", profileImage.length);
	        return profileImage;
	    }
	 
	 // get image by userId and fileName
	    @GetMapping(path = "image/profile/{userId}/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	 public byte[] getProfileImageByUserId(@PathVariable UUID userId, @PathVariable String filename) throws IOException {
		 byte[] profileImage = userService.getImageByUserid(userId, filename);
		 return profileImage;
	 }
	    @GetMapping(path = "image/profile/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
	    public byte[] getDefaultProfileImage(@PathVariable String userId) {
	        byte[] profileImage = userService.getDefaultProfileImage(userId);
	        logger.debug("File size: {}", profileImage.length);
	        return profileImage;
	    }
	private  void authenticate(String userName, String password) throws AuthenticationException{
        UsernamePasswordAuthenticationToken authenticationToken = 
        		new UsernamePasswordAuthenticationToken(userName, password);
        authenticationManager.authenticate(authenticationToken);
     // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));

//		Authentication auth = new UsernamePasswordAuthenticationToken(userName, password);
//		authenticationManager.authenticate(auth);
	}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
