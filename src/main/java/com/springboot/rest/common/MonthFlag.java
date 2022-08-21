package com.springboot.rest.common;

public enum MonthFlag {

	INVALID("0"), VALID("1");

	private final String code;

	private MonthFlag(String string) {
		this.code = string;
	}

	public String getCode() {
		return code;
	}
}
