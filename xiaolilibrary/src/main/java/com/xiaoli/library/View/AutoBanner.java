package com.xiaoli.library.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.xiaoli.library.C;
import com.xiaoli.library.R;

import java.util.List;

/**
 * 自动轮播Banner
 * xiaokx
 * hioyes@qq.com
 * 2016-7-19
 */
public class AutoBanner extends RelativeLayout{

    /**自定义属性**/
    private boolean mPointsVisibility;//指示点是否可见
    private int mPointsPosition;//指示点位置
    public static final int CENTER = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private Drawable mPointContainerBackground;//指示容器背景

    private ViewPager mViewPager;
    private LinearLayout llPonitContaniner;//指示器容器

    //本地图片资源
    private List<Integer> mListResid;
    //网络图片资源
    private List<String> mListUrl;
    //是否网络图片
    private boolean imgSourceNetwork = false;
    //图片总数
    private int imgTotal;
    //item单击监听
    private OnItemClickListener mOnItemClickListener;
    //当前页面位置
    private int mCurrentPositon;


    private static final int AUTO_PLAY = 1000;
    //是否可以自动轮播
    private boolean mAutoPlay = true;
    //是否正在轮播
    private boolean mIsAutoPlaying = false;
    //自动播放时间
    private int mAutoPalyTime = 5000;



    public AutoBanner(Context context){
        this(context,null);
    }

    public AutoBanner(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public AutoBanner(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        initAttrs(context,attrs);
        initView(context);
    }

    /**
     * 初始化视图
     * @param context
     */
    private void initView(Context context){
        /*** 为视图设置过滚动模式。
         * 有效的过滚动模式有 OVER_SCROLL_ALWAYS（默认值）、
         * OVER_SCROLL_IF_CONTENT_SCROLLS（视图内容大于容器时允许过滚动）、
         * OVER_SCROLL_NEVER.只有当视图可以滚动时，才可以设置视图的过滚动模式.**/
        setOverScrollMode(OVER_SCROLL_NEVER);

        //创建一个ViewPager并添加
        mViewPager = new ViewPager(context);
        addView(mViewPager,new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT));

        //创建指示器背景容器
        RelativeLayout rlPointContainer = new RelativeLayout(context);
        mPointContainerBackground = mPointContainerBackground==null?new ColorDrawable(Color.parseColor("#00aaaaaa")):mPointContainerBackground;
        if(Build.VERSION.SDK_INT>=16){
            rlPointContainer.setBackground(mPointContainerBackground);
        }else{
            rlPointContainer.setBackgroundDrawable(mPointContainerBackground);
        }
        rlPointContainer.setPadding(0,10,0,10);//内边距
        //设置指示器背景布局与位置
        LayoutParams rlPointContainerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        rlPointContainerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(rlPointContainer,rlPointContainerLayoutParams);

        //创建指示器容器
        llPonitContaniner = new LinearLayout(context);
        llPonitContaniner.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams llPonitContaninerLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        rlPointContainer.addView(llPonitContaniner,llPonitContaninerLayoutParams);
        //设置自定义属性值
        if(mPointsVisibility){
            if(llPonitContaniner!=null)llPonitContaniner.setVisibility(View.VISIBLE);
        }else{
            if(llPonitContaniner!=null)llPonitContaniner.setVisibility(View.GONE);
        }
        if(mPointsPosition == CENTER)llPonitContaninerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        else if(mPointsPosition == LEFT)llPonitContaninerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        else if(mPointsPosition == RIGHT)llPonitContaninerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    /**
     * 初始化自定义属性
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoBanner);
        mPointsVisibility = typedArray.getBoolean(R.styleable.AutoBanner_pointsVisibility,true);
        mPointsPosition = typedArray.getInt(R.styleable.AutoBanner_pointsPosition,CENTER);
        mPointContainerBackground = typedArray.getDrawable(R.styleable.AutoBanner_pointsContainerBackground);
        typedArray.recycle();

    }

    /**
     * 设置本地资源图片
     * @param list
     */
    public void setImagesByResid(List<Integer> list){
        if(list==null || list.size()==0)return;
        imgSourceNetwork = false;
        mListResid = list;
        imgTotal = list.size();
        initViewPager();
    }

    /**
     * 设置网络图片
     * @param list
     */
    public void setImagesUrl(List<String> list) {
        //加载网络图片
        imgSourceNetwork = true;
        mListUrl = list;
        imgTotal = list.size();
        initViewPager();
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager(){
        if(imgTotal>1){
            //图片数量大于1，添加指示点
            addPoints();
        }
        AutoPageAdapter adapter = new AutoPageAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        //跳转到首页
        mViewPager.setCurrentItem(1, false);
        if(imgTotal>1){
            startAutoPlay();
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPositon = position % (imgTotal + 2);
            switchToPoint(getRealPosition(mCurrentPositon));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                int current = mViewPager.getCurrentItem();
                int lastReal = mViewPager.getAdapter().getCount()-2;
                if (current == 0) {
                    mViewPager.setCurrentItem(lastReal, false);
                } else if (current == lastReal+1) {
                    mViewPager.setCurrentItem(1, false);
                }
            }
        }
    };

    /**
     * 添加指示点
     */
    private void addPoints() {
        llPonitContaniner.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        ImageView imageView;
        for (int i = 0; i < imgTotal; i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageResource(R.drawable.selector_autobanner_point);
            llPonitContaniner.addView(imageView);
        }
        switchToPoint(0);
    }

    /**
     * 切换指示器
     * @param currentPoint
     */
    private void switchToPoint(final int currentPoint) {
        for (int i = 0; i < llPonitContaniner.getChildCount(); i++) {
            llPonitContaniner.getChildAt(i).setEnabled(false);
        }
        llPonitContaniner.getChildAt(currentPoint).setEnabled(true);

    }

    /**
     * 适配器
     */
    private class AutoPageAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            if(imgTotal==1)return 1;
            return imgTotal+2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(getRealPosition(position));
                    }
                }
            });
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if(imgSourceNetwork){
                Picasso.with(getContext()).load(mListUrl.get(getRealPosition(position))).into(imageView);
            }else{
                imageView.setImageResource(mListResid.get(getRealPosition(position)));
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
            if (object != null)
                object = null;
        }
    }

    /**
     * 返回真实的位置
     * @param position
     * @return
     */
    private int getRealPosition(int position) {
        int realPosition = (position - 1) % imgTotal;
        if (realPosition < 0)
            realPosition += imgTotal;
        return realPosition;
    }

    /**
     * item点击事件监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    /**
     * 启动自动轮播
     */
    private void startAutoPlay(){
        if (mAutoPlay && !mIsAutoPlaying) {
            mIsAutoPlaying = true;
            mAutoPlayHandler.sendEmptyMessageDelayed(C.AUTO_BANNER_AUTO_PLAY, mAutoPalyTime);
        }
    }

    /**
     * 停止自动轮播
     */
    private void stopAutoPlay(){
        if (mAutoPlay && mIsAutoPlaying) {
            mIsAutoPlaying = false;
            mAutoPlayHandler.removeMessages(C.AUTO_BANNER_AUTO_PLAY);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mAutoPlay && imgTotal>1){
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //停止轮播
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    //自动轮播
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private Handler mAutoPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCurrentPositon++;
            mViewPager.setCurrentItem(mCurrentPositon);
            mAutoPlayHandler.sendEmptyMessageDelayed(C.AUTO_BANNER_AUTO_PLAY, mAutoPalyTime);
        }
    };
}
