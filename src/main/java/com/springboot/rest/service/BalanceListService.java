package com.springboot.rest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.UserAmountAndMCategoryJoinEntity;
import com.springboot.rest.auth.CustomUserDetails;
import com.springboot.rest.dto.BalanceListInitResponceDto;
import com.springboot.rest.dto.BalanceListRequestDto;
import com.springboot.rest.dto.UserAmountDto;
import com.springboot.rest.dto.UserAmountSettingDto;
import com.springboot.rest.logic.FixAmountLogic;
import com.springboot.rest.logic.UsableAmountLogic;
import com.springboot.rest.repository.UserAmountRepository;

@Service
public class BalanceListService {

	@Autowired
	private UserAmountRepository userAmountRepository;

	@Autowired
	private FixAmountLogic fixAmountLogic;

	@Autowired
	private UsableAmountLogic usableAmountLogic;

	public BalanceListInitResponceDto init(BalanceListRequestDto requestDto) {
		BalanceListInitResponceDto responseDto = new BalanceListInitResponceDto();

		if ("0".equals(requestDto.getMonthFlg())) {
			responseDto = getDateBalanceList(requestDto);
		} else {
			responseDto = getMonthBalanceList(requestDto);
		}

		return responseDto;
	}

	/**
	 * Get balance ammount list.
	 * 
	 * @param date
	 * @return
	 */
	public BalanceListInitResponceDto getDateBalanceList(BalanceListRequestDto requestDto) {

		BalanceListInitResponceDto responseDto = new BalanceListInitResponceDto();
		List<UserAmountDto> incomeList = new ArrayList<>();
		List<UserAmountDto> expenditureList = new ArrayList<>();

		CustomUserDetails user = getUserInfo();

		List<UserAmountAndMCategoryJoinEntity> entity = userAmountRepository.findByUserIdAndDate(
				String.valueOf(user.getId()),
				requestDto.getDate());

		entity.stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))
				.forEach(e -> {
					UserAmountDto userAmountDto = new UserAmountDto();
					userAmountDto.setId(e.getId());
					userAmountDto.setUserId(String.valueOf(e.getUserId()));
					userAmountDto.setYearMonth(e.getBalanceYearMonth());
					userAmountDto.setDate(e.getBalanceDate());
					userAmountDto.setCategoryCode(e.getCategoryCode());
					userAmountDto.setFixFlg(e.getFixFlg());
					userAmountDto.setBalanceName(e.getBalanceName());
					userAmountDto.setBalanceFlg(e.getBalanceFlg());
					userAmountDto.setAmount(e.getAmount());
					userAmountDto.setRemarks(e.getRemarks());
					userAmountDto.setCategoryName(e.getCategoryName());
					incomeList.add(userAmountDto);
				});

		entity.stream()
				.filter(e -> "1".equals(e.getBalanceFlg()))
				.forEach(e -> {
					UserAmountDto userAmountDto = new UserAmountDto();
					userAmountDto.setId(e.getId());
					userAmountDto.setUserId(String.valueOf(e.getUserId()));
					userAmountDto.setYearMonth(e.getBalanceYearMonth());
					userAmountDto.setDate(e.getBalanceDate());
					userAmountDto.setCategoryCode(e.getCategoryCode());
					userAmountDto.setFixFlg(e.getFixFlg());
					userAmountDto.setBalanceName(e.getBalanceName());
					userAmountDto.setBalanceFlg(e.getBalanceFlg());
					userAmountDto.setAmount(e.getAmount());
					userAmountDto.setRemarks(e.getRemarks());
					userAmountDto.setCategoryName(e.getCategoryName());
					expenditureList.add(userAmountDto);
				});
		responseDto.setIncomeList(incomeList);
		responseDto.setExpenditureList(expenditureList);

		return responseDto;

	}

	/**
	 * Get balance ammount list.
	 * 
	 * @param date
	 * @return
	 */
	public BalanceListInitResponceDto getMonthBalanceList(BalanceListRequestDto requestDto) {

		BalanceListInitResponceDto responseDto = new BalanceListInitResponceDto();
		List<UserAmountDto> incomeList = new ArrayList<>();
		List<UserAmountDto> expenditureList = new ArrayList<>();

		CustomUserDetails user = getUserInfo();

		List<UserAmountAndMCategoryJoinEntity> entity = userAmountRepository.findByUserIdAndMonth(
				String.valueOf(user.getId()),
				requestDto.getMonth());

		entity.stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))
				.forEach(e -> {
					UserAmountDto userAmountDto = new UserAmountDto();
					userAmountDto.setId(e.getId());
					userAmountDto.setUserId(String.valueOf(e.getUserId()));
					userAmountDto.setYearMonth(e.getBalanceYearMonth());
					userAmountDto.setDate(e.getBalanceDate());
					userAmountDto.setCategoryCode(e.getCategoryCode());
					userAmountDto.setFixFlg(e.getFixFlg());
					userAmountDto.setBalanceName(e.getBalanceName());
					userAmountDto.setBalanceFlg(e.getBalanceFlg());
					userAmountDto.setAmount(e.getAmount());
					userAmountDto.setRemarks(e.getRemarks());
					userAmountDto.setCategoryName(e.getCategoryName());
					incomeList.add(userAmountDto);
				});

		entity.stream()
				.filter(e -> "1".equals(e.getBalanceFlg()))
				.forEach(e -> {
					UserAmountDto userAmountDto = new UserAmountDto();
					userAmountDto.setId(e.getId());
					userAmountDto.setUserId(String.valueOf(e.getUserId()));
					userAmountDto.setYearMonth(e.getBalanceYearMonth());
					userAmountDto.setDate(e.getBalanceDate());
					userAmountDto.setCategoryCode(e.getCategoryCode());
					userAmountDto.setFixFlg(e.getFixFlg());
					userAmountDto.setBalanceName(e.getBalanceName());
					userAmountDto.setBalanceFlg(e.getBalanceFlg());
					userAmountDto.setAmount(e.getAmount());
					userAmountDto.setRemarks(e.getRemarks());
					userAmountDto.setCategoryName(e.getCategoryName());
					expenditureList.add(userAmountDto);
				});
		responseDto.setIncomeList(incomeList);
		responseDto.setExpenditureList(expenditureList);

		return responseDto;

	}

	/**
	 * Delete balance list.
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(String id) throws SQLException {

		int result = userAmountRepository.delete(id);

		if (result < 1) {
			throw new SQLException();
		}
	}

	public void calculateUsableAmount(String fixFlg) throws Exception {

		if ("0".equals(fixFlg)) {
			return;
		}

		CustomUserDetails user = getUserInfo();

		//user_amount_settingの更新処理
		//目標貯金額取得
		List<UserAmountSettingDto> dtoList = usableAmountLogic.getAmountSettingByUseid(user.getId());
		int goalAmount = 0;
		if (!CollectionUtils.isEmpty(dtoList)) {
			goalAmount = dtoList.get(0).getSaveAmount();
		}
		//使用金額計算
		int usableAmount = fixAmountLogic.getUsableAmount(user.getId(), goalAmount);

		//更新処理
		usableAmountLogic.updateSetting(usableAmount, goalAmount);

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
