package com.springboot.rest.dto;

/**
 * Balance list category dto.
 * 
 * @author takaseshota
 */
public class BlanceListCategoryDto {

	private String categoryName;

	private String totalAmount;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
}
