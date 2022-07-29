package com.example.demo.common;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.domain.HttpResponse;
import com.example.demo.domain.User;
import com.example.demo.domain.UserPrincipal;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JWTTokenProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("local")
//public class BaseUserTest {
	
//	@Autowired
//    JWTTokenProvider jwtTokenProvider;
//	
//	@Autowired
//    TestRestTemplate restTemplate; 
//	
//	@Autowired
//    protected UserRepository userRepository;
//
//    protected User user; 
//    Logger logger = LoggerFactory.getLogger(BaseUserTest.class);
//
////    @Test
////    void showUserHome_forbidden() {
//
//        //when
//       // var responseEntity = restTemplate.getForEntity("/user/home", HttpResponse.class);
//        //then
////        logger.debug("Response Entity: {}", responseEntity);
////        assertThat(responseEntity.getStatusCode()).isEqualTo(FORBIDDEN);
////        assertThat(responseEntity.getBody())
////                .isNotNull()
////                .hasNoNullFieldsOrProperties()
////                .satisfies(httpResponse -> assertAll(
////                        () -> assertThat(httpResponse.getHttpStatusCode()).isEqualTo(403),
////                        () -> assertThat(httpResponse.getHttpStatus()).isEqualTo(FORBIDDEN),
////                        () -> assertThat(httpResponse.getReason()).isEqualTo("FORBIDDEN"),
////                        () -> assertThat(httpResponse.getMessage()).isEqualTo("You need to log in to access this page")
////                ));
//   // }    
//    @Test
//    void showUserHome_correctToken() {
//    	User user = new User();
//		user.setEmail("????##@example4.com");
//		user.setFirstName("m4");
//		user.setLastName("baks"); 
//		user.setPassword("{noop}badpass");
//		user.setUserId("22");
//		user.setActive(true);
//		user.setNotLocked(true);
//		user.setJoinDate(LocalDateTime.now());
//		user.setLastLoginDate(LocalDateTime.now());
//		user.setLastLoginDateDisplay(LocalDateTime.now()); 
//		//user.setRoles(new String[] {"ROLE_Manager", "ROLE_USER"});
//		user.setUserName("m4");
//		user.setAuthorities(new String[] {"user:read", "user:add"}); 
//        //given
//        user = userRepository.save(user);
//        String validToken = jwtTokenProvider.generateToken(new UserPrincipal(user));
//        logger.debug("JWT Token: `{}`", validToken);
//
//        //when
//        RequestEntity<?> requestEntity = RequestEntity
//                .get("/user/home")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
//                .build();
//        var responseEntity = restTemplate.exchange(requestEntity, String.class);
//
//        //then
//        logger.debug("Response Entity: {}", responseEntity);
//        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
//        assertThat(responseEntity.getBody())
//                .isNotNull()
//                .isEqualTo("Application works");
//    }
//	@Test
//	public void createUser() {
//		User user = new User();
//		user.setEmail("????##@example2.com");
//		user.setFirstName("m2");
//		user.setLastName("baks"); 
//		user.setPassword("{noop}badpass");
//		user.setUserId("22");
//		user.setActive(true);
//		user.setNotLocked(true);
//		user.setJoinDate(LocalDateTime.now());
//		user.setLastLoginDate(LocalDateTime.now());
//		user.setLastLoginDateDisplay(LocalDateTime.now()); 
//		//user.setRoles(new String[] {"ROLE_Manager", "ROLE_USER"});
//		user.setUserName("m3");
//		user.setAuthorities(new String[] {"user:read", "user:add"}); 
//		System.out.println(user);
//		//this.userRepository.save(user); 
////		User name = this.userRepository.findByuserName("m").get();
////		System.out.println("name" + name.toString());
//	} 
//
//}
