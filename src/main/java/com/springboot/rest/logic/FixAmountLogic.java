package com.springboot.rest.logic;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.rest.common.BalanceFlag;
import com.springboot.rest.common.CreateDate;
import com.springboot.rest.common.FixFlag;
import com.springboot.rest.repository.UserAmountRepository;

/**
 * Fix amount logic.
 * 
 * @author takaseshota
 */
@Component
public class FixAmountLogic {

	@Autowired
	private UserAmountRepository userAmountRepository;

	/**
	 * Get usable amount.
	 * 
	 * @param userId
	 * @param saveAmount
	 * @return
	 */
	public int getUsableAmount(long userId, int saveAmount) {
		return (calculateFixAmount(userId) - saveAmount) / getDays();

	}

	/**
	 * Calculate fix amount.
	 * 
	 * @param userId
	 * @return
	 */
	public int calculateFixAmount(long userId) {
		//合計収支
		int totalBalance = getFixIncome(userId) - getFixExpenditure(userId);

		return totalBalance;

	}

	/**
	 * Get days.
	 * 
	 * @return
	 */
	public int getDays() {

		Calendar calTo = Calendar.getInstance();
		int lastDay = calTo.getActualMaximum(Calendar.DATE);

		return lastDay;
	}

	/**
	 * Get fix income.
	 * 
	 * @param userId
	 * @return
	 */
	public int getFixIncome(long userId) {
		//固定収入
		List<Integer> incomeAmountList = userAmountRepository
				.findAllByUserIdAndMonth(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7)).stream()
				.filter(e -> BalanceFlag.INCOME.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.FIXED.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());

		//収入合計
		int totalIncome = incomeAmountList.stream().mapToInt(Integer::intValue).sum();
		return totalIncome;

	}

	/**
	 * Get fix expenditure.
	 * 
	 * @param userId
	 * @return
	 */
	public int getFixExpenditure(long userId) {

		List<Integer> expenditureAmountList = userAmountRepository
				.findAllByUserIdAndMonth(String.valueOf(userId), CreateDate.getNowDate().substring(0, 7)).stream()
				.filter(e -> BalanceFlag.EXPENDTURE.getCode().equals(e.getBalanceFlg()))
				.filter(e -> FixFlag.FIXED.getCode().equals(e.getFixFlg()))
				.map(e -> e.getAmount()).collect(Collectors.toList());
		//支出合計
		int totalExpenditure = expenditureAmountList.stream().mapToInt(Integer::intValue).sum();
		return totalExpenditure;

	}

}
