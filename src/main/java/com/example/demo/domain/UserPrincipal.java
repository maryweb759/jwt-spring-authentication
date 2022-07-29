package com.example.demo.domain;


import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
    private User user;
   
    public UserPrincipal(User user) {
    	this.user = user;
    }
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//return Stream.concat(this.user.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//	    return Stream.concat(
//	    		Stream.of(this.user.getAuthorities()), 
//	    		Stream.of(this.user.getRoles())
//	    		).map(SimpleGrantedAuthority::new)
//	    .collect(Collectors.toList()); 
  return Stream.of(this.user.getAuthorities())
		  .map(SimpleGrantedAuthority::new)
		  .collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return this.user.isNotLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.user.isActive();
	}

	@Override
	public String toString() {
		return "UserPrincipal [user=" + user + "]";
	}

	
}
