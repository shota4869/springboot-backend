package com.springboot.rest.common;

public enum FixFlag {

	NORMAL("0"), FIXED("1");

	private final String code;

	private FixFlag(String string) {
		this.code = string;
	}

	public String getCode() {
		return code;
	}
}
