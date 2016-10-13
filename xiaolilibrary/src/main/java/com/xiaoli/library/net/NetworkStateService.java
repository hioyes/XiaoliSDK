package com.xiaoli.library.net;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;



/**
 * 使用Service和BroadcastReceiver实时监听网络状态
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class NetworkStateService extends Service {

    private ConnectivityManager connectivityManager;//网络状态管理对象
    private NetworkInfo info;//用来描述网络信息的对象

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                //网络状态已经改变"
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
//                    String name = info.getTypeName();//当前网络名称
//                    PromptUtils.showToast("当前网络名称:"+name);
                } else {
                    //没有可用网络
                }
            }

        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //注册广播
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁广播
        unregisterReceiver(mReceiver);
    }
}
