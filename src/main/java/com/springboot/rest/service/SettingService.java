package com.springboot.rest.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.AmountSettingRequestDto;
import com.springboot.rest.dto.LineSettingRequestDto;
import com.springboot.rest.dto.SettingResponseDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.dto.UserLineSettingDto;
import com.springboot.rest.logic.UserAmountLogic;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.LineSettingRepository;

@Service
public class SettingService {

	@Autowired
	private AmountSettingRepository settingRepository;

	@Autowired
	private LineSettingRepository repository;
	@Autowired
	private UserAmountLogic userAmountLogic;

	/**
	 * Inital process.
	 * 
	 * @return SettingResponseDto list.
	 */
	public SettingResponseDto init() throws Exception {
		CustomUserDetails user = getUserInfo();
		SettingResponseDto responseDto = new SettingResponseDto();
		List<UserAmountSettingDto> userSettingList = getAmountSettingByUseid(user.getId());
		List<UserLineSettingDto> lineList = getLineSettingByUserid(user.getId());

		if (userSettingList.size() > 2 || lineList.size() > 2) {
			//2レコード以上登録されていた場合はエラーを返す。
			throw new Exception();
		}

		if (!CollectionUtils.isEmpty(lineList)) {
			//ライン設定がある場合
			responseDto.setLineSetting(lineList.get(0));
		}

		if (CollectionUtils.isEmpty(userSettingList)) {
			//ユーザ設定がされていない場合、ライン設定のみ返却
			return responseDto;
		}
		//今月のユーザ設定がある場合、レスポンスに設定の上返却
		responseDto.setAmountSetting(userSettingList.get(0));

		return responseDto;

	}

	/**
	 * Regist fixed amount setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveAmountsetting(AmountSettingRequestDto requestDto) throws Exception {

		CustomUserDetails user = getUserInfo();

		int judge = getAmountSettingByUseid(user.getId()).size();

		if (judge < 1) {
			//登録されていなかったら登録処理
			registSetting(user.getId(), requestDto);
		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）
			throw new Exception();
		} else {

			System.out.println(updateSetting(user.getId(), requestDto));
		}

	}

	/**
	 * Regist line setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveBalanceSetting(UserAmountRequestDto requestDto) throws Exception {

		userAmountLogic.registUserAmount(requestDto);

	}

	/**
	 * Regist line setting.
	 * 
	 * @param requestDto
	 * @return HttpStatus list.
	 */
	public void saveLineSetting(LineSettingRequestDto requestDto) throws Exception {

		CustomUserDetails user = getUserInfo();

		int judge = getLineSettingByUserid(user.getId()).size();
		if (judge < 1) {
			//登録処理
			regist(user.getId(), requestDto);

		} else if (judge > 2) {
			//2レコード登録されていた場合はエラーを返す。（デッドコード）
			throw new Exception();
		} else {
			//すでに登録されていれば登録されていれば更新処理
			updateLineSetting(user.getId(), requestDto);
		}

	}

	/**
	 * Get user info.
	 * 
	 * @return CustomUserDetails.
	 */
	private CustomUserDetails getUserInfo() {
		return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	/**
	 * select setting.
	 * 
	 * @return
	 */
	public List<UserAmountSettingDto> getAmountSettingByUseid(long userId) {

		List<UserAmountSettingDto> responseDtoList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		Date date = new Date();

		settingRepository.findByUseidAndMonth(String.valueOf(userId), sdf.format(date).toString())
				.stream()
				.forEach(e -> {
					UserAmountSettingDto responseDto = new UserAmountSettingDto();
					responseDto.setId(e.getId());
					responseDto.setUserId(e.getUserId());
					responseDto.setMonthYear(e.getMonthYear());
					responseDto.setSaveAmount(e.getSaveAmount());
					responseDto.setFixedIncome(e.getFixedIncome());
					responseDto.setFixedExpenditure(e.getFixedExpenditure());
					responseDtoList.add(responseDto);
				});

		return responseDtoList;

	}

	/**
	 * regist setting.
	 * 
	 * @return
	 */
	public boolean registSetting(long userId, AmountSettingRequestDto requestDto) {

		//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		//		Date date = new Date();
		//		int result = settingRepository.insert(String.valueOf(userId), sdf.format(date).toString(),
		//				requestDto.getGoalAmount(), requestDto.getFixIncome(), requestDto.getFixExpenditure(),
		//				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());
		//
		//		if (result < 1) {
		//			return false;
		//		}

		return true;
	}

	/**
	 * update setting.
	 * 
	 * @return
	 */
	public boolean updateSetting(long userId, AmountSettingRequestDto requestDto) {

		//		int result = settingRepository.update(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7),
		//				requestDto.getGoalAmount(), requestDto.getFixIncome(), requestDto.getFixExpenditure(),
		//				CreateDate.getNowDateTime());
		//
		//		if (result < 1) {
		//			return false;
		//		}
		return true;
	}

	/**
	 * Get line info.
	 * 
	 * @param userId
	 * @return UserLineSettingDto list.
	 */
	public List<UserLineSettingDto> getLineSettingByUserid(long userId) {

		List<UserLineSettingDto> responseDtoList = new ArrayList<>();

		repository.findAllByUserid(String.valueOf(userId)).stream().forEach(e -> {
			UserLineSettingDto responseDto = new UserLineSettingDto();
			responseDto.setId(e.getId());
			responseDto.setUserId(e.getUserId());
			responseDto.setLineFlg(e.getLineFlg());
			responseDto.setAccessToken(e.getAccessToken());
			responseDtoList.add(responseDto);
		});
		if (responseDtoList.size() < 1) {
			return null;
		}

		return responseDtoList;

	}

	/**
	 * Regist line setting.
	 * 
	 * @param userId
	 * @param requestDto
	 * @return
	 */
	public boolean regist(long userId, LineSettingRequestDto requestDto) {

		repository.insert(String.valueOf(userId), requestDto.getLineFlg(), requestDto.getAccessToken(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		return false;

	}

	/**
	 * Update line setting.
	 * 
	 * @param userId
	 * @param requestDto
	 * @return
	 */
	public boolean updateLineSetting(long userId, LineSettingRequestDto requestDto) {

		repository.update(requestDto.getLineFlg(), requestDto.getAccessToken(), CreateDate.getNowDateTime(),
				String.valueOf(userId));

		return false;
	}
}
