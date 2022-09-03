package com.springboot.rest.dto;

import java.util.List;

/**
 * Balance list init responce dto.
 * 
 * @author takaseshota
 */
public class BalanceListInitResponceDto {

	private List<UserAmountDto> incomeList;

	private List<UserAmountDto> expenditureList;

	public List<UserAmountDto> getIncomeList() {
		return incomeList;
	}

	public void setIncomeList(List<UserAmountDto> incomeList) {
		this.incomeList = incomeList;
	}

	public List<UserAmountDto> getExpenditureList() {
		return expenditureList;
	}

	public void setExpenditureList(List<UserAmountDto> expenditureList) {
		this.expenditureList = expenditureList;
	}

}
