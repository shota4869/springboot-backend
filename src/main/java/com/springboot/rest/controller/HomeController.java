package com.springboot.rest.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.dto.HomeSaveResponseDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.service.HomeService;

/**
 * Home controller
 * 
 * @author takaseshota
 */
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
	public ResponseEntity<HomeSaveResponseDto> saveBalance(@RequestBody UserAmountRequestDto userAmountDto) {
		try {
			homeService.registUserAmount(userAmountDto);
			return new ResponseEntity<HomeSaveResponseDto>(homeService.calculateSaveAmount(), HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<HomeSaveResponseDto>(HttpStatus.CONFLICT);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			return new ResponseEntity<HomeSaveResponseDto>(HttpStatus.CONFLICT);
		}
	}
}
