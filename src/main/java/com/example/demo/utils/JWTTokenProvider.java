package com.example.demo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import static com.example.demo.constant.SecurityConstants.*;
import com.example.demo.domain.UserPrincipal;

import org.springframework.beans.factory.annotation.Value;
// to generate token 
	// 1 - 
@Service
//@Component
public class JWTTokenProvider {
   // private final JWTVerifier jwtVerifier;

		@Value("${jwt.secret}")
		private String secret;
		// 1 - generate the token 
		// this will take userPrincipal because once they log in i take their info and check 
		// if they exist on the database and once their credencil is correct i can create a 
		// userPricipal from spring
		public String generateToken(UserDetails  userDetails) {
			// take that userPrincipal and get all the (claims) mean autorities all the information 
			String[] claims = getClaimsFromuser(userDetails); 
			System.out.println("claims" + claims);
			return JWT.create()
					// name of the company
					.withIssuer(GET_ARRAYS_LLC)
					.withAudience(GET_ARRAYS_ADMINISTRATION)
					// date when token was created 
					.withIssuedAt(new Date())
					.withSubject(userDetails.getUsername())
					.withArrayClaim(AUTHORITIES, claims)
					.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(secret));
					//.sign(Algorithm.HMAC512(secret.getBytes()));
		}

		private String[] getClaimsFromuser(UserDetails  user) {
//			List<String> data = new ArrayList<>(); 
//			for(GrantedAuthority grandAuth: user.getAuthorities()) {
//				data.add(grandAuth.getAuthority());
//			} 
//			return data.toArray(new String[0]); 
			
			// return the authority and convert it to a array of String 
			System.out.println("user Authorities" + user.getAuthorities());

			return user.getAuthorities().stream() 
		     // it returns an object and when we getAuthority we get the String
					.map(GrantedAuthority::getAuthority)
					.toArray(String[]::new);
			
		} 
		
		// 2 - get authorites from the token
		// we need this method when the user try to access a link or page and we need to get the 
		// authorites from token  to know what authorites they have 
		public List<GrantedAuthority> getAuthories(String token) {
			
			// this method will get us all the claims from the token 
			String[] claims = getClaimsToken(token) ; 
			System.out.println("get claims " + claims);
			// we use stream to loop through a collection, 
			//and we map to get SimpleGrantedAuthority and collect to return a list
			return Arrays.stream(claims).map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
			
		}

		private String[] getClaimsToken(String token) {
			JWTVerifier verifier = getJwtVerifie(); 
			System.out.println("verifier -- " + verifier);
			try {  
				// to verifie the token we pass the verifier token and get the claims name we provided 
				// convert the response to Array of string 
				return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
			} catch (JWTVerificationException e) {
				throw new JWTVerificationException(ACCESS_DENIED_MESSAGE);
			} 
			
		}
       // this will return JWT verifier 
		private JWTVerifier getJwtVerifie() {
			JWTVerifier verifier;
	        Algorithm algorithm = Algorithm.HMAC512(secret);
			try {
				verifier= JWT.require(algorithm)
						.withIssuer(GET_ARRAYS_LLC)
						.build(); //Reusable verifier instance
			} catch (JWTVerificationException e) {
			 throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
			}
			return verifier;
		} 
		
		// - 3
		//get AUTHENTICATE of the user 
		// after we verifie that the token is correct we call spring to authentication of the user and
		// put it on the context
		public Authentication getAuthontication
		(String userName, List<GrantedAuthority> authorities, HttpServletRequest request) {
			UsernamePasswordAuthenticationToken userAuthontication = 
					new UsernamePasswordAuthenticationToken(userName,null, authorities); 
			// set info of user in spring context
			userAuthontication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			System.out.println("authontication -- " + userAuthontication);
			return userAuthontication ;
		} 
		
		// - 4 
		public boolean isTokenValid(String userName, String token) {
			JWTVerifier verifier = getJwtVerifie();
			return StringUtils.isNotEmpty(userName) && !isTokenExpired(verifier, token);
		}

		private boolean isTokenExpired(JWTVerifier verifier, String token) {
			Date exprition = verifier.verify(token).getExpiresAt();
			return exprition.before(new Date());
		}
		
		// - 5 
		public String getSubject(String token) {
			JWTVerifier verifier = getJwtVerifie();
            return verifier.verify(token).getSubject();
		}
		
	}

