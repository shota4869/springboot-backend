package com.springboot.rest.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateDate {

	public static String getNowDateTime() {
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Date date = new Date(System.currentTimeMillis());
		return df.format(date);
	}

	public static String getNowDate() {
		final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		final Date date = new Date(System.currentTimeMillis());
		return df.format(date);
	}

}
