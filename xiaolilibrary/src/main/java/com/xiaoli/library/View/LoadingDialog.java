package com.xiaoli.library.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoli.library.C;
import com.xiaoli.library.R;


/**
 * 网络加载小菊花
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class LoadingDialog extends ProgressDialog {

    private static LoadingDialog mLoadingDialog = null;

    /**
     * 显示转圈
     * @param context
     * @param isCancel 是否可手动取消
     */
    public synchronized static void showDialog(Context context,boolean isCancel){
        mLoadingDialog = new LoadingDialog(context,isCancel);
        if (mLoadingDialog.isShowing() == false) {
            try{
                mLoadingDialog.show();
            }catch (Exception e){}
        }
    }

    /**
     * 关闭转圈
     */
    public synchronized static void closeDialog(){
        if (mLoadingDialog!=null && mLoadingDialog.isShowing()) {
            try{
                mLoadingDialog.dismiss();
            }catch (Exception e){}
        }
    }

    /** 是否能被取消 */
    private boolean mCancleable;
    /** 信息显示控件 */
    private TextView mTvMessage;
    /** 所要显示的信息 */
    private String message;

    /**
     * @param context
     *            上下文对象
     * @param cancleable
     *            对话框是否可以取消 false 触碰屏幕不能取消
     **/
    public LoadingDialog(Context context, boolean cancleable) {
        super(context, R.style.loadingDialog);
        mCancleable = cancleable;
    }

    public LoadingDialog(Context context, boolean cancleable, String message) {
        super(context,R.style.loadingDialog);
        mCancleable = cancleable;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_loading);
        setCancelable(mCancleable);
        setScreenBrightness(0);
        initView();
        initListener();
    }

    /** 初始化控件 */
    private void initView() {
        mTvMessage = (TextView) findViewById(R.id.uitv_message);
        if (message != null) {
            mTvMessage.setText(message);
        }
    }

    /** 初始化监听 */
    private void initListener() {
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ImageView image = (ImageView) findViewById(R.id.ui_dialog_loading_iv);
                Animation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setRepeatCount(Animation.INFINITE); // 设置INFINITE，对应值-1，代表重复次数为无穷次
                anim.setDuration(1000); // 设置该动画的持续时间，毫秒单位
                anim.setInterpolator(new LinearInterpolator()); // 设置一个插入器，或叫补间器，用于完成从动画的一个起始到结束中间的补间部分
                image.startAnimation(anim);
            }
        });
    }

    /**
     * 设置屏幕亮度值
     *
     * @param dimAmount
     *            代表黑暗数量，也就是昏暗的多少，设置为0则代表完全明亮。 范围是0.0到1.0
     */
    private void setScreenBrightness(int dimAmount) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = dimAmount;
        window.setAttributes(lp);
    }

}
