package com.springboot.rest.dto;

import java.util.List;

public class CalenderInitResponseDto {

	private List<CalenderDto> calenderDtoList;

	public List<CalenderDto> getCalenderDtoList() {
		return calenderDtoList;
	}

	public void setCalenderDtoList(List<CalenderDto> calenderDtoList) {
		this.calenderDtoList = calenderDtoList;
	}

}
