package com.springboot.rest.dto;

import java.io.Serializable;
import java.util.List;

public class HomeInitResponseDto implements Serializable {

	private List<CategoryDto> incomeCategory;

	private List<CategoryDto> expenditureCategory;

	private int fixIncome;

	private int fixExpenditure;

	private int saveAmount;

	private int usableAmount;

	private int balanceAmount;

	private int restUsableAmount;

	public List<CategoryDto> getIncomeCategory() {
		return incomeCategory;
	}

	public void setIncomeCategory(List<CategoryDto> incomeCategory) {
		this.incomeCategory = incomeCategory;
	}

	public List<CategoryDto> getExpenditureCategory() {
		return expenditureCategory;
	}

	public void setExpenditureCategory(List<CategoryDto> expenditureCategory) {
		this.expenditureCategory = expenditureCategory;
	}

	public int getFixIncome() {
		return fixIncome;
	}

	public void setFixIncome(int fixIncome) {
		this.fixIncome = fixIncome;
	}

	public int getFixExpenditure() {
		return fixExpenditure;
	}

	public void setFixExpenditure(int fixExpenditure) {
		this.fixExpenditure = fixExpenditure;
	}

	public int getSaveAmount() {
		return saveAmount;
	}

	public void setSaveAmount(int saveAmount) {
		this.saveAmount = saveAmount;
	}

	public int getUsableAmount() {
		return usableAmount;
	}

	public void setUsableAmount(int usableAmount) {
		this.usableAmount = usableAmount;
	}

	public int getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(int balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public int getRestUsableAmount() {
		return restUsableAmount;
	}

	public void setRestUsableAmount(int restUsableAmount) {
		this.restUsableAmount = restUsableAmount;
	}

}
