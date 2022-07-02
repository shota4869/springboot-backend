package com.springboot.rest.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.service.AmountSettingService;
import com.springboot.rest.service.MCategoryService;
import com.springboot.rest.service.UsableAmountService;
import com.springboot.rest.service.UserAmountService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/home")
public class HomeController {

	@Autowired
	private MCategoryService mCategoryService;

	@Autowired
	private UserAmountService userAmountService;

	@Autowired
	private UsableAmountService usableAmountService;

	@Autowired
	private AmountSettingService amountSettingService;

	/**
	 * Initial process.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<HomeInitResponseDto> init() {

		HomeInitResponseDto dto = mCategoryService.findCategory();

		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		try {
			dto.setSaveAmount(amountSettingService.getSaveAmount(user.getId()));
			dto.setUsableAmount(usableAmountService.findUsableAmount(user.getId()));

			return ResponseEntity.ok(dto);
		} catch (RuntimeException e) {

			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
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
	public ResponseEntity<Integer> saveBalance(@RequestBody UserAmountRequestDto userAmountDto) {

		CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		try {
			userAmountService.registUserAmount(userAmountDto);

			return new ResponseEntity<Integer>(userAmountService.saveAmountCalculete(user.getId()), HttpStatus.OK);

		} catch (RuntimeException e) {
			return new ResponseEntity<Integer>(HttpStatus.CONFLICT);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			return new ResponseEntity<Integer>(HttpStatus.CONFLICT);
		}

	}

}
