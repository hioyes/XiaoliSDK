package com.xiaoli.library.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Google旗下的Json与Java映射工具类
 * xiaokx
 * hioyes@qq.com
 * 2014-11-6
 */
public class GsonUtils {
    private static Gson gson = null;

    /**
     * 获取Gson对象
     * return
     */
    public Gson getInstance() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    /**
     * 返回一个对象
     * param json
     * param clazz
     * param <T>
     * return
     */
    public static <T> T toObject(String json, Class clazz) {
        try {
            return (T) new GsonUtils().getInstance().fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回一个对象
     * param json
     * param type
     * param <T>
     * return
     */
    public static <T> T toObject(String json, Type type) {
        try {
            return (T) new GsonUtils().getInstance().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Map参数转成json格式传输
     * <p>
     * param map
     * return
     */
    public static String toJson(Map map) {
        return new GsonUtils().getInstance().toJson(map);
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     * <p>
     * param json
     * param type  new TypeToken<List<yourbean>>(){}.getType()
     * return
     */
    public static List<?> toList(String json, Type type) {
        try {
            Gson gson = new Gson();
            List<?> list = gson.fromJson(json, type);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将Object转成Json
     * param obj
     * return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
