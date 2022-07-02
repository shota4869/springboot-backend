package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.BalanceListInitResponceDto;
import com.springboot.rest.dto.BalanceListRequestDto;
import com.springboot.rest.service.UserAmountService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/balance-list")
public class BalanceListController {

	@Autowired
	private UserAmountService userAmountService;

	@PostMapping
	public ResponseEntity<BalanceListInitResponceDto> init(@RequestBody BalanceListRequestDto balanceListRequestDto) {
		CustomUserDetails user = getUserInfo();

		System.out.println(balanceListRequestDto.getDate());
		return ResponseEntity.ok(userAmountService.findBalanceList(user.getId(), balanceListRequestDto.getDate()));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> deleteBalance(@PathVariable String id) {

		userAmountService.delete(id);

		return new ResponseEntity<HttpStatus>(HttpStatus.OK);

	}

	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
