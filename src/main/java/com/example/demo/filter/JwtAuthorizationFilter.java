package com.example.demo.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import static com.example.demo.constant.SecurityConstants.*;
import com.example.demo.utils.JWTTokenProvider;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthorizationFilter  extends OncePerRequestFilter{

	private JWTTokenProvider jWTTokenProvider;
	
	public JwtAuthorizationFilter(JWTTokenProvider jWTTokenProvider) {
		this.jWTTokenProvider = jWTTokenProvider;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            String username = jWTTokenProvider.getSubject(token);
            if (jWTTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = jWTTokenProvider.getAuthories(token);
                Authentication authentication = jWTTokenProvider.getAuthontication(username, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
//		if(request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
//			String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//			if(authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
//				String token = authHeader.replace(TOKEN_PREFIX, "").trim(); 
//				String userName = jWTTokenProvider.getSubject(token); 
//				
//				if(jWTTokenProvider.isTokenValid(userName, token)) {
//					var authorities = jWTTokenProvider.getAuthories(token);
//					var authentication = jWTTokenProvider.getAuthontication(userName,authorities, request);
//					SecurityContextHolder.getContext().setAuthentication(authentication);
//				} else {
//					SecurityContextHolder.clearContext();
//				}
//			}
//		}
//	  filterChain.doFilter(request, response);
	}

