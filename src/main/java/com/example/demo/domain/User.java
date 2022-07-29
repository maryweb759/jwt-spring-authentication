package com.example.demo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class User implements Serializable {

    private static final long serialVersionUID = -4372214856545239049L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(nullable=false, updatable=false)
    private Long id;
    private UUID userId;
    private String firstName; 
    private String lastName;
    private String userName;
    private String password;
    private String email; 
    private String profileImageUrl; 
    private LocalDateTime lastLoginDate; // this is gonna be the last time they loggin
    private LocalDateTime lastLoginDateDisplay; // this will show last time they login
    private LocalDateTime joinDate;
    // UserPrinciple prop from spring 
   // private String[] roles; // ROLE_USER {read}, ROLE_ADMIN {read, update, delete}
    private String role; //ROLE_USER, ROLE_ADMIN
    private String[] authorities;
    private boolean isActive; 
    private boolean isNotLocked;
    
    
    
    public User(Long id, UUID userId, String firstName, String lastName, String userName, String password,
			String email, String profileImageUrl, LocalDateTime lastLoginDate, LocalDateTime lastLoginDateDisplay,
			LocalDateTime joinDate, String role, String[] authorities, boolean isActive, boolean isNotLocked) {
		super();
		this.id = id;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.lastLoginDate = lastLoginDate;
		this.lastLoginDateDisplay = lastLoginDateDisplay;
		this.joinDate = joinDate;
		this.role = role;
		this.authorities = authorities;
		this.isActive = isActive;
		this.isNotLocked = isNotLocked;
	}

	public User() {}
     
		public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

		public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Type(type="org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar(255)", updatable = false, nullable = false )
	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	
	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public LocalDateTime getLastLoginDateDisplay() {
		return lastLoginDateDisplay;
	}

	public void setLastLoginDateDisplay(LocalDateTime lastLoginDateDisplay) {
		this.lastLoginDateDisplay = lastLoginDateDisplay;
	}

	public LocalDateTime getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDateTime joinDate) {
		this.joinDate = joinDate;
	}

//	public String[] getRoles() {
//		return roles;
//	}
//	public void setRoles(String[] roles) {
//		this.roles = roles;
//	}
	public String[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isNotLocked() {
		return isNotLocked;
	}
	public void setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", userName=" + userName + ", password=" + password + ", email=" + email + ", profileImageUrl="
				+ profileImageUrl + ", lastLoginDate=" + lastLoginDate + ", lastLoginDateDisplay="
				+ lastLoginDateDisplay + ", joinDate=" + joinDate + ", role=" + role + ", authorities="
				+ Arrays.toString(authorities) + ", isActive=" + isActive + ", isNotLocked=" + isNotLocked + "]";
	}

	
    
    
    

}
