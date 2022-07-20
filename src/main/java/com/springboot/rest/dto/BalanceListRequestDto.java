package com.springboot.rest.dto;

public class BalanceListRequestDto {

	private String date;

	private String month;

	private String monthFlg;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMonthFlg() {
		return monthFlg;
	}

	public void setMonthFlg(String monthFlg) {
		this.monthFlg = monthFlg;
	}

}
