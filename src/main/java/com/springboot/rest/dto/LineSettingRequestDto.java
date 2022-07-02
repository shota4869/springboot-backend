package com.springboot.rest.dto;

public class LineSettingRequestDto {

	private String lineFlg;

	private String accessToken;

	public String getLineFlg() {
		return lineFlg;
	}

	public void setLineFlg(String lineFlg) {
		this.lineFlg = lineFlg;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
