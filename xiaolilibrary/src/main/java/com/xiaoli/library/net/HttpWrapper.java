package com.xiaoli.library.net;

import android.content.Context;
import android.os.Handler;

import com.xiaoli.library.net.impl.HttpConnection;

import java.io.File;
import java.util.Map;

/**
 *网络请求请求装饰者
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class HttpWrapper implements INetwork {
	private static HttpWrapper httpWrapper = null;
	private static INetwork  netwrok;

	public HttpWrapper getInstance() {
		if(httpWrapper==null){
			netwrok = new HttpConnection();
			httpWrapper = new HttpWrapper();
		}
		return httpWrapper;
	}

	@Override
	public boolean checkNetWorking(Context context, Handler handler) {
		return netwrok.checkNetWorking(context,handler);
	}

	@Override
	public void processResult(String result, int taskid, Handler handler) {
		netwrok.processResult(result,taskid,handler);
	}

	@Override
	public String get(String url, Map<String, String> params, int taskid) {
		return netwrok.get(url,params,taskid);
	}

	@Override
	public String get(String url, Map<String, String> params, Handler handler, int taskid) {
		return netwrok.get(url,params, handler,taskid);
	}

	@Override
	public String post(String url, Map<String, String> params,int taskid) {
		return netwrok.post(url,params,taskid);
	}

	@Override
	public String post(String url, Map<String, String> params, Handler handler, int taskid) {
		return netwrok.post(url,params,handler,taskid);
	}

	@Override
	public String post(String url, Map<String, String> params, Handler handler, int taskid, Context context) {
		return netwrok.post(url,params,handler,taskid,context);
	}

	@Override
	public String postImg(String url, Map<String, File> files, Handler handler, int taskid, Context context) {
		return netwrok.postImg(url,files,handler,taskid,context);
	}

	@Override
	public String postImg(String url, Map<String, String> params, Map<String, File> files, Handler handler, int taskid, Context context) {
		return netwrok.postImg(url,params,files,handler,taskid,context);
	}
}
