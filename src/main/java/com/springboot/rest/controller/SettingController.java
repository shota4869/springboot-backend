package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.AmountSettingRequestDto;
import com.springboot.rest.dto.LineSettingRequestDto;
import com.springboot.rest.dto.SettingResponseDto;
import com.springboot.rest.dto.SettingSaveResponceDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.service.SettingService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/setting")
public class SettingController {

	@Autowired
	private SettingService settingService;

	/**
	 * Inital process.
	 * 
	 * @return SettingResponseDto list.
	 */
	@GetMapping
	public ResponseEntity<SettingResponseDto> init() {
		try {
			SettingResponseDto responseDto = settingService.init();
			return ResponseEntity.ok(responseDto);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Regist save amount setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	@PostMapping("/amount")
	public ResponseEntity<HttpStatus> saveAmountsetting(@RequestBody AmountSettingRequestDto requestDto) {
		try {
			settingService.saveAmountSetting(requestDto);
			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Regist fixed balance setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	@PostMapping("/balance")
	public ResponseEntity<SettingSaveResponceDto> saveBalanceSetting(@RequestBody UserAmountRequestDto requestDto) {
		try {
			settingService.saveBalanceSetting(requestDto);
			return new ResponseEntity<SettingSaveResponceDto>(settingService.getFixBalance(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Regist line setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	@PostMapping("/line")
	public ResponseEntity<HttpStatus> saveLineSetting(@RequestBody LineSettingRequestDto requestDto) {
		try {
			settingService.saveLineSetting(requestDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Regist line setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	@PostMapping("/test-connecting")
	public ResponseEntity<HttpStatus> testConnecting(@RequestBody LineSettingRequestDto requestDto) {
		try {
			settingService.testConnecting(requestDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
