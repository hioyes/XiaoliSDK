package com.xiaoli.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类
 * xiaokx
 * hioyes@qq.com
 * 2016-6-16
 */
public class SharedPreferencesUtils {
    private static Context mContext;
    private static SharedPreferencesUtils instence;
    private static SharedPreferences saveInfo;
    private static SharedPreferences.Editor saveEditor;
    public static String SHARE_NAME = "xiaoli";

    public static SharedPreferencesUtils getInstance(Context context){

        mContext =context;
        if (saveInfo == null && mContext != null) {
            saveInfo = mContext.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }
        if (instence == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (instence == null)
                    instence = new SharedPreferencesUtils();
            }
        }
        return instence;
    }

    /**
     * 保存字符串
     * param key 要保存的名称
     * param value 保存的值
     */
    public void saveString(String key,String value){
        saveEditor.putString(key,value).commit();
    }

    /**
     * 保存Boolean值
     * param key 要保存的名称
     * param value 保存的值
     */
    public void saveBoolean(String key,boolean value){
        saveEditor.putBoolean(key,value).commit();
    }
    /**
     * 保存int值
     * param key 要保存的名称
     * param value 保存的值
     */
    public void saveInteger(String key,int value){
        saveEditor.putInt(key,value).commit();
    }

    /**
     * 获取int值
     * param key 名称
     * param defValue 默认值
     * return
     */
    public int getInteger(String key,int defValue){
        return saveInfo.getInt(key, defValue);
    }
    /**
     * 获取Boolean值
     * param key 名称
     * param defValue 默认值
     * return
     */
    public boolean getBoolean(String key,boolean defValue){
        return saveInfo.getBoolean(key, defValue);
    }

    /**
     * 获取字符串
     * param key 名称
     * param defValue 默认值
     * return
     */
    public String getString(String key,String defValue){
        return saveInfo.getString(key, defValue);
    }

    /**
     * 删除
     * param key
     */
    public void delete(String key){
        saveEditor.remove(key).commit();
    }
}

