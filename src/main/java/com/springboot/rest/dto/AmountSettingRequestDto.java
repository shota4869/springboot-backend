package com.springboot.rest.dto;

public class AmountSettingRequestDto {

	private String goalAmount;

	private String fixIncome;

	private String fixExpenditure;

	public String getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(String goalAmount) {
		this.goalAmount = goalAmount;
	}

	public String getFixIncome() {
		return fixIncome;
	}

	public void setFixIncome(String fixIncome) {
		this.fixIncome = fixIncome;
	}

	public String getFixExpenditure() {
		return fixExpenditure;
	}

	public void setFixExpenditure(String fixExpenditure) {
		this.fixExpenditure = fixExpenditure;
	}

}
