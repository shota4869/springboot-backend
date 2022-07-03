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
import com.springboot.rest.service.BalanceListService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/balance-list")
public class BalanceListController {

	@Autowired
	private BalanceListService balanceListService;

	@PostMapping
	public ResponseEntity<BalanceListInitResponceDto> init(@RequestBody BalanceListRequestDto balanceListRequestDto) {
		CustomUserDetails user = getUserInfo();

		return ResponseEntity.ok(balanceListService.findBalanceList(user.getId(), balanceListRequestDto.getDate()));

	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> deleteBalance(@PathVariable String id) {
		try {
			balanceListService.delete(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.CONFLICT);
		}
	}

	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
