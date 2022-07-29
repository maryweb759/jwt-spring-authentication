package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.demo.constant.UserImpConstant.*;

import com.example.demo.constant.FileConstant;
import com.example.demo.domain.EmailExistsException;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.domain.UserNameExistsException;
import com.example.demo.domain.UserNotFoundException;
import com.example.demo.domain.UserPrincipal;
import com.example.demo.domain.dto.UserDto;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {
    private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired 
	private UserRepository userR;
	@Autowired    
	private PasswordEncoder passwordEncoder; 
	@Autowired 
	private LoginAttempService loginAttempService;
	@Autowired 
	 private MailService emailService;
	
	@Autowired 
    private  RestTemplateBuilder restTemplateBuilder;
	@Autowired 
	RestTemplate restTemplate ;
	@PostConstruct
    void init() {
		 restTemplate 
				= restTemplateBuilder.
				rootUri(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL)
				.build();
	}
	
		@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userR.findByUserName(userName);
		if (user != null) {		
		validateLoginAttemps(user);
		user.setLastLoginDateDisplay(user.getLastLoginDate());
		user.setLastLoginDate(LocalDateTime.now()); 
		userR.save(user); 
		return new UserPrincipal(user);
		}
		 LOGGER.error(NO_USER_FOUND_BY_USERNAME + userName);
         throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + userName);
    
	}
