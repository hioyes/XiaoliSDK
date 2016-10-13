package com.xiaoli.library.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xiaoli.library.R;

/**
 *自定义dialog,实现动态指定资源文件；手动取消；停留时间的设置
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class MyDialog extends Dialog {

    /**
     * 取消dialog消息标记
     */
    private int FLAG_DISMISS = 1;

    /**
     * 是否执行时间监控
     */
    private boolean flag = true;

    /**
     * 资源文件id
     */
    private int _resid;

    /**
     * dialog停留时间，毫秒級，0为永久
     */
    private long _duration = 0;

    /**
     * view重置对象
     */
    private ResetView _rv = null;


    /**
     * 重置视图接口
     */
    public interface ResetView {
        public void initView(Dialog dialog);
    }

    /**
     * 构造函数,设置上下文、资源Id
     * @param context
     */
    public MyDialog(Context context, int resid) {
        super(context, R.style.CustomDialog);
        this._resid = resid;
    }

    /**
     * 造函数,设置上下文、资源Id,主题风格
     * @param context 上下文
     * @param resid 资源Id
     * @param theme 主题风格
     */
    public MyDialog(Context context, int resid, int theme){
        super(context, theme);
        this._resid = resid;
    }

    /**
     * 设置持续时间(毫秒)
     * 默认为永久
     * @param duration
     */
    public void setDuration(long duration){
        this._duration = duration;
    }

    /**
     * 重置视图，重写initView进行view赋值，监听等操作
     * @param rv
     */
    public void setResetView(ResetView rv){
        this._rv = rv;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(_resid);
        if (_rv != null) _rv.initView(this);
    }

    @Override
    public void show() {
        try {
            super.show();
            if (_duration != 0)
                mMyThread.start();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    //设置窗口显示
    public void show(int x, int y){
        try {
            windowDeploy(x,y);
            super.show();
            if (_duration != 0)
                mMyThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置窗口显示
     * @param x
     * @param y
     */
    private void windowDeploy(int x,int y){
        Window window= getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
        wl.alpha = 0.3f; //设置透明度
        wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            flag = false;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private Thread mMyThread = new Thread(new Runnable() {

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(_duration);
                    Message msg = mMyHandler.obtainMessage();
                    msg.what = FLAG_DISMISS;
                    mMyHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FLAG_DISMISS)
                dismiss();
        }

    };




}
