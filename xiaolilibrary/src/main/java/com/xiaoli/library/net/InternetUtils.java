package com.xiaoli.library.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 *网络检查工具
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class InternetUtils {

    /**

     * Unknown network class

     */

    public static final int NETWORK_CLASS_UNKNOWN = 0 ;

    /**

     * wifi net work

     */

    public static final int NETWORK_WIFI = 1 ;

    /**

     * "2G" networks

     */

    public static final int NETWORK_CLASS_2_G = 2 ;

    /**

     * "3G" networks

     */

    public static final int NETWORK_CLASS_3_G = 3 ;

    /**

     * "4G" networks

     */

    public static final int NETWORK_CLASS_4_G = 4 ;

    /**
     *判断是否有网络连接
     * @param context
     * @return false为未连接
     */
    public static boolean checkNet(Context context) {

        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     * @param context
     * @return false不可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     *判断MOBILE网络是否可用
     * @param context
     * @return false不可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * @param context
     * @return 1为wifi连接，0为移动数据连接
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }



    /**
     * 获取运营商信息
     * 0G表示未知网络
     */
    public static String getOperatorName(Context context) {
        if(getConnectedType(context)==1){
            return "wifi";
        }
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        String operatorName = "";
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")|| operator.equals("46007")) {
                operatorName="中国移动";
            } else if (operator.equals("46001")) {
                operatorName="中国联通";
            } else if (operator.equals("46003")) {
                operatorName="中国电信";
            }
        }
        operatorName = operatorName+getConnectedType(context)+"G";

        return operatorName;
    }


    /**
     * 判断手机连接的网络类型(2G,3G,4G)
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return InternetUtils.NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return InternetUtils.NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return InternetUtils.NETWORK_CLASS_4_G;
            default :
                return InternetUtils.NETWORK_CLASS_UNKNOWN;

        }
    }

    /**
     * 判断当前手机的网络类型(WIFI还是2,3,4G)
     * @param context
     * @return
     */
    public static int getNetworkStatus(Context context) {

        int netWorkType = InternetUtils.NETWORK_CLASS_UNKNOWN;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = InternetUtils.NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetworkType(context);
            }
        }

        return netWorkType;

    }


}