// if the user tries to log in 5 times in 5 mnts set setNotLocked(false) else clear cache
	private void validateLoginAttemps(User user) {
			if(user.isNotLocked()) {
				if(loginAttempService.hasExceededMaxAttempts(user.getUserName())) {
					user.setNotLocked(false);
				} else {
					 user.setNotLocked(true);
				} 
			} else {
				loginAttempService.evictCache(user.getUserName());
			}
			
		}

	@Override
	public User register(String firstName, String lastName, String userName, String email) {
		validateNewUsernameAndEmail(null,userName, email);
		Role defaultRole = Role.ROLE_USER;
        String rawPassword = generatePassword(); 
        System.out.println(rawPassword);
        String encodedPass = passwordEncoder.encode("ma");
        
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        user.setEmail(email); 
        user.setPassword(encodedPass);
        user.setProfileImageUrl(getTemporaryProfileImageUrl(userName));
        user.setJoinDate(LocalDateTime.now());
        user.setActive(true);
        user.setNotLocked(true);
        user.setLastLoginDate(null);
        user.setLastLoginDateDisplay(null);
        user.setRole(defaultRole.name());
        user.setAuthorities(defaultRole.getAuthorities());
        userR.save(user);
        
        try {
		 emailService.sendNewEmailPassword(user.getFirstName(),user.getPassword(), user.getEmail());
		System.out.println("email sent");
        } catch (Exception e) {
			LOGGER.error("cant send email", e.getMessage());
		}
        
		return user;
	}


	private String generatePassword() {
		return RandomStringUtils.randomAscii(10);
	}
	private UUID generateUserId() {
        return UUID.randomUUID();
    }
	
	 private String getTemporaryProfileImageUrl(String userId) {
		 return ServletUriComponentsBuilder.fromCurrentContextPath()
//				 .path(FileConstant.DEFAULT_USER_IMAGE_PATH)
//                 .pathSegment(username)
				 .path(FileConstant.DEFAULT_USER_IMAGE_PATH)
				 .pathSegment(userId)
				 .pathSegment(FileConstant.USER_IMAGE_FILENAME)
                 .toUriString();
	 }

	@Override
	public Page<User> findAll(Pageable pageable) {
        return userR.findAll(pageable);
    }

	@Override
	public User findByUserName(String userName) {
		return userR.findByUserName(userName);
		}

	@Override
	public User findByEmail(String email) {
		return userR.findByEmail(email);
	} 
	
	private User validateNewUsernameAndEmail(String currentUsername, String userName, String email) throws UserNotFoundException {
        User userByNewUsername = findByUserName(userName);
        User userByNewEmail = findByEmail(email);
        if(StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findByUserName(currentUsername);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UserNameExistsException(USERNAME_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {     	 
    		if(userByNewUsername != null) {
			//throwEmailExistsException(USERNAME_ALREADY_EXISTS);
			throwUsernameExistsException(userName);
		} 
		if(userByNewEmail != null) {
			//throwUsernameExistsException(EMAIL_ALREADY_EXISTS);
			throwEmailExistsException(email);
		}
            return null;
        }
	} 
	
//    private User validateUpdateUsernameAndEmail(String currentUsername, String username, String email) {
//
//        Objects.requireNonNull(currentUsername);
//
//        User currentUser = findByUserName(currentUsername);
//
//        if (!Objects.equals(currentUsername, username) && userR.existsByUsername(username))
//            throwUsernameExistsException(username);
//
//        if (!Objects.equals(currentUser.getEmail(), email) && userR.existsByEmail(email))
//            throwEmailExistsException(email);
//
//        return currentUser;
//    }
//	
	private void throwEmailExistsException(String email) {
        throw new UserNameExistsException(String.format(USERNAME_ALREADY_EXISTS, email));
    
	}

    private void throwUsernameExistsException(String username) {
        throw new UserNameExistsException(String.format(EMAIL_ALREADY_EXISTS, username));
    }
	@Override
	public User addNewUser(UserDto userDto) {
		String userName = userDto.getUsername();
		String email = userDto.getEmail();
		validateNewUsernameAndEmail(null,userName, email);
		
		User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUsername());
        user.setEmail(email);
        user.setJoinDate(LocalDateTime.now());
       // user.setPassword(encodePassword(password));
        user.setPassword(encodePassword(userDto.getPassword()));
       // user.setProfileImageUrl(getTemporaryProfileImageUrl(user.getUserId().toString()));
        user.setProfileImageUrl(generateDefaultProfileImageUrl(user.getUserId().toString()));
        user.setActive(true);
        user.setNotLocked(true);
        user.setLastLoginDate(LocalDateTime.now());
        user.setLastLoginDateDisplay(null);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        LOGGER.info("New user password: " + password);
        userR.save(user); 
        saveProfileImage(user, userDto.getProfileImage());
		return user;
	}
	private void saveProfileImage(User user, MultipartFile profileImage) {
		if(profileImage == null) return;
		Path userFolder = Paths.get(FileConstant.USER_FOLDER, user.getUserId().toString());
        try {
            if (Files.notExists(userFolder)) {
                Files.createDirectories(userFolder);
                LOGGER.debug(FileConstant.DIRECTORY_CREATED);
            }
            // transfer the received file to  the given destination file
            profileImage.transferTo(userFolder.resolve(FileConstant.USER_IMAGE_FILENAME));
     // userFolder.resolve -> takes the current path and add the value we specifie to it 
            LOGGER.debug(FileConstant.FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
            user.setProfileImageUrl(generateProfileImageUrl(user.getUserId().toString()));
            userR.save(user);
        } catch (IOException exception) {
        	LOGGER.error("Can't save to file", exception);
        }
		
	}
	@Override
	public User updateUser(String userName, UserDto userDto) {
		String newUsername = userDto.getUsername();
		String email = userDto.getEmail();
		
		User user = validateUpdateUsernameAndEmail(userName, newUsername, email);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUserName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole().name());
        user.setAuthorities(userDto.getRole().getAuthorities());
        user.setNotLocked(userDto.isNonLocked());
        user.setActive(userDto.isActive()); 
        userR.save(user); 
        saveProfileImage(user, userDto.getProfileImage());

		return user;
	}
	private User validateUpdateUsernameAndEmail(String userName, String newUsername, String email) {
       Objects.requireNonNull(userName);
       User currentUser = findByUserName(userName); 
       
       if(!Objects.equals(currentUser.getUserName(), newUsername)  && findByUserName(newUsername) == null) {
           throwUsernameExistsException(newUsername);
       } 
       if(!Objects.equals(currentUser.getEmail(), email)  && findByEmail(email) == null) {
           throwEmailExistsException(email);;
       }
		return currentUser;
	}
	
	@Override
	public void deleteUser(long id) {
		userR.deleteById(id);
		
	}
	@Override
	public void resetPassword(String email, String password) {
		 User user = findByEmail(email);
		 String encodePass = encodePassword(password);
		 user.setPassword(encodePass);
		 userR.save(user); 
		 try {
	            emailService.sendNewEmailPassword(user.getFirstName(), encodePass, user.getEmail());
	        } catch (Exception exception) {
	            LOGGER.debug("Can't send message. Error: {} ", exception.getMessage());
	        }
		
	}
	@Override
	public User updateProfileImage(String username, MultipartFile profileImage) {
		 User user = findByUserName(username);
	        saveProfileImage(user, profileImage);
	        return user;
	}

	private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
	@Override
	public User findByUserId(UUID userId) {
		return userR.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERID));
		
	} 
	private String generateProfileImageUrl(String userId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH)
                .pathSegment(userId)
                .pathSegment(FileConstant.USER_IMAGE_FILENAME)
                .toUriString();
    }
	@Override
	public byte[] getProfileImage(String username) throws IOException {
		 User user = findByUserName(username);
//	        Path userFolder = Paths.get(FileConstant.USER_FOLDER, user.getUserId().toString(), FileConstant.USER_IMAGE_FILENAME);
//	        return Files.readAllBytes(userFolder);
	        return getImageByUserid(user.getUserId(), FileConstant.USER_IMAGE_FILENAME);

	}
	@Override
	public byte[] getImageByUserid(UUID userId, String filename) throws IOException {
		 Path userProfileImagePath = Paths
	                .get(FileConstant.USER_FOLDER, userId.toString(), filename);
	        return Files.readAllBytes(userProfileImagePath);
	}
	@Override
	public byte[] getDefaultProfileImage(String userId) {
		
		  //  "https://robohash.org/11951691-d373-4126-bef2-84d157a6546b"
        RequestEntity<Void> requestEntity = RequestEntity
                .get("/{userId}", userId)
                .accept(MediaType.IMAGE_JPEG)
                .build();
        var responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<byte[]>() {
        });
        return responseEntity.getBody();
	} 
	private String generateDefaultProfileImageUrl(String userId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH)
                .pathSegment(userId)
                .toUriString();
    }
}
