package com.springboot.rest.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.MCategoryEntity;
import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.CategoryDto;
import com.springboot.rest.dto.HomeInitResponseDto;
import com.springboot.rest.dto.UsableAmountDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.logic.CalculateBalanceLogic;
import com.springboot.rest.logic.UsableAmountLogic;
import com.springboot.rest.repository.AmountSettingRepository;
import com.springboot.rest.repository.MCategoryRepository;
import com.springboot.rest.repository.UserAmountRepository;

@Service
public class HomeService {

	@Autowired
	private AmountSettingRepository settingRepository;

	@Autowired
	private CalculateBalanceLogic calculateBalanceLogic;

	@Autowired
	private UsableAmountLogic usableAmountLogic;

	@Autowired
	private UserAmountRepository userAmountRepository;

	@Autowired
	private MCategoryRepository categoryRepository;

	/**
	 * Get amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int getSaveAmount() {

		CustomUserDetails user = getUserInfo();

		List<UserAmountSettingDto> responseDtoList = getAmountSettingByUseid(user.getId());

		if (CollectionUtils.isEmpty(responseDtoList)) {
			return 0;
		}

		return responseDtoList.get(0).getSaveAmount();

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
	 * 
	 * 
	 * @param userId
	 * @return
	 */
	public int saveAmountCalculete() {

		CustomUserDetails user = getUserInfo();

		int amount = 0;
		List<UsableAmountDto> dtoList = usableAmountLogic.findAll(user.getId(), CreateDate.getNowDate());

		if (!CollectionUtils.isEmpty(dtoList)) {
			amount = dtoList.get(0).getUsableAmount();

		}

		return amount + calculateBalanceLogic.balanceCalculete(user.getId());
	}

	/**
	 * Regist save amount.
	 * 
	 * @param userAmountDto
	 * @throws SQLException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void registUserAmount(UserAmountRequestDto userAmountDto) throws SQLException {

		int count = userAmountRepository.registUserAmount(userAmountDto.getUserId(),
				userAmountDto.getDate().substring(0, 7),
				userAmountDto.getDate(),
				String.format("%03d", Integer.valueOf(userAmountDto.getCategoryCode())),
				userAmountDto.getBalanceFlg(), userAmountDto.getAmount(), userAmountDto.getRemarks(),
				CreateDate.getNowDateTime(), CreateDate.getNowDateTime());

		if (count < 1) {
			new SQLException();
		}

	}

	/**
	 * 
	 * 
	 * @return
	 */
	public HomeInitResponseDto findCategory() {

		List<MCategoryEntity> targetList = categoryRepository.findAll();

		HomeInitResponseDto homeInitDto = new HomeInitResponseDto();

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

		System.out.println(incomeCategoryList.get(1).getCategoryCode());
		homeInitDto.setIncomeCategory(incomeCategoryList);
		homeInitDto.setExpenditureCategory(expenditureCategoryList);

		return homeInitDto;
	}

	/**
	 * Get usable amount.
	 * 
	 * @param userId
	 * @return usableAmount.
	 */
	public int findUsableAmount() throws SQLException {

		CustomUserDetails user = getUserInfo();

		List<UsableAmountDto> dtoList = new ArrayList<>();
		int usableAmount = 0;
		try {
			dtoList = usableAmountLogic.commonProcess(user.getId());
		} catch (SQLException e) {
			throw e;
		}

		if (!CollectionUtils.isEmpty(dtoList)) {
			usableAmount = dtoList.get(0).getUsableAmount();
		}

		int todayBlance = calculateBalanceLogic.balanceCalculete(user.getId());

		return usableAmount + todayBlance;
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
