package com.xiaoli.library.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.xiaoli.library.C;


/**
 *手机硬件、软件相关工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class MobileUtils {

//	String phoneInfo = "Product: " + android.os.Build.PRODUCT;
//	phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
//	phoneInfo += ", TAGS: " + android.os.Build.TAGS;
//	phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
//	phoneInfo += ", MODEL: " + android.os.Build.MODEL;
//	phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
//	phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
//	phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
//	phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
//	phoneInfo += ", BRAND: " + android.os.Build.BRAND;
//	phoneInfo += ", BOARD: " + android.os.Build.BOARD;
//	phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
//	phoneInfo += ", ID: " + android.os.Build.ID;
//	phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
//	phoneInfo += ", USER: " + android.os.Build.USER;

	/**
	 * 照片，媒体，文件访问权限
	 * true 可用，false不可用
	 */
	public static boolean FILE_AND_ALBUM_PERMISSION_ENABLE = true;
	static{
		int version = getSystemVersionCode();
		if(version>=23){
			FILE_AND_ALBUM_PERMISSION_ENABLE = false;
		}
	}

	/**
	 * 照片，媒体，文件访问权限-请求码
	 */
	public static int FILE_AND_ALBUM_PERMISSION_REQUESTCODE = 123456789;

	/**
	 * 调用系统界面，给指定的号码拨打电话
	 * param activity
	 * param number 电话号码
     * return 0为成功，1 没有拨打电话的权限
     */
	public static int call(Activity activity, String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			//没有打拨打电话的权限

			return 1;
		}
		activity.startActivity(intent);
		return 0;
	}

	/**
	 * 获取SDK版本号
	 * return
     */
	public static int getSystemVersionCode() {
		try {
			return android.os.Build.VERSION.SDK_INT;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 获取手机型号
	 * @return
     */
	public static String getSystemModel(){
		return android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本
	 *
	 * @return
     */
	public static String getSystemVersion(){
		return android.os.Build.VERSION.RELEASE;
	}




	/**
	 * 处理6.0系统-照片，媒体，文件访问权限
	 * param activity
     */
	public static void processFileAndAlbumPermission(Activity activity){
		int hasWriteContactsPermission = ContextCompat.checkSelfPermission(C.mCurrentActivity.getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
			//没有权限去申请
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, FILE_AND_ALBUM_PERMISSION_REQUESTCODE);
		}
	}


	/**
	 * 检测摄像头设备是否可用
	 *
	 * @param context
	 * @return
	 */
	public static boolean checkCameraHardware(Context context) {
		if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}


	/**
	 * 计算焦点及测光区域
	 *
	 * @param focusWidth 聚焦panel的宽度
	 * @param focusHeight 聚焦panel的高度
	 * @param areaMultiple
	 * @param x 触摸屏X轴
	 * @param y 触摸屏Y轴
	 * @param previewleft
	 * @param previewRight
	 * @param previewTop
	 * @param previewBottom
	 * @return Rect(left,top,right,bottom) : left、top、right、bottom是以显示区域中心为原点的坐标
	 */
	public static Rect calculateTapArea(int focusWidth, int focusHeight,
										float areaMultiple, float x, float y, int previewleft,
										int previewRight, int previewTop, int previewBottom) {
		int areaWidth = (int) (focusWidth * areaMultiple);
		int areaHeight = (int) (focusHeight * areaMultiple);
		int centerX = (previewleft + previewRight) / 2;
		int centerY = (previewTop + previewBottom) / 2;
		double unitx = ((double) previewRight - (double) previewleft) / 2000;
		double unity = ((double) previewBottom - (double) previewTop) / 2000;
		int left = clamp((int) (((x - areaWidth / 2) - centerX) / unitx),
				-1000, 1000);
		int top = clamp((int) (((y - areaHeight / 2) - centerY) / unity),
				-1000, 1000);
		int right = clamp((int) (left + areaWidth / unitx), -1000, 1000);
		int bottom = clamp((int) (top + areaHeight / unity), -1000, 1000);

		return new Rect(left, top, right, bottom);
	}

	public static int clamp(int x, int min, int max) {
		if (x > max)
			return max;
		if (x < min)
			return min;
		return x;
	}



}
