package com.xiaoli.library.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.xiaoli.library.C;
import com.xiaoli.library.R;
import com.xiaoli.library.model.Update;
import com.xiaoli.library.net.CommonHandler;
import com.xiaoli.library.utils.DateUtils;
import com.xiaoli.library.utils.GsonUtils;
import com.xiaoli.library.utils.HttpUtils;

/**
 * 轮询service
 * xiaokx
 * hioyes@qq.com
 * 2014-11-6
 */
public class PollingService extends Service {

    private String TAG = "PollingService";
    public static final String ACTION = "com.xiaoli.library.service.PollingService";

    /**
     * 通知栏icon
     */
    public static int ICON = 0;

    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"--onCreate");
        initNotifiManager();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"--onStart");
        if (C.PACKAGE_NAME != null && C.CHECK_VERSION_URL != null)
            new PollingThread().start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"--onDestroy");
    }

    private void initNotifiManager() {
        //消息通知栏处理
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = ICON;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "有新消息";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    //弹出Notification
    private void showNotification() {
        Log.e("service", DateUtils.toString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss")+"--showNotification");
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     */
    class PollingThread extends Thread {
        @Override
        public void run() {
            updateTask();
        }
    }

    private void updateTask() {
        if (!C.IS_CHECK_VERSION) {
            return;
        }
        if (C.mCurrentActivity == null) return;
        String simpleName = C.mCurrentActivity.getClass().getSimpleName();
        if (C.NONE_CHEECK_VERSION.contains(simpleName)) return;
        C.IS_CHECK_VERSION = false;
        String result = HttpUtils.sendGet(C.CHECK_VERSION_URL, null);
        Update respUpdate = GsonUtils.toObject(result, Update.class);
        if (respUpdate == null) {
            C.IS_CHECK_VERSION = true;
            return;
        }
        if (respUpdate.getVerCode() > getLocalVerCode()) {//服务器code>当前code
            Message msg = CommonHandler.getInstance().getHandler().obtainMessage();
            msg.what = C.CHECK_UPDATE_TASK;
            msg.obj = result;
            CommonHandler.getInstance().getHandler().sendMessage(msg);
        } else {
            C.IS_CHECK_VERSION = true;
        }

    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    private int getLocalVerCode() {
        int verCode = -1;
        try {
            verCode = C.mCurrentActivity.getPackageManager().getPackageInfo(C.PACKAGE_NAME, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (RuntimeException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return verCode;
    }

}
