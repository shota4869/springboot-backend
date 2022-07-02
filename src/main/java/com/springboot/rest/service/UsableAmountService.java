package com.springboot.rest.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.springboot.rest.dto.UsableAmountDto;
import com.springboot.rest.logic.CalculateBalanceLogic;
import com.springboot.rest.logic.UsableAmountLogic;

@Service
public class UsableAmountService {

	@Autowired
	private UsableAmountLogic usableAmountLogic;

	@Autowired
	private CalculateBalanceLogic calculateBalanceLogic;

	/**
	 * 
	 * 
	 * @param userId
	 * @return
	 */
	public int findUsableAmount(long userId) throws SQLException {

		List<UsableAmountDto> dtoList = new ArrayList<>();
		int usableAmount = 0;
		try {
			dtoList = usableAmountLogic.commonProcess(userId);
		} catch (SQLException e) {
			throw e;
		}

		if (!CollectionUtils.isEmpty(dtoList)) {
			usableAmount = dtoList.get(0).getUsableAmount();
		}

		int todayBlance = calculateBalanceLogic.balanceCalculete(userId);

		return usableAmount + todayBlance;
	}

}
