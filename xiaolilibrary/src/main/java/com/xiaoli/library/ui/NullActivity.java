package com.xiaoli.library.ui;

import android.app.Activity;
import android.os.Bundle;

import com.xiaoli.library.C;
import com.xiaoli.library.R;
import com.xiaoli.library.service.PollingService;
import com.xiaoli.library.utils.PollingUtils;

/**
 * @author xiaokx Email:hioyes@qq.com
 * @ClassName:NullActivity
 * @date 2015-12-28
 * @Description: 空的activity,退出App使用
 */
public class NullActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_null);
        C.IS_CHECK_VERSION = true;
        PollingUtils.stopPollingService(C.mCurrentActivity, PollingService.class,PollingService.ACTION);
        finish();

//        try{
//            android.os.Process.killProcess(android.os.Process.myPid());
//        }catch (Exception e){
//
//        }
    }
}
