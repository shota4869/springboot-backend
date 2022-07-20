package com.springboot.rest.dto;

public class SettingSaveResponceDto {

	private int fixIncomeAmount;

	private int fixExpenditureAmount;

	public int getFixIncomeAmount() {
		return fixIncomeAmount;
	}

	public void setFixIncomeAmount(int fixIncomeAmount) {
		this.fixIncomeAmount = fixIncomeAmount;
	}

	public int getFixExpenditureAmount() {
		return fixExpenditureAmount;
	}

	public void setFixExpenditureAmount(int fixExpenditureAmount) {
		this.fixExpenditureAmount = fixExpenditureAmount;
	}

}
