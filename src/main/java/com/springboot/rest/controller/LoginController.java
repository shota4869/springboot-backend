package com.springboot.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.LoginResponseDto;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/login")
public class LoginController {

	/**
	 * Init process.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<LoginResponseDto> init() {

		LoginResponseDto dto = new LoginResponseDto();

		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		dto.setUserDetails(user);
		return ResponseEntity.ok(dto);

	}
}
