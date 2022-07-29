package com.example.demo.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.Role;

public class UserDto {
	@NotEmpty(message = "Should not be empty")
	private String firstName;
	@NotEmpty(message ="Should not be empty")
    private String lastName;
    @NotEmpty(message = "Should not be empty")
    private String username;
    @NotEmpty(message = "Should not be empty")
    @Email(message = "Must match email format")
    private String email; 
    @NotEmpty(message = "Should not be empty")
    private String password;
   // @NotNull(message = "Role is mandatory")
    private Role role;
    private boolean isNonLocked;
    private boolean isActive;
    private MultipartFile profileImage;
    
    public UserDto() {}
    
		public UserDto(String firstName, String lastName, String username, String email,String password, Role role, boolean isNonLocked,
			boolean isActive, MultipartFile profileImage) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email; 
		this.password = password;
		this.role = role;
		this.isNonLocked = isNonLocked;
		this.isActive = isActive;
		this.profileImage = profileImage;
	}
    
		public Role getRole() {
			return role;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setRole(Role role) {
			this.role = role;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
		public boolean isNonLocked() {
		return isNonLocked;
	}
	public void setNonLocked(boolean isNonLocked) {
		this.isNonLocked = isNonLocked;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public MultipartFile getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}
    
    
}
