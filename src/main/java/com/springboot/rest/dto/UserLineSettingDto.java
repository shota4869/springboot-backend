package com.springboot.rest.dto;

public class UserLineSettingDto {

	private long id;

	private int userId;

	private String lineFlg;

	private String accessToken;

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
