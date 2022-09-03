package com.springboot.rest.common;

/**
 * Month flg.
 * 
 * @author takaseshota
 */
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
