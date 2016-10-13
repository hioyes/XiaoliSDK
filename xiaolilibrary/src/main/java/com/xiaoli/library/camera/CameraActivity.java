package com.xiaoli.library.camera;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xiaoli.library.C;
import com.xiaoli.library.R;
import com.xiaoli.library.utils.DateUtils;
import com.xiaoli.library.utils.FileUtils;

/**
 * Activity中使用自定义相机
 * xiaokx
 * hioyes@qq.com
 * 2016-7-29
 */
public class CameraActivity extends Activity implements CameraPreview.OnCameraStatusListener{

    private String TAG = "CameraActivity";
    private CameraPreview mCameraPreview;//自定义相机
    private ImageView mIvPhotograph;//拍照按钮
    private FocusView mFocusView;//聚焦View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_camera);
        mCameraPreview = (CameraPreview)findViewById(R.id.mCameraPreview);
//        mCameraPreview.setRate(1.33f);//比率设置
//        mCameraPreview.setFlashMode(Camera.Parameters.FLASH_MODE_ON);//闪光灯设置
        mIvPhotograph = (ImageView)findViewById(R.id.mIvPhotograph);
        mFocusView = (FocusView)findViewById(R.id.mFocusView);
        mCameraPreview.setOnCameraStatusListener(this);
        mCameraPreview.setFocusView(mFocusView);

    }


    /**
     * 执行拍照
     * @param view
     */
    public void doPhotograph(View view){
        if(mCameraPreview != null) {
            mCameraPreview.takePicture();
        }
    }

    @Override
    public void onCameraStopped(byte[] data) {
        Log.e(TAG, "in...onCameraStopped");
        // 创建图像
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap = isMarket(bitmap);
        // 图像名称
        long dateTaken = System.currentTimeMillis();
        String filename = DateUtils.toString(dateTaken,"yyyy-MM-dd-hh-mm-ss") + ".jpg";
        // 存储图像（PATH目录）
        Uri source = FileUtils.saveImage(getContentResolver(), filename, dateTaken, C.ROOT_CATALOG+"XiaoliMedia/",
                filename, bitmap, data);
        Log.e(TAG,"photo path is ->"+FileUtils.getRealFilePath(getApplicationContext(),source));
    }

    private Bitmap isMarket(Bitmap bitmap){
        String str = "老萧写的相机";
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        System.out.println("宽"+width+"高"+height);
        Bitmap icon = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //建立一个空的BItMap
        Canvas canvas = new Canvas(icon);//初始化画布绘制的图像到icon上

        Paint photoPaint = new Paint(); //建立画笔
        photoPaint.setDither(true); //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);//过滤一些

        Rect src = new Rect(0, 0, width, height);//创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, height);//创建一个指定的新矩形的坐标
        canvas.drawBitmap(bitmap, src, dst, photoPaint);//将photo 缩放或则扩大到 dst使用的填充区photoPaint

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(26.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.RED);//采用的颜色
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
        Rect rect = new Rect();
        textPaint.getTextBounds(str,0,str.length(),rect);
        canvas.drawText(str, width-rect.width()-20, height-rect.height()-20, textPaint);//绘制上去字，开始未知x,y采用那只笔绘制
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }
}
