package com.springboot.rest.dto;

public class SettingResponseDto {

	private UserAmountSettingDto amountSetting;

	private UserLineSettingDto lineSetting;

	public UserAmountSettingDto getAmountSetting() {
		return amountSetting;
	}

	public void setAmountSetting(UserAmountSettingDto amountSetting) {
		this.amountSetting = amountSetting;
	}

	public UserLineSettingDto getLineSetting() {
		return lineSetting;
	}

	public void setLineSetting(UserLineSettingDto lineSetting) {
		this.lineSetting = lineSetting;
	}

}
