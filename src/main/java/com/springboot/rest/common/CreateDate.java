package com.springboot.rest.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Create system date class.
 * 
 * @author takaseshota
 *
 */
public class CreateDate {

	/**
	 * Get date time.
	 * 
	 * @return
	 */
	public static String getNowDateTime() {
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Date date = new Date(System.currentTimeMillis());
		return df.format(date);
	}

	/**
	 * Get date.
	 * 
	 * @return
	 */
	public static String getNowDate() {
		final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		final Date date = new Date(System.currentTimeMillis());
		return df.format(date);
	}

	/**
	 * Get month.
	 * 
	 * @return
	 */
	public static String getMonth() {
		final DateFormat df = new SimpleDateFormat("yyyy/MM");
		final Date date = new Date(System.currentTimeMillis());
		return df.format(date);
	}

}
