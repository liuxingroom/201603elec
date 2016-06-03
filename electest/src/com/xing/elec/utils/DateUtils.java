package com.xing.elec.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String dateToStringByFile(Date date) {
		// TODO Auto-generated method stub
		String sDate=new SimpleDateFormat("/yyyy/MM/dd/").format(date);
		return sDate;
	}

	public static String dateToString(Date date) {
		// TODO Auto-generated method stub
		String dates=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dates;
	}

	public static String dateToStringWithExcel(Date date){
		String sDate=new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		return sDate;
	}

	public static Date stringToDate(String date) {
		Date sDate=null;
		try {
			sDate=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sDate;
	}
}
