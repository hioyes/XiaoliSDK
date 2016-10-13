package com.xiaoli.library.camera;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.xiaoli.library.utils.CurrencyUtils;
import com.xiaoli.library.utils.DensityUtils;
import com.xiaoli.library.utils.MobileUtils;
import com.xiaoli.library.utils.PromptUtils;
import com.xiaoli.library.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 自定义相机
 * http://www.cnblogs.com/xiaoxiao-study/p/867d2ad9206c8600186c90690f1e7965.html
 * xiaokx
 * hioyes@qq.com
 * 2016-7-29
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.AutoFocusCallback{

    private String TAG = "CameraPreview";
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private FocusView mFocusView;
    private ToneGenerator mToneGenerator; // 铃声

    private int viewWidth = 0;
    private int viewHeight = 0;

    /**
     * 比例
     * 4:3 ~~1.3333
     * 16:9~~~1.7777
     */
    private float mRate = 1.77f;

    /**
     * 闪光灯参数
     * Camera.Parameters.FLASH_MODE_TORCH 一直开启
     * Camera.Parameters.FLASH_MODE_ON 拍照时开启
     * Camera.Parameters.FLASH_MODE_OFF 默认关闭
     */
    private String mFlashMode = Camera.Parameters.FLASH_MODE_OFF;

    /** 监听接口 */
    private OnCameraStatusListener listener;


    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public CameraPreview(Context context, AttributeSet attrs){
        super(context,attrs);
        surfaceHolder = getHolder();// 获得句柄
        if (Build.VERSION.SDK_INT < 11)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置SurfaceHolder对象的类型
        surfaceHolder.addCallback(this);//注册拍照回调监听事件
        setOnTouchListener(onTouchListener);
    }


    /**
     * 触屏监听，点击显示焦点区域
     */
    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int width = mFocusView.getWidth();
                int height = mFocusView.getHeight();
                mFocusView.setX(event.getX() - (width / 2));
                mFocusView.setY(event.getY() - (height / 2));
                mFocusView.beginFocus();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                focusOnTouch(event);
            }
            return true;
        }
    };


    /**
     * 设置焦点和测光区域
     *
     * @param event
     */
    public void focusOnTouch(MotionEvent event) {

        int[] location = new int[2];
        RelativeLayout relativeLayout = (RelativeLayout)getParent();
        relativeLayout.getLocationOnScreen(location);

        Rect focusRect = MobileUtils.calculateTapArea(mFocusView.getWidth(),
                mFocusView.getHeight(), 1f, event.getRawX(), event.getRawY(),
                location[0], location[0] + relativeLayout.getWidth(), location[1],
                location[1] + relativeLayout.getHeight());
        Rect meteringRect = MobileUtils.calculateTapArea(mFocusView.getWidth(),
                mFocusView.getHeight(), 1.5f, event.getRawX(), event.getRawY(),
                location[0], location[0] + relativeLayout.getWidth(), location[1],
                location[1] + relativeLayout.getHeight());

        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
            focusAreas.add(new Camera.Area(focusRect, 1000));

            parameters.setFocusAreas(focusAreas);
        }

        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
            meteringAreas.add(new Camera.Area(meteringRect, 1000));

            parameters.setMeteringAreas(meteringAreas);
        }

        try {
            camera.setParameters(parameters);
        } catch (Exception e) {
        }
        camera.autoFocus(this);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    /**
     * 在surface创建时激发
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG,"in...surfaceCreated");
        if(!MobileUtils.checkCameraHardware(getContext())){
            PromptUtils.showToast(getContext(),"摄像头打开失败！");
        }
        // 获得Camera对象
        camera = getCameraInstance();
        try {
            // 设置用于显示拍照摄像的SurfaceHolder对象
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
            camera.release();
            camera = null;
        }
        updateCameraParameters();
        if(camera!=null){
            camera.startPreview();
        }
        setFocus();
    }

    /**
     *  在surface的大小发生改变时激发
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG,"in...surfaceChanged");
        // 修改前停止预览
        try {
            camera.stopPreview();
        } catch (Exception e){
        }
        updateCameraParameters();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e){
            Log.e(TAG, "Error starting camera preview: " + e.getMessage());
        }
        setFocus();
    }

    /**
     * 在surface被摧毁前调用，该函数被调用后就不能继续使用Surface了，一般在该函数中清理使用的资源
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG,"in...surfaceDestroyed");
        camera.release();
        camera = null;
    }

    /**
     * 获取摄像头实例
     * @return
     */
    private Camera getCameraInstance(){
        Camera c = null;
        try {
            int cameraCount = 0;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            for (int i=0;i<cameraCount;i++){
                Camera.getCameraInfo(i,cameraInfo);
                // 代表摄像头的方位，目前有定义值分别为:CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        c = Camera.open(i);   //打开后置摄像头
                    } catch (RuntimeException e) {
                       PromptUtils.showToast(getContext(),"摄像头打开失败！");
                    }
                }
            }
            if (c == null) {
                c = Camera.open(0); //没有打开摄像头，尝试获取一个实例
            }
        }catch (Exception e){
            PromptUtils.showToast(getContext(),"摄像头打开失败！");
        }
        return c;
    }

    /**
     * 修改相机参数
     */
    private void updateCameraParameters() {
        if (camera != null) {
            Camera.Parameters p = camera.getParameters();

            setParameters(p);

            try {
                camera.setParameters(p);
            } catch (Exception e) {
                Camera.Size previewSize = findBestPictureSize(p,mRate);
                p.setPreviewSize(previewSize.width, previewSize.height);
                p.setPictureSize(previewSize.width, previewSize.height);
                Log.e(TAG,"updateCameraParameters Exception w->"+previewSize.width+"---h->"+previewSize.height);
                camera.setParameters(p);
            }
        }
    }

    /**
     * 设置相机参数
     * @param p
     */
    private void setParameters(Camera.Parameters p) {
        List<String> focusModes = p.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//连续对焦
        }else{
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦
        }
