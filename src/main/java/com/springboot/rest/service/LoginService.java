package com.springboot.rest.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.LoginResponseDto;

@Service
public class LoginService {

	public LoginResponseDto init() {
		LoginResponseDto dto = new LoginResponseDto();

		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		dto.setUserDetails(user);

		return dto;

	}
}
