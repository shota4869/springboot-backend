package com.springboot.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.AmountSettingRequestDto;
import com.springboot.rest.dto.LineSettingRequestDto;
import com.springboot.rest.dto.SettingResponseDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.dto.UserLineSettingDto;
import com.springboot.rest.service.AmountSettingService;
import com.springboot.rest.service.LineSettingService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/setting")
public class SettingController {

	@Autowired
	private AmountSettingService userSettingService;

	@Autowired
	private LineSettingService lineSettingService;

	@GetMapping
	public ResponseEntity<SettingResponseDto> init() {
		CustomUserDetails user = getUserInfo();
		SettingResponseDto responseDto = new SettingResponseDto();
		List<UserAmountSettingDto> responseDtoList = userSettingService.findByUserid(user.getId());
		List<UserLineSettingDto> lineList = lineSettingService.findByUserid(user.getId());

		responseDto.setLineSetting(lineList.get(0));

		if (CollectionUtils.isEmpty(responseDtoList)) {
			return ResponseEntity.ok(responseDto);
		}
		System.out.println(responseDtoList.size());
		if (responseDtoList.size() > 2 || lineList.size() > 2) {
			//2レコード登録されていた場合はエラーを返す。
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		responseDto.setAmountSetting(responseDtoList.get(0));

		return ResponseEntity.ok(responseDto);

	}

	@PostMapping("/amount")
	public ResponseEntity<HttpStatus> saveAmountsetting(@RequestBody AmountSettingRequestDto requestDto) {

		CustomUserDetails user = getUserInfo();
		System.out.println("hozi");

		int judge = userSettingService.findByUserid(user.getId()).size();

		if (judge < 1) {
			//登録されていなかったら登録処理
			userSettingService.registSetting(user.getId(), requestDto);
		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			//登録されていれば更新処理
			System.out.println(requestDto.getFixExpenditure());

			System.out.println(userSettingService.updateSetting(user.getId(), requestDto));
		}

		return new ResponseEntity<HttpStatus>(HttpStatus.OK);

	}

	@PostMapping("/line")
	public ResponseEntity<HttpStatus> saveLineSetting(@RequestBody LineSettingRequestDto requestDto) {

		CustomUserDetails user = getUserInfo();
		//nullチェック

		System.out.println("hozi");
		int judge = lineSettingService.findByUserid(user.getId()).size();
		if (judge < 1) {
			//登録処理
			lineSettingService.regist(user.getId(), requestDto);

		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）

		} else {
			//すでに登録されていれば登録されていれば更新処理
			System.out.println("hozi1");
			lineSettingService.update(user.getId(), requestDto);
		}

		return new ResponseEntity<HttpStatus>(HttpStatus.OK);

	}

	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
