package com.cmp.res.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	
	
	
	
	public static String getDateStr(Date date){
		if(date==null){
			return null;
		}
		String pattern="yyyy-MM-dd HH:mm:ss";
		DateFormat dateDire=new SimpleDateFormat(pattern);
		return dateDire.format(date);
	}
	
	
	/**
	 * 日期格式化为字符串
	 * @param date   默认值为当前日期
	 * @param pattern   默认值为yyyyMMdd
	 * @return
	 */
	public static String getDateStr(Date date,String pattern){
		if(date==null){
			Calendar calendar=Calendar.getInstance();
			date=calendar.getTime();
		}
		if(StringUtils.isBlank(pattern)){
			pattern="yyyyMMdd";
		}
		DateFormat dateDire=new SimpleDateFormat(pattern);
		
		return dateDire.format(date);
	}
	
	public static Date getDateByStr(String str,String pattern) throws ParseException{
		
		if(StringUtils.isBlank(pattern)){
			pattern="yyyyMMdd";
		}
		DateFormat dateDire=new SimpleDateFormat(pattern);
		return dateDire.parse(str);
	}
	
	public static String getDateStr(String pattern,int days){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, days);
		if(StringUtils.isBlank(pattern)){
			pattern="yyyyMMdd";
		}
		DateFormat dateDire=new SimpleDateFormat(pattern);
		return dateDire.format(calendar.getTime());
	}
	
	
	public static int getDateInt(Date date,String pattern){
		String str=getDateStr(date, pattern);
		if(StringUtils.isBlank(str)||!StringUtils.isNumeric(str)){
			return 0;
		}
		return Integer.parseInt(str);
	}
	
	public static void main(String[] args) throws ParseException {
		String str="1989/2/3";
		Date date=getDateByStr(str, "yyyy/MM/dd");
		System.out.println(getDateStr(date));
		
	}
	

}
