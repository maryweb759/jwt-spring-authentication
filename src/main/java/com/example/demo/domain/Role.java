package com.example.demo.domain;

import static com.example.demo.constant.Authorities.*;

public enum Role {
	
	ROLE_USER(USER_AUTHORITIES),
	ROlE_HR(HR_AUTHORITIES),
    ROlE_MANAGER(MANAGER_AUTHORITIES),
    ROlE_ADMIN(ADMIN_AUTHORITIES),
    ROlE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);
	
	private String[] authorities; 
	
	Role(String... authorities) {
		this.authorities = authorities;
	}

	public String[] getAuthorities() {
		return authorities;
	} 
	
	
}