//        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//一值开启闪光灯
        p.setFlashMode(mFlashMode);//闪光灯设置
        long time = new Date().getTime();
        p.setGpsTimestamp(time);
        p.setPictureFormat(PixelFormat.JPEG);// 设置照片格式
        p.set("jpeg-quality", 100);//照片质量
//        String board = android.os.Build.BRAND; //Xiaomi,Meizu
        //p.setPreviewFrameRate(3);//每秒3帧 某些机型（红米note2,魅族MX5）不支持
//        Camera.Size previewSize = findPreviewSizeByScreen(p);
        Camera.Size previewSize = findBestPictureSize(p,mRate);
        p.setPreviewSize(previewSize.width, previewSize.height);
        p.setPictureSize(previewSize.width, previewSize.height);
        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(90);
            p.setRotation(90);
        }
    }

    /**
     * 获取最适合的图片尺寸
     * @param parameters
     * @param rate 比率
     * @return 没有匹配的则直接返回屏幕大小
     */
    private Camera.Size findBestPictureSize(Camera.Parameters parameters,float rate){
        Camera.Size defaultSize = camera.new Size(DensityUtils.getDisplayMetrics(getContext()).widthPixels,DensityUtils.getDisplayMetrics(getContext()).heightPixels);
        // 系统支持的所有预览分辨率
        String previewSizeValueString = parameters.get("preview-size-values");
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }
        if (previewSizeValueString == null) { // 有些手机例如m9获取不到支持的预览大小 就直接返回屏幕大小
            return defaultSize;
        }

        int width = 0;
        String[] resolutionRatio = previewSizeValueString.split(",");
        for (String previewSize : resolutionRatio) {
            String[] arry = previewSize.split("x");
            int w = StringUtils.toInt(arry[0]);
            int h = StringUtils.toInt(arry[1]);
            Camera.Size tempSize = camera.new Size(w, h);
            if(w>width && equalRate(tempSize,rate)){
                width = w;
                defaultSize = tempSize;
            }
        }
        return defaultSize;
    }

    /**
     * 按比例获取最大支持预览图片
     * @param parameters
     * @return
     */
    private Camera.Size findPreviewSizeByScreen(Camera.Parameters parameters) {
        Camera.Size mSize = null;
        int width = 0;
        List<Camera.Size> list = camera.getParameters().getSupportedPictureSizes();
        for (Camera.Size size:list){
            if(size.width>width && equalRate(size, mRate)){
                mSize = size;
                width = size.width;
            }
        }

        if(mSize==null){
            if (viewWidth != 0 && viewHeight != 0) {
                return camera.new Size(Math.max(viewWidth, viewHeight),
                        Math.min(viewWidth, viewHeight));
            } else {
                return camera.new Size(DensityUtils.getDisplayMetrics(getContext()).heightPixels,
                        DensityUtils.getDisplayMetrics(getContext()).widthPixels);
            }
        }
        return mSize;
    }

    /**
     * 根据比率判断图片比例
     * 4:3 ~~1.3333
     * 16:9~~~1.7777
     * @param s
     * @param rate 比率
     * @return
     */
    public boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"in...onMeasure");
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY));
    }

    /**
     * 进行拍照，并将拍摄的照片传入PictureCallback接口的onPictureTaken方法
     */
    public void takePicture() {
        if (camera != null) {
            try {
                camera.takePicture(shutterCallback, null, pictureCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 快门按下的时候onShutter()被回调拍照声音
     */
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            if (mToneGenerator == null) {
                // 发出提示用户的声音
                mToneGenerator = new ToneGenerator(AudioManager.AUDIOFOCUS_REQUEST_GRANTED,ToneGenerator.MIN_VOLUME);
            }
            mToneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
        }
    };


    /**
     * 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        // 该方法用于处理拍摄后的照片数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 停止照片拍摄
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }
            // 调用结束事件
            if (null != listener) {
                listener.onCameraStopped(data);
            }
        }
    };

    /**
     * 相机拍照监听接口
     */
    public interface OnCameraStatusListener {
        // 相机拍照结束事件
        void onCameraStopped(byte[] data);
    }

    /**
     * 设置拍照监听事件
     * @param listener
     */
    public void setOnCameraStatusListener(OnCameraStatusListener listener) {
        this.listener = listener;
    }

    /**
     * 设置聚焦的图片
     * @param focusView
     */
    public void setFocusView(FocusView focusView) {
        this.mFocusView = focusView;
    }

    /**
     * 设置自动聚焦，并且聚焦的圈圈显示在屏幕中间位置
     */
    public void setFocus() {
        if(!mFocusView.isFocusing()) {
            try {
                camera.autoFocus(this);
                mFocusView.setX((DensityUtils.getDisplayMetrics(this.getContext()).widthPixels-mFocusView.getWidth()) / 2);
                mFocusView.setY((DensityUtils.getDisplayMetrics(this.getContext()).heightPixels-mFocusView.getHeight()) / 2);
                mFocusView.beginFocus();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 设置比率,保留两位小数即可
     * 4:3 = 4/3~~1.3333
     * 16:9 = 16/9~~~~1.7777
     * @param rate
     */
    public void setRate(float rate){
        mRate = rate;
    }

    /**
     * 设置闪光灯效果，具体参数参考Camera.Parameters，常用如：
     * Camera.Parameters.FLASH_MODE_TORCH 一直开启
     * Camera.Parameters.FLASH_MODE_ON 拍照时开启
     * Camera.Parameters.FLASH_MODE_OFF 默认关闭
     * @param flashMode
     */
    public void setFlashMode(String flashMode){
        mFlashMode = flashMode;
    }
}
