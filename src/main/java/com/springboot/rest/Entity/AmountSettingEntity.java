package com.springboot.rest.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AmountSettingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "month_year")
	private String monthYear;

	@Column(name = "save_amount")
	private int saveAmount;

	@Column(name = "fixed_income")
	private int fixedIncome;

	@Column(name = "fixed_expenditure")
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
