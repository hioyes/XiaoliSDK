package com.xiaoli.library.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.xiaoli.library.C;
import com.xiaoli.library.model.Update;
import com.xiaoli.library.net.CommonHandler;
import com.xiaoli.library.net.HttpWrapper;
import com.xiaoli.library.service.PollingService;
import com.xiaoli.library.utils.GsonUtils;
import com.xiaoli.library.utils.PollingUtils;
import com.xiaoli.library.utils.ThreadPoolUtils;
import com.xiaoli.library.utils.UpdateManager;

/**
 * FragmentActivity基类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements CommonHandler.HandlerWork,View.OnClickListener{

    protected HttpWrapper mHttpWrapper = new HttpWrapper().getInstance();
    protected Handler mHandler = CommonHandler.getInstance().getHandler();
    @Override
    public void handleMessageImpl(Message msg) {
        switch (msg.what) {
            case C.CHECK_UPDATE_TASK://升级信息处理
                Update respUpdate = GsonUtils.toObject(msg.obj.toString(), Update.class);
                if (respUpdate == null) break;
                UpdateManager updateManager = UpdateManager.getInstance();
                updateManager.checkIsNeedUpdate(respUpdate);
                break;
        }
    }

    /**
     * 页面标签
     */
    protected String TAG = getClass().getSimpleName();


    /**
     * 获取布局文件的资源id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 在setContentView之前执行
     */
    protected void viewBefore(){}

    public void replaceFragment(int containerViewId, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId,fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        C.mCurrentActivity = this;
        viewBefore();
        setContentView(getLayoutResId());
        initView();
        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        C.mCurrentActivity = this;
        CommonHandler.getInstance().setHandlerWork(this);
        if(!C.NONE_CHEECK_VERSION.contains(C.mCurrentActivity.getPackageName()) && C.CHECK_VERSION_URL!=null) {
            PollingUtils.startPollingService(C.mCurrentActivity, 5, PollingService.class, PollingService.ACTION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!C.NONE_CHEECK_VERSION.contains(C.mCurrentActivity.getPackageName()) && C.CHECK_VERSION_URL!=null) {
            PollingUtils.stopPollingService(C.mCurrentActivity, PollingService.class, PollingService.ACTION);
            C.IS_CHECK_VERSION = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        C.release(this,0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ThreadPoolUtils.destoryMyThread(this);
    }

    @Override
    public void onClick(View v) {

    }
}
