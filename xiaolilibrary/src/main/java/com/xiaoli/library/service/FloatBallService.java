package com.xiaoli.library.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaoli.library.R;
import com.xiaoli.library.utils.PromptUtils;

/**
 * xiaokx
 * hioyes@qq.com
 * 2016-7-15
 */
public class FloatBallService extends Service {
    private String TAG = this.getClass().getSimpleName();

    /**
     * 自定义悬浮布局文件
     * LinearLayout
     *
     */
    public static int LAYOUT_RES_ID=0;

    /**
     * 点击事件启动Intent
     */
    public static Intent mIntent;

    /**
     * 窗口管理器
     */
    WindowManager mWindowManager;

    /**
     * 浮动窗口View
     */
    LinearLayout mFloatBallView;
    WindowManager.LayoutParams wmLayoutParams;

    /**
     * 浮球
     */
    ImageView mIvFloatBall;

    /**
     * 是否长按
     */
    private boolean whetherLongPress = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatBallView!=null){
            mWindowManager.removeView(mFloatBallView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initView(){
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmLayoutParams = new WindowManager.LayoutParams();
        wmLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//设置窗口类型
        wmLayoutParams.format = PixelFormat.RGBA_8888;//设置图片格式：背景透明
        wmLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//设置窗口行为，不可聚焦
        wmLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmLayoutParams.x = 0;
        wmLayoutParams.y = 200;
        wmLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //获取view
        LAYOUT_RES_ID = LAYOUT_RES_ID == 0 ? R.layout.floatball : LAYOUT_RES_ID;
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        mFloatBallView = (LinearLayout) inflater.inflate(LAYOUT_RES_ID,null);
        //添加View
        mWindowManager.addView(mFloatBallView,wmLayoutParams);

        //测量大小
        mFloatBallView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //监听
        mFloatBallView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(whetherLongPress){
                    //移动,重置View位置
                    wmLayoutParams.x = (int)event.getRawX() - mFloatBallView.getMeasuredWidth()/2;
                    wmLayoutParams.y = (int)event.getRawY() - mFloatBallView.getMeasuredHeight()/2 - getStatusBarHeight();
                    mWindowManager.updateViewLayout(mFloatBallView,wmLayoutParams);
                }
                return false;
            }
        });
        mFloatBallView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e(TAG,"长按了一会");
                whetherLongPress = true;
                return false;
            }
        });
        mFloatBallView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"点击了一下");
                whetherLongPress = false;
                mIntent = getPackageManager().getLaunchIntentForPackage("com.xiaolidemo");
                if(mIntent!=null){
                    startActivity(mIntent);
                }else{
                    PromptUtils.showToast("没有设置点击事件");
                }

            }
        });


    }

    /**
     * 获取状态栏的高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
