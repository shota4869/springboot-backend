package com.springboot.rest.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Create system date class.
 * 
 * @author takaseshota
 *
 */
public class CreateDate {

	private static final Date date = new Date();

	/**
	 * Get date time.
	 * 
	 * @return
	 */
	public static String getNowDateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone timeZoneJP = TimeZone.getTimeZone("Asia/Tokyo");
		df.setTimeZone(timeZoneJP);
		return df.format(date);
	}

	/**
	 * Get date.
	 * 
	 * @return
	 */
	public static String getNowDate() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		TimeZone timeZoneJP = TimeZone.getTimeZone("Asia/Tokyo");
		Date date = new Date();
		df.setTimeZone(timeZoneJP);
		return df.format(date);
	}

	/**
	 * Get month.
	 * 
	 * @return
	 */
	public static String getMonth() {
		DateFormat df = new SimpleDateFormat("yyyy/MM");
		TimeZone timeZoneJP = TimeZone.getTimeZone("Asia/Tokyo");
		Date date = new Date();
		df.setTimeZone(timeZoneJP);
		return df.format(date);
	}

}
