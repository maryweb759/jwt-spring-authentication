package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.User;
import com.example.demo.domain.dto.UserDto;

public interface UserService extends UserDetailsService {

	User register(String firstName,String lastName, String userName, String email); 
	
	Page<User> findAll(Pageable pageable);
	User findByUserName(String userName);
	
	User findByEmail(String email); 
	
	User addNewUser(UserDto userDto);

    User updateUser(String userName, UserDto userDto);

    void deleteUser(long id);

    void resetPassword(String email, String password);

    User updateProfileImage(String username, MultipartFile profileImage); 
    
    User findByUserId(UUID userId);
    byte[] getProfileImage(String username) throws IOException;

    byte[] getImageByUserid(UUID userId, String filename) throws IOException;

	byte[] getDefaultProfileImage(String userId);

}
