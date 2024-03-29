package com.springboot.rest.common;

/**
 * Balance flg.
 * 
 * @author takaseshota
 */
public enum BalanceFlag {

	INCOME("0"), EXPENDTURE("1");

	private final String code;

	private BalanceFlag(String string) {
		this.code = string;
	}

	public String getCode() {
		return code;
	}
}
