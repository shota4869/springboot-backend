package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.LoginResponseDto;
import com.springboot.rest.service.LoginService;

/**
 * Login controller.
 * 
 * @author takaseshota
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * Initialize.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<LoginResponseDto> init() {

		try {
			return ResponseEntity.ok(loginService.init());
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}
}
