package com.springboot.rest.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.rest.common.BalanceFlag;
import com.springboot.rest.common.CreateDate;
import com.springboot.rest.common.FixFlag;
import com.springboot.rest.repository.UserAmountRepository;

/**
 * Calculate balance logic.
 * 
 * @author takaseshota
 */
@Component
public class CalculateBalanceLogic {

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Calculate balance.
	 * 
	 * @param userId
	 * @return
	 */
	public int balanceCalculete(long userId) {
		//本日の収支計算を行う。
		return getIncomeAmount(userId) - getExpenditureAmount(userId);
	}

	/**
	 * Get incoome amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int getIncomeAmount(long userId) {
		//本日の収入リスト
		List<Integer> incomeAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), CreateDate.getNowDate()).stream()
				.filter(e -> BalanceFlag.INCOME.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());

		return incomeAmountList.stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Get expenditure amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int getExpenditureAmount(long userId) {
		//本日の支出リスト
		List<Integer> expenditureAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), CreateDate.getNowDate()).stream()
				.filter(e -> BalanceFlag.EXPENDTURE.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());

		return expenditureAmountList.stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Get previous day balance.
	 * 
	 * @param userId
	 * @return
	 */
	public int getPreviousDaybalanceCalculete(long userId) {
		//昨日の収支計算を行う。
		return getPreviousDayIncomeAmount(userId) - getPreviousDayExpenditureAmount(userId);
	}

	/**
	 * Get previous day income amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int getPreviousDayIncomeAmount(long userId) {
		Calendar cal = Calendar.getInstance();
		Date nowDate = new Date();
		cal.setTime(nowDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		String strPreviousDate = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());
		//昨日の収入リスト
		List<Integer> incomeAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), strPreviousDate).stream()
				.filter(e -> BalanceFlag.INCOME.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());

		return incomeAmountList.stream().mapToInt(Integer::intValue).sum();
	}

	/**
	 * Get previous day expenditure amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int getPreviousDayExpenditureAmount(long userId) {
		Calendar cal = Calendar.getInstance();
		Date nowDate = new Date();
		cal.setTime(nowDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		String strPreviousDate = new SimpleDateFormat("yyyy/MM/dd").format(cal.getTime());

		//本日の支出リスト
		List<Integer> expenditureAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), strPreviousDate).stream()
				.filter(e -> BalanceFlag.EXPENDTURE.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.NORMAL.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());

		return expenditureAmountList.stream().mapToInt(Integer::intValue).sum();
	}

}
