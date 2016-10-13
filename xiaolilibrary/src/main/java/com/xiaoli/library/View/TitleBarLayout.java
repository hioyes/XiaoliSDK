package com.xiaoli.library.View;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoli.library.R;
import com.xiaoli.library.utils.StringUtils;

/**
 * 自定义标题栏
 * xiaokx
 * hioyes@qq.com
 * 2016-7-13
 */
public class TitleBarLayout extends RelativeLayout{

    /** 自定义属性 **/
    private int titleBarBgColor;//标题背景颜色
    private int titleBarTextColor;//标题文字颜色
    private float titleBarTitleHeight;//标题高度
    private String titleBarTitle;//标题文字
    private int titleBarBackgroundLeft;//标题左边Background
    private int titleBarBackgroundRight;//标题右边Background
    private boolean titleBarLeftIconShow;//是否显示左边icon
    private int titleBarLeftIcon;//设置左边icon
    private boolean titleBarRightIconShow;//是否显示右边icon
    private int titleBarRightIcon;//设置右边icon

    /** 包含控件 **/
    private TextView mTvLeft;
    private ImageView mIvLeft;
    private TextView mTvCenter;
    private TextView mTvRight;
    private ImageView mIvRight;


    public TitleBarLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        if(context instanceof Activity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
//                ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }

        //获取属性值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarLayout);
        titleBarBgColor = a.getColor(R.styleable.TitleBarLayout_titleBarBgColor, getResources().getColor(R.color.mainBlue));
        titleBarTextColor = a.getColor(R.styleable.TitleBarLayout_titleBarTextColor, Color.WHITE);
        titleBarTitleHeight = a.getDimension(R.styleable.TitleBarLayout_titleBarTitleHeight,getResources().getDimension(R.dimen.defalut_titlebar_height));
        titleBarTitle = a.getString(R.styleable.TitleBarLayout_titleBarTitle);
        titleBarBackgroundLeft = a.getResourceId(R.styleable.TitleBarLayout_titleBarBackgroundLeft,R.drawable.selector_common_btn_bg);
        titleBarBackgroundRight = a.getResourceId(R.styleable.TitleBarLayout_titleBarBackgroundRight,R.drawable.selector_common_btn_bg);
        titleBarLeftIconShow = a.getBoolean(R.styleable.TitleBarLayout_titleBarLeftIconShow,true);
        titleBarLeftIcon = a.getResourceId(R.styleable.TitleBarLayout_titleBarLeftIcon,R.mipmap.btn_back);
        titleBarRightIconShow = a.getBoolean(R.styleable.TitleBarLayout_titleBarRightIconShow,false);
        titleBarRightIcon =  a.getResourceId(R.styleable.TitleBarLayout_titleBarRightIcon,R.mipmap.btn_back);
        a.recycle();

        setFitsSystemWindows(true);//设置布局调整时是否考虑系统窗口（如状态栏）
        setClipToPadding(true);
        setBackgroundColor(titleBarBgColor);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)titleBarTitleHeight);
        View titleBarView = LayoutInflater.from(context).inflate(R.layout.common_title_bar, null, false);
        titleBarView.setBackgroundColor(titleBarBgColor);
        mTvLeft = (TextView)titleBarView.findViewById(R.id.mTvLeft);
        mIvLeft = (ImageView)titleBarView.findViewById(R.id.mIvLeft);
        mTvCenter = (TextView)titleBarView.findViewById(R.id.mTvCenter);
        mTvRight = (TextView)titleBarView.findViewById(R.id.mTvRight);
        mIvRight = (ImageView)titleBarView.findViewById(R.id.mIvRight);

        if(titleBarLeftIconShow){
            mIvLeft.setVisibility(View.VISIBLE);
            mIvLeft.setImageDrawable(getResources().getDrawable(titleBarLeftIcon));
        }

        if(titleBarRightIconShow){
            mIvRight.setVisibility(View.VISIBLE);
            mIvRight.setImageDrawable(getResources().getDrawable(titleBarRightIcon));
        }


        mTvLeft.setTextColor(titleBarTextColor);
        mTvLeft.setBackgroundResource(titleBarBackgroundLeft);
        mIvLeft.setBackgroundResource(titleBarBackgroundLeft);

        mTvRight.setTextColor(titleBarTextColor);
        mTvRight.setBackgroundResource(titleBarBackgroundRight);
        mIvRight.setBackgroundResource(titleBarBackgroundRight);

        mTvCenter.setTextColor(titleBarTextColor);
        mTvCenter.setText(titleBarTitle);
        addView(titleBarView,layoutParams);


    }

    /**
     * 设置左边文本
     * 空的时候不显示
     * @param text
     */
    public void setLeftText(String text){
        if(StringUtils.isEmpty(text)){
            mTvLeft.setVisibility(View.GONE);
        }else{
            mTvLeft.setVisibility(View.VISIBLE);
            mTvLeft.setText(text);
        }
    }

    /**
     * 设置右边文本
     * 空的时候不显示
     * @param text
     */
    public void setRightText(String text){
        if(StringUtils.isEmpty(text)){
            mTvRight.setVisibility(View.GONE);
        }else{
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(text);
        }
    }

    /**
     * 设置中间文本
     * 空的时候不显示
     * @param text
     */
    public void setCenterText(String text){
        if(StringUtils.isEmpty(text)){
            mTvCenter.setVisibility(View.GONE);
        }else{
            mTvCenter.setVisibility(View.VISIBLE);
            mTvCenter.setText(text);
        }
    }

    /**
     * 设置左边图片
     * @param visibility
     */
    public void setLeftImage(boolean visibility){
        this.setLeftImage(visibility,0);
    }

    /**
     * 设置左边图片
     * visibility false 不显示
     * @param visibility
     * @param resid
     */
    public void setLeftImage(boolean visibility,int resid){
        if(visibility){
            mIvLeft.setVisibility(View.VISIBLE);
            if(resid>0){
                mIvLeft.setImageResource(resid);
            }
        }else{
            mIvLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右边图片
     * @param visibility
     */
    public void setRightImage(boolean visibility){
        this.setRightImage(visibility,0);
    }

    /**
     * 设置右边图片
     * visibility false 不显示
     * @param visibility
     * @param resid
     */
    public void setRightImage(boolean visibility,int resid){
        if(visibility){
            mIvRight.setVisibility(View.VISIBLE);
            if(resid>0){
                mIvRight.setImageResource(resid);
            }
        }else{
            mIvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 左边点击事件
     *
     * @param listener
     */
    public void setLeftOnClickListener(View.OnClickListener listener) {
        if(mTvLeft.getVisibility() == View.VISIBLE){
            mTvLeft.setOnClickListener(listener);
        }
        if(mIvLeft.getVisibility() == View.VISIBLE){
            mIvLeft.setOnClickListener(listener);
        }
    }

    /**
     * 右边点击事件
     * @param listener
     */
    public void setRightOnClickListener(View.OnClickListener listener) {
        if(mTvRight.getVisibility() == View.VISIBLE){
            mTvRight.setOnClickListener(listener);
        }
        if(mIvRight.getVisibility() == View.VISIBLE){
            mIvRight.setOnClickListener(listener);
        }
    }

}
