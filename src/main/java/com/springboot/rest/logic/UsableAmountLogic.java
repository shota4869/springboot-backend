package com.springboot.rest.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.common.CreateDate;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.repository.AmountSettingRepository;

/**
 * Usable amount logic.
 * 
 * @author takaseshota
 */
@Component
public class UsableAmountLogic {

	@Autowired
	private AmountSettingRepository amountSettingRepository;

	/**
	 * select setting.
	 * 
	 * @return
	 */
	public List<UserAmountSettingDto> getAmountSettingByUseid(long userId) {

		List<UserAmountSettingDto> responseDtoList = new ArrayList<>();

		amountSettingRepository.findByUseidAndMonth(String.valueOf(userId), CreateDate.getMonth())
				.stream()
				.forEach(e -> {
					UserAmountSettingDto responseDto = new UserAmountSettingDto();
					responseDto.setId(e.getId());
					responseDto.setUserId(e.getUserId());
					responseDto.setMonthYear(e.getMonthYear());
					responseDto.setSaveAmount(e.getSaveAmount());
					responseDtoList.add(responseDto);
				});

		return responseDtoList;

	}

	/**
	 * Update setting
	 * 
	 * @param goalAmount
	 * @param usableAmount
	 */
	public void updateSetting(int goalAmount, int usableAmount) throws Exception {

		CustomUserDetails user = getUserInfo();

		int result = amountSettingRepository.update(String.valueOf(user.getId()), CreateDate.getMonth(),
				String.valueOf(usableAmount), String.valueOf(goalAmount), CreateDate.getNowDateTime());

		if (result < 1) {
			new Exception();
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
}
