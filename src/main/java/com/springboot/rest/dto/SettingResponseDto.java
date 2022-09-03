package com.springboot.rest.dto;

/**
 * Setting response dto.
 * 
 * @author takaseshota
 */
public class SettingResponseDto {

	private UserAmountSettingDto amountSetting;

	private UserLineSettingDto lineSetting;

	private int fixIncomeAmount;

	private int fixExpenditureAmount;

	public UserAmountSettingDto getAmountSetting() {
		return amountSetting;
	}

	public void setAmountSetting(UserAmountSettingDto amountSetting) {
		this.amountSetting = amountSetting;
	}

	public UserLineSettingDto getLineSetting() {
		return lineSetting;
	}

	public void setLineSetting(UserLineSettingDto lineSetting) {
		this.lineSetting = lineSetting;
	}

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
