package com.springboot.rest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.Entity.UserAmountAndMCategoryJoinEntity;
import com.springboot.rest.date.CreateDate;
import com.springboot.rest.dto.BalanceListInitResponceDto;
import com.springboot.rest.dto.UsableAmountDto;
import com.springboot.rest.dto.UserAmountDto;
import com.springboot.rest.dto.UserAmountRequestDto;
import com.springboot.rest.logic.CalculateBalanceLogic;
import com.springboot.rest.logic.UsableAmountLogic;
import com.springboot.rest.repository.UserAmountRepository;

@Service
public class UserAmountService {

	@Autowired
	private UserAmountRepository userAmountRepository;

	@Autowired
	private CalculateBalanceLogic calculateBalanceLogic;

	@Autowired
	private UsableAmountLogic usableAmountLogic;

	/**
	 * nsert save amount.
	 * 
	 * @param userAmountDto
	 * @return
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
	 * @param userId
	 * @return
	 */
	public int saveAmountCalculete(long userId) {

		int amount = 0;
		List<UsableAmountDto> dtoList = usableAmountLogic.findAll(userId, CreateDate.getNowDate());

		if (!CollectionUtils.isEmpty(dtoList)) {
			amount = dtoList.get(0).getUsableAmount();

		}

		return amount + calculateBalanceLogic.balanceCalculete(userId);
	}

	/**
	 * 
	 * 
	 balanceCalculete(userId)	 * @param date
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

	public void getDonutsList() {

	}

	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(String id) {

		int result = userAmountRepository.delete(id);

		if (result == 1) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 * 
	 * @param userId
	 * @param month
	 * @return
	 */
	public List<BalanceListInitResponceDto> findByUseridAndMonth(long userId, String month) {

		List<BalanceListInitResponceDto> responseDto = new ArrayList<>();

		userAmountRepository.findAllByUserIdAndMonth(String.valueOf(userId), month);

		return responseDto;
	}

}
