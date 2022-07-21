package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.CalenderInitResponseDto;
import com.springboot.rest.service.CalenderService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/home/calender")
public class CalenderController {

	@Autowired
	private CalenderService calenderService;

	/**
	 * Init process.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<CalenderInitResponseDto> init() {
		try {
			return ResponseEntity.ok(calenderService.init());
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

}
