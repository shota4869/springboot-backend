package com.springboot.rest.dto;

import java.io.Serializable;
import java.util.List;

public class HomeInitResponseDto implements Serializable {

	private List<CategoryDto> incomeCategory;

	private List<CategoryDto> expenditureCategory;

	private int saveAmount;

	private int usableAmount;

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

}
