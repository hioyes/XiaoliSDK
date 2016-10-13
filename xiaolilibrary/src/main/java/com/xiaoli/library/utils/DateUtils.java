package com.xiaoli.library.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class DateUtils {

	/**
	 * 获取多少天之前日期
	 * param beforDay
	 * param pattern
     * return
     */
	public static String getBeforDate(int beforDay,String pattern){
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		long currTime = System.currentTimeMillis();
		long time = currTime/1000 - beforDay*24*60*60;
		String str  = DateUtils.toString(time, pattern);
		return str;
	}

	/**
	 * 将一个字符串转换成日期格式
	 * param date
	 * param pattern
	 * return
	 */
	public static Date toDate(String date, String pattern) {
		if((""+date).equals("")){
			return null;
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date newDate = new Date();
		try {
			newDate = sdf.parse(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return newDate;
	}
	
	/**
	 * 把日期转换成字符串型
	 * param date
	 * param pattern
	 * return
	 */
	public static String toString(Date date, String pattern){
		if(date == null){
			return "";
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			dateString = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateString;
	}

	/**
	 * 把日期转换成字符串型
	 * param time
	 * param pattern
     * return
     */
	public static String toString(Long time,String pattern){
		if(time>0){
			if(time.toString().length()==10){
				time = time*1000;
			}
			Date date = new Date(time);
			String str  = DateUtils.toString(date, pattern);
			return str;
		}
		return "";
	}

	
	
	/**
	 * 获取上个月的开始结束时间
	 * return
	 */
	public static String[] getLastMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH) + 1;
		   
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		   // 输出上月最后一天日期
		   int day = cal.get(Calendar.DAY_OF_MONTH);

		   String months = "";
		   String days = "";

		   if (month > 1) {
		    month--;
		   } else {
		    year--;
		    month = 12;
		   }
		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(day).length() > 1)) {
		    days = "0" + day;
		   } else {
		    days = String.valueOf(day);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay = "" + year + "-" + months + "-" + days;

		   String[] lastMonth = new String[2];
		   lastMonth[0] = firstDay;
		   lastMonth[1] = lastDay;

		 //  System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return lastMonth;
		}
	
	
	/**
	 * 获取当月的开始结束时间
	 * return
	 */
	public static String[] getCurrentMonth() {
		   // 取得系统当前时间
		   Calendar cal = Calendar.getInstance();
		   int year = cal.get(Calendar.YEAR);
		   int month = cal.get(Calendar.MONTH)+1 ;
		   
		   // 取得系统当前时间所在月第一天时间对象
		   cal.set(Calendar.DAY_OF_MONTH, 1);
		   
		   // 日期减一,取得上月最后一天时间对象
		   cal.add(Calendar.DAY_OF_MONTH, -1);
		   
		   // 输出上月最后一天日期
		   int day = cal.get(Calendar.DAY_OF_MONTH);

		   String months = "";
		   String days = "";


		   if (!(String.valueOf(month).length() > 1)) {
		    months = "0" + month;
		   } else {
		    months = String.valueOf(month);
		   }
		   if (!(String.valueOf(day).length() > 1)) {
		    days = "0" + day;
		   } else {
		    days = String.valueOf(day);
		   }
		   String firstDay = "" + year + "-" + months + "-01";
		   String lastDay = "" + year + "-" + months + "-" + days;

		   String[] currentMonth = new String[2];
		   currentMonth[0] = firstDay;
		   currentMonth[1] = lastDay;

		 //  System.out.println(lastMonth[0] + "||" + lastMonth[1]);
		   return currentMonth;
		}


	/**
	 * 获取当前时间戳(10位)
	 * return
     */
	public static int getDateline(){
		
		return (int)(System.currentTimeMillis()/1000);
	}


	/**
	 * 获取当前时间戳(10位)
	 * return
	 */
	public static long getDatelineLong(){
		
		return System.currentTimeMillis()/1000;
	}

	/**
	 * 将指定日期转换成13位时间戳
	 * param date 日期
	 * param pattern 日期格式
     * return
     */
	public static long getDatelineLong(String date,String pattern){
		return toDate(date, pattern).getTime();
	}

	/**
	 * 将指定日期转换成10位时间戳
	 * param date
	 * return
     */
	public static int getDateline(String date){
		return (int)(toDate(date, "yyyy-MM-dd").getTime()/1000);
	}

	/**
	 * 将指定日期转换成10位时间戳
	 * param date
	 * param pattern
     * return
     */
	public static int getDateline(String date,String pattern){
		return (int)(toDate(date, pattern).getTime()/1000);
	}

	/**
	 * 检查当前时间是否包含在区间内
	 * param startTime
	 * param endTime
	 * return
	 */
	public static boolean containsCurrTime(Long startTime,Long endTime){
		if(startTime==null || endTime==null){
			return false;
		}
		Long curr = DateUtils.getDatelineLong();
		if(curr.longValue()>startTime.longValue() && curr.longValue()<endTime.longValue())
			return true;
		return false;
	}
	/**
	 * 检查当前时间是否包含在区间内
	 * param startTime
	 * param endTime
	 * return 0 时间区间错误  1 在区间内 2 当前时间小于开始区间值 3 当前时间大于结束区间值
	 */
	public static int CurrTimeStatus(Long startTime,Long endTime){
		if(startTime==null || endTime==null){
			return 0;
		}
		Long curr = DateUtils.getDatelineLong();
		if(curr.longValue()>startTime.longValue() && curr.longValue()<endTime.longValue())
			return 1;
		else if(curr.longValue()<startTime.longValue())
			return 2;
		else if(curr.longValue()>endTime.longValue())
			return 3;
		return 0;
	}
}
