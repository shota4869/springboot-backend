package com.springboot.rest.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.AmountSettingRequestDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.repository.AmountSettingRepository;

@Service
public class AmountSettingService {

	@Autowired
	private AmountSettingRepository settingRepository;

	/**
	 * select setting.
	 * 
	 * @return
	 */
	public List<UserAmountSettingDto> findByUserid(long userId) {

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

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		Date date = new Date();
		int result = settingRepository.insert(String.valueOf(userId), sdf.format(date).toString(),
				requestDto.getGoalAmount(), requestDto.getFixIncome(), requestDto.getFixExpenditure(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		if (result < 1) {
			return false;
		}

		return true;
	}

	/**
	 * update setting.
	 * 
	 * @return
	 */
	public boolean updateSetting(long userId, AmountSettingRequestDto requestDto) {

		int result = settingRepository.update(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7),
				requestDto.getGoalAmount(), requestDto.getFixIncome(), requestDto.getFixExpenditure(),
				CreateDate.getNowDateTime());

		if (result < 1) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @param userId
	 * @return
	 */
	public int getSaveAmount(long userId) {

		List<UserAmountSettingDto> responseDtoList = findByUserid(userId);

		if (CollectionUtils.isEmpty(responseDtoList)) {
			return 0;
		}

		return responseDtoList.get(0).getSaveAmount();

	}

}
