package com.xiaoli.library.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoli.library.C;
import com.xiaoli.library.R;
import com.xiaoli.library.View.MyDialog;


/**
 * 内容提示工具类
 *  xiaokx
 *  hioyes@qq.com
 *  2014-11-6
 */
public class PromptUtils {

    private static Toast toast = null;
    private static MyDialog myDialog=null;

    /**
     * 自定义信息提示
     * param msg
     */
    public synchronized  static void showMessage(final String msg){
        if (C.mCurrentActivity==null)return ;
        myDialog = new MyDialog(C.mCurrentActivity, R.layout.dlg_common_info,R.style.MyDialog);
        myDialog.setDuration(2*1000);
        myDialog.setResetView(new MyDialog.ResetView() {
            @Override
            public void initView(Dialog dialog) {
                TextView tv = (TextView) dialog.findViewById(R.id.mCommonInfo);
                tv.setText(msg);
            }
        });
        myDialog.show(0,0);
    }

    /**
     *信息提示
     * param tip
     */
    public synchronized static void showToast(int tip) {
        showToast(C.mCurrentActivity,tip);
    }

    /**
     * 信息提示
     * @param context
     * @param tip
     */
    public synchronized static void showToast(Context context,int tip) {
        if (C.mCurrentActivity==null)return ;
        if(toast==null){
            toast = Toast.makeText(context, tip, Toast.LENGTH_SHORT);
        }else{
            toast.setText(tip);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
//		Toast.makeText(BuyerApplication.getInstance(), tip, Toast.LENGTH_SHORT)
//				.show();
    }

    /**
     *信息提示
     * param tipStr
     *            提示内容
     */
    public synchronized static void showToast(String tipStr){
        showToast(C.mCurrentActivity,tipStr);
    }

    /**
     * 信息提示
     * @param context
     * @param tipStr
     */
    public synchronized static void showToast(Context context,String tipStr) {
        if (context==null)return ;
        if (StringUtils.isEmpty(tipStr))return ;
        if(toast==null){
            toast = Toast.makeText(context, tipStr, Toast.LENGTH_SHORT);
        }else{
            toast.setText(tipStr);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     *
     * 显示toast，自己定义显示长短。
     * param1:activity  传入context
     * param2:word   我们需要显示的toast的内容
     * param3:time length  long类型，我们传入的时间长度（如500）
     */
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }


    /**
     * 自定义文本内入，例如：
     *
     * param view
     */
    public synchronized static void showToast(View view) {
        if(C.mCurrentActivity==null)return;
        if(toast==null){
            toast = Toast.makeText(C.mCurrentActivity, "",Toast.LENGTH_SHORT);
        }else{
            toast.setText("");
        }
        toast.setGravity(Gravity.CENTER, toast.getXOffset(), toast.getYOffset());
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.addView(view, 0);
        toast.show();
    }

    /**
     *
     * param activity
     * param titleStr
     *            提示标签
     * param messageStr
     *            提示内容
     */
    public static void showDialog(final Activity activity, String titleStr,
                                  String messageStr) {
        new AlertDialog.Builder(activity).setTitle(titleStr)
                .setMessage(messageStr)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
