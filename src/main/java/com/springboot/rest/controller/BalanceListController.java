package com.springboot.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.dto.BalanceListInitResponseDto;
import com.springboot.rest.dto.BalanceListRequestDto;
import com.springboot.rest.service.BalanceListService;

/**
 * Balance list controller.
 * 
 * @author takaseshota
 */
@RestController
@RequestMapping(value = "/api/balance-list", method = { RequestMethod.GET, RequestMethod.PUT })
public class BalanceListController {

	@Autowired
	private BalanceListService balanceListService;

	/**
	 * Initialize.
	 * 
	 * @param balanceListRequestDto
	 * @return
	 */
	@PostMapping
	public ResponseEntity<BalanceListInitResponseDto> init(@RequestBody BalanceListRequestDto requestDto) {
		try {
			return ResponseEntity.ok(balanceListService.init(requestDto));

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}

	/**
	 * Delete record.
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> deleteBalance(@PathVariable String id) {
		try {
			balanceListService.delete(id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.CONFLICT);
		}
	}

	/**
	 * Update fix balance
	 * 
	 * @param fixFlg
	 * @return
	 */
	@PostMapping("/update")
	public ResponseEntity<HttpStatus> updateFixedBalance(@RequestBody String fixFlg) {
		try {
			balanceListService.calculateUsableAmount(fixFlg);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<HttpStatus>(HttpStatus.CONFLICT);
		}
	}

}
