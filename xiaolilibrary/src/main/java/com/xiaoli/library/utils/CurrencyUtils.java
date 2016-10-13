package com.xiaoli.library.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *由于Java的简单类型不能够精确的对浮点数进行运算， 这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public final class CurrencyUtils {
	/**
	 * 默认除法运算精度
	 */
	private static final int DEF_DIV_SCALE = 2;

	// 这个类不能实例化
	private CurrencyUtils() {
	}

	/**
	 * 提供精确的加法运算。
	 * 
	 * param v1
	 *            被加数
	 * param v2
	 *            加数
	 * return 两个参数的和v1+v2
	 */
	public static Double add(Double v1, Double v2) {
		try {
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.add(b2).doubleValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	/**
	 * 提供精确的减法运算。
	 * 
	 * param v1
	 *            被减数
	 * param v2
	 *            减数
	 * return 两个参数的差 v1-v2
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	
	/**
	 * 提供精确的乘法运算。
	 * 
	 * param v1
	 *            被乘数
	 * param v2
	 *            乘数
	 * return 两个参数的积v1*v2
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 *
	 * param v1
	 *            被除数
	 * param v2
	 *            除数
	 * return 两个参数的商的字符串v1/v2
	 */
	public static String divForStr(double v1, double v2) {
		Double val = div(v1, v2, DEF_DIV_SCALE);
		return CurrencyUtils.format(val,DEF_DIV_SCALE);
	}

	/**
	 *
	 *提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 * param v1 被除数
	 * param v2  除数
	 * param scale  保留小数长度
	 * return 两个参数的商的字符串v1/v2
	 */
	public static String divForStr(double v1, double v2, int scale) {
		Double val = div(v1, v2, scale);
		return CurrencyUtils.format(val,scale);
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 * 
	 * param v1
	 *            被除数
	 * param v2
	 *            除数
	 * return 两个参数的商v1/v2
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * param v1
	 *            被除数
	 * param v2
	 *            除数
	 * param scale
	 *            表示表示需要精确到小数点以后几位。
	 * return 两个参数的商 v1/v2
	 */
	public static Double div(Double v1, Double v2, int scale) {
		try {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b1 = new BigDecimal(Double.toString(v1));
			BigDecimal b2 = new BigDecimal(Double.toString(v2));
			return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * param v
	 *            需要四舍五入的数字
	 * param scale
	 *            小数点后保留几位
	 * return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 格式化成固定小数位。
	 * param v 需要格式化的数字
	 * param scale 保留小数即几位
	 * return
	 */
	public static String format(Double v,int scale){
		try {
			String s = "";
			for (int i = 0; i < scale; i++) {
				if(i==0)s+=".";
				s += "0";
			}
			DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
			df.applyPattern("0"+s);
			return df.format(v);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	/**
	 * 格式化成固定小数位
	 * param str 需要格式化的数字
	 * param scale 保留小数即几位
	 * return
	 */
	public static String format(String str,int scale){
		try {
			return format(Double.valueOf(str),scale);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	/**
	 * 转换成 %
	 * param v
	 * return
	 */
	public static String toPercent(double v){
		String s = "" ;
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		int size = getSize(v*100);
		for (int i = 0; i < size; i++) {
			if(i==0)s+=".";
			s += "0";
		}
		df.applyPattern("0"+s);
		return df.format(v*100)+"%";
	}

	/**
	 * 小时部位长度
	 * param d
	 * return
     */
	public static int getSize (Double d) {
		String str = String.valueOf(d) ;
		int length = 0 ;
		if(str.contains(".")){
			int last = str.length()-str.indexOf(".")-1 ;
			if(last>0){
				length = last; 
			}
		}
		return length ;
	}
	
	public static void main(String[] args){
		System.out.println(CurrencyUtils.format("0.3", 2));
	}
}