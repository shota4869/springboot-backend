package com.springboot.rest.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.UserDto;
import com.springboot.rest.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Regist user.
	 * 
	 * @param user
	 * @return HttpStatus list.
	 */
	@PostMapping("/regist-user")
	public ResponseEntity<HttpStatus> registUser(@RequestBody UserDto user) {

		try {
			userService.registUser(user.getUsername(), user.getEmail(), user.getPassword());
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (SQLException e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.CONFLICT);
		}

	}

}
