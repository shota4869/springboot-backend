package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.CalenderInitResponseDto;
import com.springboot.rest.service.CalenderService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/home/calender")
public class CalenderController {

	@Autowired
	private CalenderService calenderService;

	@GetMapping
	public ResponseEntity<CalenderInitResponseDto> init() {

		CustomUserDetails user = getUserInfo();

		CalenderInitResponseDto responseDto = new CalenderInitResponseDto();
		responseDto.setCalenderDtoList(calenderService.getCallenderList(user.getId()));
		return ResponseEntity.ok(responseDto);

	}

	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
