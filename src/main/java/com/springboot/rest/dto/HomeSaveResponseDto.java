package com.springboot.rest.dto;

/**
 * Home save responce dto
 * 
 * @author takaseshota
 */
public class HomeSaveResponseDto {

	private int restAmount;

	private int balanceAmount;

	public int getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(int restAmount) {
		this.restAmount = restAmount;
	}

	public int getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(int balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
}
