package com.springboot.rest.Entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserAmountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "balance_year_month")
	private String balanceYearMonth;

	@Column(name = "balance_date")
	private String balanceDate;

	@Column(name = "category_code")
	private String categoryCode;

	@Column(name = "fix_name")
	private String fixFlg;

	@Column(name = "balance_name")
	private String balanceName;

	@Column(name = "balance_flg")
	private String balanceFlg;

	@Column(name = "amount")
	private int amount;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "create_at")
	private Date create;

	@Column(name = "update_at")
	private Date update;

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

	public String getBalanceYearMonth() {
		return balanceYearMonth;
	}

	public void setBalanceYearMonth(String balanceYearMonth) {
		this.balanceYearMonth = balanceYearMonth;
	}

	public String getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(String balanceDate) {
		this.balanceDate = balanceDate;
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

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getUpdate() {
		return update;
	}

	public void setUpdate(Date update) {
		this.update = update;
	}

}
