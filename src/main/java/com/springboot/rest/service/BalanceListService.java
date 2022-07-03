package com.springboot.rest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.rest.Entity.UserAmountAndMCategoryJoinEntity;
import com.springboot.rest.dto.BalanceListInitResponceDto;
import com.springboot.rest.dto.UserAmountDto;
import com.springboot.rest.repository.UserAmountRepository;

@Service
public class BalanceListService {

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Get balance ammount list.
	 * 
	 * @param date
	 * @return
	 */
	public BalanceListInitResponceDto findBalanceList(long userId, String date) {

		BalanceListInitResponceDto responseDto = new BalanceListInitResponceDto();
		List<UserAmountDto> incomeList = new ArrayList<>();
		List<UserAmountDto> expenditureList = new ArrayList<>();

		List<UserAmountAndMCategoryJoinEntity> entity = userAmountRepository.findByUserIdAndDate(String.valueOf(userId),
				date);

		entity.stream()
				.filter(e -> "0".equals(e.getBalanceFlg()))
				.forEach(e -> {
					UserAmountDto userAmountDto = new UserAmountDto();
					userAmountDto.setId(e.getId());
					userAmountDto.setUserId(String.valueOf(e.getUserId()));
					userAmountDto.setYearMonth(e.getBalanceYearMonth());
					userAmountDto.setDate(e.getBalanceDate());
					userAmountDto.setCategoryCode(e.getCategoryCode());
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

}
