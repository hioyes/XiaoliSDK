package com.xiaoli.library.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

/**
 * 轮询工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class PollingUtils {

    /**
     * 开启轮询服务
     * param context 上下文
     * param seconds 轮询间隔（秒）
     * param cls 目标Service
     * param action 动作
     */
    public static void startPollingService(Context context, int seconds, Class<?> cls,String action) {
        String fileContent = DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"->startPollingService";
        Log.e("PollingUtils",fileContent);
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间
        long triggerAtTime = SystemClock.elapsedRealtime();

        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,seconds * 1000, pendingIntent);


    }

    /**
     * 停止轮询服务
     * param context 上下文
     * param cls 目标Service
     * param action 动作
     */
    public static void stopPollingService(Context context, Class<?> cls,String action) {
        if(context==null)return;
        String fileContent = DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"->stopPollingService";
        Log.e("PollingUtils",fileContent);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(manager==null)return;
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //取消正在执行的服务
        manager.cancel(pendingIntent);
    }
}
