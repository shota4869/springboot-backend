package com.springboot.rest.dto;

public class UserAmountSettingDto {

	private long id;

	private int userId;

	private String monthYear;

	private int saveAmount;

	private int fixedIncome;

	private int fixedExpenditure;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public int getSaveAmount() {
		return saveAmount;
	}

	public void setSaveAmount(int saveAmount) {
		this.saveAmount = saveAmount;
	}

	public int getFixedIncome() {
		return fixedIncome;
	}

	public void setFixedIncome(int fixedIncome) {
		this.fixedIncome = fixedIncome;
	}

	public int getFixedExpenditure() {
		return fixedExpenditure;
	}

	public void setFixedExpenditure(int fixedExpenditure) {
		this.fixedExpenditure = fixedExpenditure;
	}

}
