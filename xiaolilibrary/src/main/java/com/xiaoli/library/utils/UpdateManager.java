package com.xiaoli.library.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoli.library.C;
import com.xiaoli.library.R;
import com.xiaoli.library.model.Update;
import com.xiaoli.library.net.InternetUtils;
import com.xiaoli.library.ui.NullActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本更新工具类
 * {"appname":"路宝买家","downurl":"http://app.lubaocar.com/android/buyer/production/buyer_18.apk","verName":"V_1.1.2","verCode":"18","updateContent":[{"content":"1、增加仲裁结果返买受人保证金功能"},{"content":"2、等待竞价状态增加“有投标价”、“可成交”标签"}],"preVersionInfo":[{"content":"1、车辆列表增加“可成交”“有投标价”的标识"},{"content":"2、限制投标车取消关注"}],"updateTitle":"检测到更新","size":"9.71M"}
 * xiaokx
 * hioyes@qq.com
 */
public class UpdateManager {
    private String TAG = "UpdateManager";
    public ProgressDialog pBar;
    private static Context context;
    private static UpdateManager instence;
    private int newVerCode;
    private static String packageName = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!InternetUtils.checkNet(context)) {
                        PromptUtils.showToast("网络不给力，请检查网络");
                        pBar.cancel();
                        break;
                    }
                    pBar.setProgress(msg.arg1);
                    break;
            }
        }

        ;
    };

    public static UpdateManager getInstance() {
        context = C.mCurrentActivity;
        packageName = C.PACKAGE_NAME;
        if (instence == null) {
            synchronized (UpdateManager.class) {
                if (instence == null)
                    instence = new UpdateManager();
            }
        }
        return instence;
    }

    private UpdateManager() {
    }


    /**
     * 检查是否需要更新
     */
    public void checkIsNeedUpdate(Update respUpdate) {
        if(packageName==null){
            Log.e(TAG,"package is null");
            return;
        }
        if (respUpdate == null) return;
        this.newVerCode = respUpdate.getVerCode();
        int currentVercode = getLocalVerCode();
        if (newVerCode > currentVercode) {//服务器code>当前code
            showUpdataDialog(respUpdate.getVerName(), respUpdate.getInfoByUpdateContent(), respUpdate.getDownurl());
        }
    }

    /**
     * 显示更新对话框
     *
     * param title 标题
     * param msg   内容
     * param url   更新地址 包括文件名
     */
    public void showUpdataDialog(String title, String msg, final String url) {
        View view = View.inflate(context, C.DLG_UPDATE, null);
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        TextView mTvUpdateTitle = (TextView) view.findViewById(R.id.mTvUpdateTitle);
        mTvUpdateTitle.setText(title);
        TextView mTvUpdateMsg = (TextView) view.findViewById(R.id.mTvUpdateMsg);
        mTvUpdateMsg.setText(msg);
        Button positiveBtn = (Button) view.findViewById(R.id.mBtnUpdateOk);
        Button negativeBtn = (Button) view.findViewById(R.id.mBtnUpdateCancle);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
                pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pBar.setTitle("软件更新");
                pBar.setMessage("正在下载新版本，请稍候…");
                pBar.setIndeterminate(false);
                pBar.setCancelable(false);
                downFile(url);

                if (context != null) {
                    dialog.dismiss();
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击退出应用
//                android.os.Process.killProcess(android.os.Process.myPid());
                if (context != null) {
                    dialog.dismiss();
                }
                C.exit(context);
            }
        });
        dialog.show();
    }

    /**
     * 下载文件
     *
     * param fileUrl
     */
    void downFile(final String fileUrl) {
        if (!InternetUtils.checkNet(context)) {
            PromptUtils.showToast("网络不给力，请检查网络");
            pBar.cancel();
            return;
        }

        pBar.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"), fileUrl.length());
                    //创建按一个URL实例
                    URL url = new URL(fileUrl);
                    //创建一个HttpURLConnection的链接对象
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    //获取所下载文件的InputStream对象
                    InputStream inputStream = httpConn.getInputStream();
                    int length = httpConn.getContentLength();
                    FileOutputStream fileOutputStream = null;
                    if (inputStream != null) {
                        File file = new File(Environment.getExternalStorageDirectory(), fileName);
                        fileOutputStream = new FileOutputStream(file);

                        byte[] buf = new byte[1024 * 128];
                        int ch = -1;
                        int count = 0;
                        while ((ch = inputStream.read(buf)) != -1) {
                            count += ch;
                            fileOutputStream.write(buf, 0, ch);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.arg1 = (int) (count * 100 / length);
                            handler.sendMessage(msg);
                            if (length > 0) {
                            }
                        }

                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    /**
     * 下载完毕
     */
    void down(final String fileName) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                pBar.cancel();
                update(fileName);
            }
        });

    }

    /**
     * 安装更新包
     */
    void update(String fileName) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
            context = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public int getLocalVerCode() {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (RuntimeException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return verCode;
    }

    /**
     * 获取当前版本名称
     *
     * @return
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (RuntimeException e) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return verName;

    }
}
