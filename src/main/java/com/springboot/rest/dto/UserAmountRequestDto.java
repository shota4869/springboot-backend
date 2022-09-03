package com.springboot.rest.dto;

/**
 * Usable amount request dto.
 * 
 * @author takaseshota
 */
public class UserAmountRequestDto {

	private String userId;

	private String categoryCode;

	private String yearMonth;

	private String date;

	private String balanceFlg;

	private String amount;

	private String remarks;

	private String fixFlg;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBalanceFlg() {
		return balanceFlg;
	}

	public void setBalanceFlg(String balanceFlg) {
		this.balanceFlg = balanceFlg;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFixFlg() {
		return fixFlg;
	}

	public void setFixFlg(String fixFlg) {
		this.fixFlg = fixFlg;
	}

}
