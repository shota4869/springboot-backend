package com.springboot.rest.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.MCategoryEntity;
import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.CategoryDto;
import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.dto.HomeSaveResponceDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.logic.CalculateBalanceLogic;
import com.springboot.rest.logic.UserAmountLogic;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.MCategoryRepository;

@Service
public class HomeService {

	@Autowired
	private AmountSettingRepository settingRepository;

	@Autowired
	private CalculateBalanceLogic calculateBalanceLogic;

	@Autowired
	private UserAmountLogic userAmountLogic;

	@Autowired
	private MCategoryRepository categoryRepository;

	public HomeInitResponseDto init() {
		CustomUserDetails user = getUserInfo();
		HomeInitResponseDto homeInitDto = new HomeInitResponseDto();

		//カテゴリー取得
		List<MCategoryEntity> targetList = findCategory();
		List<CategoryDto> incomeCategoryList = new ArrayList<CategoryDto>();
		List<CategoryDto> expenditureCategoryList = new ArrayList<CategoryDto>();
		targetList.stream().filter(e -> "0".equals(e.getBalanceFlg()))
				.forEach(item -> {
					CategoryDto categoryDto = new CategoryDto();
					categoryDto.setId(item.getId());
					categoryDto.setCategoryCode(item.getCategoryCode());
					categoryDto.setCategoryName(item.getCategoryName());
					categoryDto.setBalanceFlg(item.getBalanceFlg());
					incomeCategoryList.add(categoryDto);
				});

		targetList.stream().filter(e -> "1".equals(e.getBalanceFlg()))
				.forEach(item -> {
					CategoryDto categoryDto = new CategoryDto();
					categoryDto.setId(item.getId());
					categoryDto.setCategoryCode(item.getCategoryCode());
					categoryDto.setCategoryName(item.getCategoryName());
					categoryDto.setBalanceFlg(item.getBalanceFlg());
					expenditureCategoryList.add(categoryDto);
				});

		homeInitDto.setIncomeCategory(incomeCategoryList);
		homeInitDto.setExpenditureCategory(expenditureCategoryList);

		//貯金額と1日に使える金額設定
		List<UserAmountSettingDto> responseDtoList = getAmountSettingByUseid(user.getId());

		homeInitDto.setSaveAmount(0);
		homeInitDto.setUsableAmount(0);
		if (!CollectionUtils.isEmpty(responseDtoList)) {
			homeInitDto.setSaveAmount(responseDtoList.get(0).getSaveAmount());
			homeInitDto.setUsableAmount(responseDtoList.get(0).getUsableAmount());
		}

		//本日の収支
		homeInitDto.setBalanceAmount(calculateBalanceLogic.balanceCalculete(user.getId()));
		//残り使える金額
		homeInitDto.setRestUsableAmount(
				responseDtoList.get(0).getUsableAmount() + calculateBalanceLogic.balanceCalculete(user.getId()));

		return homeInitDto;
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
					responseDto.setUsableAmount(e.getUsableAmount());
					responseDtoList.add(responseDto);
				});

		return responseDtoList;

	}

	/**
	 * 
	 * 
	 * @param userId
	 * @return
	 */
	public HomeSaveResponceDto saveAmountCalculete() {

		HomeSaveResponceDto responseDto = new HomeSaveResponceDto();
		CustomUserDetails user = getUserInfo();

		int amount = 0;
		List<UserAmountSettingDto> dtoList = getAmountSettingByUseid(user.getId());

		if (!CollectionUtils.isEmpty(dtoList)) {
			amount = dtoList.get(0).getUsableAmount();

		}
		responseDto.setRestAmount(amount + calculateBalanceLogic.balanceCalculete(user.getId()));
		responseDto.setBalanceAmount(calculateBalanceLogic.balanceCalculete(user.getId()));
		return responseDto;
	}

	/**
	 * Regist save amount.
	 * 
	 * @param userAmountDto
	 * @throws SQLException
	 */
	public void registUserAmount(UserAmountRequestDto userAmountDto) throws SQLException {

		userAmountLogic.registUserAmount(userAmountDto);
	}

	/**
	 * Get category list.
	 * 
	 * @return
	 */
	public List<MCategoryEntity> findCategory() {

		return categoryRepository.findAll();
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
