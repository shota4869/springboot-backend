package com.springboot.rest.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.LoginResponseDto;

/**
 * Login service.
 * 
 * @author takaseshota
 */
@Service
public class LoginService {

	/**
	 * Initialize.
	 * 
	 * @return
	 */
	public LoginResponseDto init() {
		LoginResponseDto dto = new LoginResponseDto();

		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		dto.setUserDetails(user);

		return dto;
	}
}
