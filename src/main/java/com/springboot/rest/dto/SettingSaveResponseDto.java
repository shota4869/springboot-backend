package com.springboot.rest.dto;

/**
 * Setting save response dto.
 * 
 * @author takaseshota
 */
public class SettingSaveResponseDto {

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
