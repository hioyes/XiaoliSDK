package com.xiaoli.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity转向工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class ForwardUtils {

    /**
     * activity跳转-最简单的跳转
     *
     * paramactivity     当前activity
     * paramclazz   目标类
     */
    public static void to(Activity activity, Class<?> clazz) {
        to(activity, clazz, false, null, false, -1);
    }

    /**
     * activity跳转
     *
     * paramactivity     当前activity
     * paramclazz   目标类
     * paramparams  参数
     */
    public static void to(Activity activity, Class<?> clazz, Bundle params) {
        to(activity, clazz, false, params, false, -1);
    }

    /**
     * activity跳转
     *
     * paramactivity     当前activity
     * paramclazz   目标类
     * paramparams  参数
     */
    public static void to(Activity activity, Class<?> clazz, Bundle params, int requestCode) {
        to(activity, clazz, false, params, true, requestCode);
    }

    /**
     * activity跳转-选哟finish的跳转
     *
     * paramactivity     当前activity
     * paramclazz    目标类
     * paramisFinish 是否需要调用finish方法
     */
    public static void to(Activity activity, Class<?> clazz, boolean isFinish) {
        to(activity, clazz, isFinish, null, false, -1);
    }

    /**
     * activity跳转-需要回调的跳转
     *
     * paramactivity     当前activity
     * paramclazz       目标类
     * paramrequestCode 请求码，用于回调
     */
    public static void to(Activity activity, Class<?> clazz, int requestCode) {
        to(activity, clazz, false, null, true, requestCode);
    }

    /**
     * activity跳转-需要回调的跳转
     *
     * paramactivity     当前activity
     * paramclazz       目标类
     * paramisFinish    是否需要调用finish方法
     * paramrequestCode 请求码，用于回调
     */
    public static void to(Activity activity, Class<?> clazz, boolean isFinish, int requestCode) {
        to(activity, clazz, isFinish, null, true, requestCode);
    }

    /**
     * activity跳转-带参数的跳转
     *
     * paramactivity     当前activity
     * paramclazz    目标类
     * paramisFinish 是否需要调用finish方法
     * paramparams   参数
     */
    public static void to(Activity activity, Class<?> clazz, boolean isFinish, Bundle params) {
        to(activity, clazz, isFinish, params, false, -1);
    }

    /**
     * activity跳转-带参数、且需要回调的跳转
     *
     * paramactivity     当前activity
     * paramclazz       目标类
     * paramisFinish    是否需要调用finish方法
     * paramparams      传递参数
     * paramisBack      是否需要回调onActivityResult
     * paramrequestCode 请求码，用于回调
     */
    public static void to(Activity activity, Class<?> clazz, boolean isFinish, Bundle params, boolean isBack, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity.getApplicationContext(), clazz);
        if (params != null) intent.putExtras(params);
        if (isBack) activity.startActivityForResult(intent, requestCode);
        else activity.startActivity(intent);
        if (isFinish) activity.finish();
    }
}
