package com.springboot.rest.dto;

import com.springboot.rest.auth.CustomUserDetails;

/**
 * Login response dto.
 * 
 * @author takaseshota
 */
public class LoginResponseDto {

	private CustomUserDetails userDetails;

	public CustomUserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(CustomUserDetails userDetails) {
		this.userDetails = userDetails;
	}

}
