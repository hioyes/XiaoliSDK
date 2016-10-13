package com.xiaoli.library.net;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.util.Map;

/**
 * 网络请求接口
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public interface INetwork {

    /**
     * 网络检查
     * @param context
     * @param handler
     * @return
     */
    public boolean checkNetWorking(Context context, Handler handler);

    /**
     * 网络请求结果处理，并发送handle消息
     * @param result
     * @param taskid
     */
    public void processResult(String result, int taskid, Handler handler);

    /**
     * get方式请求数据
     * @param url
     * @param params
     * @param taskid
     * @return 请求线程名称
     */
    public String get(String url, Map<String, String> params, int taskid);

    /**
     * get方式请求数据
     * @param url
     * @param params
     * @param handler
     * @param taskid
     * @return 请求线程名称
     */
    public String get(String url, Map<String, String> params, Handler handler, int taskid);

    /**
     * post方式提交数据
     * @param url
     * @param params
     * @param taskid
     * @return 请求线程名称
     */
    public String post(String url, Map<String, String> params, int taskid);

    /**
     * post方式提交数据
     * @param url
     * @param params
     * @param handler
     * @param taskid
     * @return 请求线程名称
     */
    public String post(String url, Map<String, String> params, Handler handler, int taskid);

    /**
     * post方式提交数据，并检察状态
     * @param url
     * @param params
     * @param handler
     * @param taskid
     * @param context
     * @return 请求线程名称
     */
    public String post(String url, Map<String, String> params, Handler handler, int taskid, Context context);

    /**
     * 不带参数的图片上传图片上传
     * @param url
     * @param handler
     * @param taskid
     * @param context
     * @return 请求线程名称
     */
    public String postImg(String url, final Map<String, File> files, Handler handler, int taskid, Context context);


    /**
     * 带参数的图片上传
     * @param url
     * @param params
     * @param files
     * @param handler
     * @param taskid
     * @param context
     * @return
     */
    public String postImg(final String url, final Map<String,String> params, final Map<String, File> files, final Handler handler, final int taskid, Context context);
}
