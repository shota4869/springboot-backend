package com.springboot.rest.dto;

/**
 * Delete balance request dto.
 * 
 * @author takaseshota
 */
public class DeleteBalanceRequestDto {

	private String id;

	private String date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
