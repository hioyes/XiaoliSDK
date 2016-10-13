package com.xiaoli.library;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.xiaoli.library.ui.NullActivity;
import com.xiaoli.library.utils.ThreadPoolUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 *常量、系统方法基类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public abstract class C {

    /**
     * app初始化参数
     * param packageName 包
     * param rootCatalog 存储根目录
     * param allowWriteLog 是否开启记录日志
     */
    public static void init(String packageName,String rootCatalog,boolean allowWriteLog){
        PACKAGE_NAME = packageName;
        ROOT_CATALOG = rootCatalog;
        ThreadPoolUtils.init();
        WRITE_LOG = allowWriteLog;
    }

    /**
     * 是否开启记录日志
     */
    public static boolean WRITE_LOG = true;

    /**
     * 升级弹窗资源文件
     */
    public static int DLG_UPDATE = R.layout.dlg_update;

    /**
     * 当前Activity
     */
    public static Activity mCurrentActivity;

    /**
     * 当前App的package
     */
    public static String PACKAGE_NAME = null;

    /**
     * 根目录
     */
    public static String ROOT_CATALOG = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaoli/";


    /**
     * 网络请求超时
     */
    public static final int NET_TIME_OUT = 90000;

    /**
     * 请求数据为空
     */
    public static final int DATA_IS_NULL = 90001;

    /**
     * 未连接网络
     */
    public static final int NET_IS_NULL = 90002;

    /**
     * 已连接WIFI
     */
    public static final int NET_IS_WIFI = 90003;

    /**
     * 连接的WIFI不可用
     */
    public static final int NET_IS_WIFI_UNABLE = 90004;


    /**
     * 已连接移动数据
     */
    public static final int NET_IS_MOBLE = 90005;

    /**
     * 连接的移动数据不可用
     */
    public static final int NET_IS_MOBLE_UNABLE = 90006;

    /**
     * URL地址找不到
     */
    public static final int NET_FILE_NOT_FOUND = 90007;

    /**
     * 自动轮播banner
     */
    public static final int AUTO_BANNER_AUTO_PLAY = 91000;

    /**
     * 是否检查升级
     */
    public static boolean IS_CHECK_VERSION = true;

    /**
     * 不需要檢測升级的类名
     */
    public static List<String> NONE_CHEECK_VERSION = new ArrayList<String>();

    /**
     * 版本检查地址
     */
    public static String CHECK_VERSION_URL;

    /**
     * 版本检查地址任务ID
     */
    public static final int CHECK_UPDATE_TASK = 91000;


    /**
     * 资源释放
     * @param activity
     * @param resid 空资源文件ID
     */
    public static void release(Activity activity,int resid) {
        if(activity==null)return;
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field == null) continue;
            if (Modifier.isFinal(field.getModifiers())) continue;
            field.setAccessible(true);//关闭安全检查
            try {
                String typeString = field.getType().toString();
                if (typeString.startsWith("class")) {
                    field.set(activity, null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(resid!=0)
            activity.setContentView(resid);
    }

    /**
     * 退出app
     * @param context
     */
    public static void exit(Context context){
        Intent intentOut = new Intent();
        intentOut.setClass(context, NullActivity.class);
        intentOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//当需要一次清空基于此类上所有的activity时，调用此方法
        context.startActivity(intentOut);
    }

}
