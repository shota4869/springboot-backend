package com.springboot.rest.dto;

/**
 * Usable amount dto.
 * 
 * @author takaseshota
 */
public class UserAmountDto {

	private long id;

	private String userId;

	private String categoryCode;

	private String fixFlg;

	private String balanceName;

	private String yearMonth;

	private String date;

	private String balanceFlg;

	private int amount;

	private String remarks;

	private String categoryName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public String getFixFlg() {
		return fixFlg;
	}

	public void setFixFlg(String fixFlg) {
		this.fixFlg = fixFlg;
	}

	public String getBalanceName() {
		return balanceName;
	}

	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
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

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
