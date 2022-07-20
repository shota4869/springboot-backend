package com.springboot.rest.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.dto.HomeSaveResponceDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.service.HomeService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	/**
	 * Initial process.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<HomeInitResponseDto> init() {

		try {
			return ResponseEntity.ok(homeService.init());
		} catch (RuntimeException e) {

			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	/**
	 * Save balance amount.
	 * 
	 * @param userAmountDto
	 * @return
	 */
	@PostMapping("/save")
	public ResponseEntity<HomeSaveResponceDto> saveBalance(@RequestBody UserAmountRequestDto userAmountDto) {
		try {
			homeService.registUserAmount(userAmountDto);
			return new ResponseEntity<HomeSaveResponceDto>(homeService.saveAmountCalculete(), HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<HomeSaveResponceDto>(HttpStatus.CONFLICT);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			return new ResponseEntity<HomeSaveResponceDto>(HttpStatus.CONFLICT);
		}
	}
}
