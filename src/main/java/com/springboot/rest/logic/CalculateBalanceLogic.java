package com.springboot.rest.logic;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.rest.date.CreateDate;
import com.springboot.rest.repository.UserAmountRepository;

@Component
public class CalculateBalanceLogic {

	@Autowired
	private UserAmountRepository userAmountRepository;

	public int balanceCalculete(long userId) {
		//本日の収支計算を行う。
		//本日の収入リスト
		List<Integer> incomeAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), CreateDate.getNowDate()).stream()
				.filter(e -> "0".equals(e.getBalanceFlg())).map(e -> e.getAmount()).collect(Collectors.toList());
		//本日の支出リスト
		List<Integer> expenditureAmountList = userAmountRepository
				.findAllByUserId(String.valueOf(userId), CreateDate.getNowDate()).stream()
				.filter(e -> "1".equals(e.getBalanceFlg())).map(e -> e.getAmount()).collect(Collectors.toList());

		//収入・支出の計算を行う。
		int totalIncome = incomeAmountList.stream().mapToInt(Integer::intValue).sum();
		int totalExpenditure = expenditureAmountList.stream().mapToInt(Integer::intValue).sum();

		return totalIncome - totalExpenditure;
	}

}
