package com.example.demo.config;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.constant.SecurityConstants;
import com.example.demo.filter.JwtAccessDeniedFilter;
import com.example.demo.filter.JwtAuthonticationEntryPoint;
import com.example.demo.filter.JwtAuthorizationFilter;
@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(1)
// EnableGlobalMethodSecurity method level security 
// even if we are logged in this annotation check the authorities and roles we have before 
// executing the method , we add @PreAuthorize("hasRole('USER_VIEW')") on method level
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	 private  JwtAuthorizationFilter jwtAuthorizationFilter;
	 private  UserDetailsService userService; 
	 private JwtAccessDeniedFilter jwtAccessDeniedHandler;
	 private JwtAuthonticationEntryPoint  jwtAuthenticationEntryPoint;
	 private  PasswordEncoder passwordEncoder;
	 @Value("${app.public-urls}")
	    private String[] publicUrls;
	 
	 @Autowired
	 public SecurityConfiguration(@Lazy UserDetailsService userService,
	JwtAuthorizationFilter jwtAuthorizationFilter, JwtAccessDeniedFilter jwtAccessDeniedHandler,
	JwtAuthonticationEntryPoint  jwtAuthenticationEntryPoint,
	 PasswordEncoder passwordEncoder) {
		 this.userService = userService;
		 this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
		 this.jwtAuthorizationFilter = jwtAuthorizationFilter;
		 this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	     this.passwordEncoder = passwordEncoder;

	 } 
	 
	 @Bean()
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }
	 
	 @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth
      .userDetailsService(userService)
      .passwordEncoder(passwordEncoder);
		// auth.authenticationProvider(authenticationProvider());
	 } 
	 
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable().cors().and()
         .sessionManagement().sessionCreationPolicy(STATELESS)
         .and().authorizeRequests().antMatchers(publicUrls).permitAll()
         .anyRequest().authenticated()
         .and().
//		 http.csrf().disable();
//
//	        http.cors();
//
//	        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//	        http.authorizeRequests()
//	                .antMatchers(SecurityConstants.PUBLIC_URLS).permitAll()
//	                .anyRequest().authenticated();

	        exceptionHandling()
	                .accessDeniedHandler(jwtAccessDeniedHandler)
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

	        .and().addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
	    }
	 
	
//	 @Bean
//	 DaoAuthenticationProvider authenticationProvider(){
//	 DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider() ;
//	 daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//	 daoAuthenticationProvider.setUserDetailsService(userService);
//	 return daoAuthenticationProvider;
//	 }
//	 @Bean
//	 public  PasswordEncoder passwordEncoder(){
//		 String idForEncode = "bcrypt";
//		 Map<String, PasswordEncoder> encoders = new HashMap<>();
//		 encoders.put(idForEncode, new BCryptPasswordEncoder());
//		 encoders.put("noop", NoOpPasswordEncoder.getInstance());
//		 encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
//	     encoders.put("scrypt", new SCryptPasswordEncoder()); 
//	     return new DelegatingPasswordEncoder(idForEncode, encoders);
//	 }
	 
//	 @Bean
//	 public PasswordEncoder passwordEncoder() {
//	     return new BCryptPasswordEncoder(); // or any other password encoder
//	 }
	 
	 
	 
}
