package com.xiaoli.library.net;

import android.os.Handler;
import android.os.Message;


/**
 * 全局通用handler
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public  class CommonHandler {

    private static CommonHandler mCommonHandler = null;
    private HandlerWork mHandlerWork;

    public synchronized  static CommonHandler getInstance(){
        if(mCommonHandler==null)
            mCommonHandler = new CommonHandler();
        return mCommonHandler;
    }

    public void setHandlerWork(HandlerWork handlerWork){
        mHandlerWork = handlerWork;
    }

    public Handler getHandler(){
        return mHandler;
    }

    public void setmHandler(Handler handler){
        mHandler = handler;
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mHandlerWork.handleMessageImpl(msg);
        }
    };

    public interface HandlerWork{
        void handleMessageImpl(Message msg);
    }


}
