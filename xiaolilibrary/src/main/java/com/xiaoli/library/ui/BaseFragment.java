package com.xiaoli.library.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoli.library.C;
import com.xiaoli.library.net.HttpWrapper;
import com.xiaoli.library.utils.ThreadPoolUtils;


/**
 * Fragment基类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{

    protected HttpWrapper mHttpWrapper = new HttpWrapper().getInstance();
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handleMessageImpl(msg);
        }
    };
    public void handleMessageImpl(Message msg) {

    }

    /**
     * 页面标签
     */
    protected String TAG = getClass().getSimpleName();


    protected View mView;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBefore();
        if (mView == null)
            mView = inflater.inflate(getLayoutResId(), container, false);
        initView();
        initListener();
        initData();
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        C.mCurrentActivity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        C.mCurrentActivity = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ThreadPoolUtils.destoryMyThread(getActivity());
        C.release(getActivity(), 0);
    }

    @Override
    public void onClick(View v) {

    }
}
