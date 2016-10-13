package com.xiaoli.library.net.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.xiaoli.library.C;
import com.xiaoli.library.net.CommonHandler;
import com.xiaoli.library.net.INetwork;
import com.xiaoli.library.net.InternetUtils;
import com.xiaoli.library.task.LogThread;
import com.xiaoli.library.utils.DateUtils;
import com.xiaoli.library.utils.HttpUtils;
import com.xiaoli.library.utils.StringUtils;
import com.xiaoli.library.utils.ThreadPoolUtils;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * HttpConnection网络请求与handler通信处理
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class HttpConnection implements INetwork {
    @Override
    public boolean checkNetWorking(Context context, Handler handler) {
        if(!InternetUtils.checkNet(context)){
            Message msg = handler.obtainMessage();
            msg.what = C.NET_IS_NULL;
            msg.obj = "未连接网络";
            handler.sendMessage(msg);
            return false;
        }
        int netType = InternetUtils.getConnectedType(context);
        if(netType==0){
            //连接到移动网络
            if(!InternetUtils.isMobileConnected(context)){
                Message msg = handler.obtainMessage();
                msg.what = C.NET_IS_MOBLE_UNABLE;
                msg.obj = "移动网络不可用";
                handler.sendMessage(msg);
                return false;
            }
        }else if(netType==1){
            //连接到wifi
            if(!InternetUtils.isWifiConnected(context)){
                Message msg = handler.obtainMessage();
                msg.what = C.NET_IS_WIFI_UNABLE;
                msg.obj = "WIFI不可用";
                handler.sendMessage(msg);
                return false;
            }
        }else{
            Message msg = handler.obtainMessage();
            msg.what = C.NET_IS_NULL;
            msg.obj = "非WIFI，非移动网络";
            handler.sendMessage(msg);
            return false;
        }
        return true;
    }

    @Override
    public void processResult(String result, int taskid, Handler handler) {
        try {
            if (StringUtils.isEmpty(result)) {
                Message msg = handler.obtainMessage();
                msg.what = taskid;
                msg.obj = "http wrapper check data is null";
                handler.sendMessage(msg);
            } else if (result.equals(HttpUtils.TIME_OUT)) {
                Message msg = handler.obtainMessage();
                msg.what = C.NET_TIME_OUT;
                msg.obj = "http wrapper check data is null->TIME_OUT";
                handler.sendMessage(msg);
            } else if (result.equals(HttpUtils.FILE_NOT_FOUND)) {
                Message msg = handler.obtainMessage();
                msg.what = C.NET_FILE_NOT_FOUND;
                msg.obj = "http wrapper check data is null->FILE_NOT_FOUND";
                handler.sendMessage(msg);
            } else if (result.equals(HttpUtils.SERVER_NOT_FOUND)) {
                Message msg = handler.obtainMessage();
                msg.what = C.NET_FILE_NOT_FOUND;
                msg.obj = "http wrapper check data is null->SERVER_NOT_FOUND";
                handler.sendMessage(msg);
            } else {
                Message msg = handler.obtainMessage();
                msg.what = taskid;
                msg.obj = result;
                handler.sendMessage(msg);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String get(String url, Map<String, String> params, int taskid) {
        return this.get(url,params, CommonHandler.getInstance().getHandler(),taskid);
    }

    @Override
    public String get(String url, Map<String, String> params, Handler handler, int taskid) {
        if(!checkNetWorking(C.mCurrentActivity,handler))return "";
        final String doUrl = url;
        final Map<String, String> doParams = params;
        final Handler _handler = handler;
        final int _taskid = taskid;
        final Runnable runnable = new Runnable() {
            public void run() {
                String result = HttpUtils.sendGet(doUrl, doParams);
                processResult(result,_taskid,_handler);
                if(C.WRITE_LOG) {
                    String fileContent = DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " ---->";
                    fileContent += "taskid=" + _taskid + "\r\n";
                    if (doParams!=null && !doParams.isEmpty())
                        fileContent += "post params->" + doParams.toString() + "\r\n";
                    fileContent += "post result->" + result + "\r\n";
                    LogThread logThread = new LogThread("api" + DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd") + ".txt", fileContent);
                    ThreadPoolUtils.add(logThread);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName(UUID.randomUUID().toString());
        thread.start();
        ThreadPoolUtils.addMyThread(C.mCurrentActivity,thread);
        return thread.getName();
    }

    @Override
    public String post(String url, Map<String, String> params,int taskid) {
        return this.post(url,params, CommonHandler.getInstance().getHandler(), taskid,C.mCurrentActivity);
    }

    @Override
    public String post(String url, Map<String, String> params, Handler handler, int taskid) {
        return this.post(url,params,handler,taskid,C.mCurrentActivity);
    }

    @Override
    public String post(String url, Map<String, String> params, Handler handler, int taskid, Context context) {
        if(!checkNetWorking(context,handler))return "";
        final String doUrl = url;
        final Map<String, String> doParams = params;
        final Handler _handler = handler;
        final int _taskid = taskid;
        final Runnable runnable = new Runnable() {
            public void run() {
                String result = HttpUtils.sendPost(doUrl, doParams);
                processResult(result, _taskid,_handler);
                if(C.WRITE_LOG) {
                    String fileContent = DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " ---->";
                    fileContent += "taskid=" + _taskid + "\r\n";
                    if (doParams!=null && !doParams.isEmpty())
                        fileContent += "post params->" + doParams.toString() + "\r\n";
                    result = result == null ? "返回空数据" : result;
                    fileContent += "post result->" + result + "\r\n";
                    LogThread logThread = new LogThread("api" + DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd") + ".txt", fileContent);
                    ThreadPoolUtils.add(logThread);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName(UUID.randomUUID().toString());
        thread.start();
        ThreadPoolUtils.addMyThread(C.mCurrentActivity,thread);
        return thread.getName();
    }


    @Override
    public String postImg(String url, Map<String, File> files, Handler handler, int taskid, Context context) {
        return this.postImg(url,null,files,handler,taskid,context);
    }

    @Override
    public String postImg(String url, Map<String, String> params, Map<String, File> files, Handler handler, int taskid, Context context) {
        if(!checkNetWorking(context,handler))return "";
        final String doUrl = url;
        final Map<String, String> doParams = params;
        final Map<String, File> _files = files;
        final Handler _handler = handler;
        final int _taskid = taskid;
        final Runnable runnable = new Runnable() {
            public void run() {
                String result = HttpUtils.postImg(doUrl,doParams,_files);
                processResult(result, _taskid,_handler);
                if(C.WRITE_LOG) {
                    String fileContent = DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + " ---->";
                    fileContent += "taskid=" + _taskid + "\r\n";
                    if (!doParams.isEmpty())
                        fileContent += "post params->" + doParams.toString() + "\r\n";
                    result = result == null ? "返回空数据" : result;
                    fileContent += "post result->" + result + "\r\n";
                    LogThread logThread = new LogThread("api" + DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd") + ".txt", fileContent);
                    ThreadPoolUtils.add(logThread);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName(UUID.randomUUID().toString());
        thread.start();
        ThreadPoolUtils.addMyThread(C.mCurrentActivity,thread);
        return thread.getName();
    }


}
