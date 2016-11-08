package com.xuange.phonesafe.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xuange.phonesafe.Activity.MobileSafeSetup2Activity;

/**
 * Created by Xuange on 2016-10-18.
 */
public class SpUtil {

    private static SharedPreferences sp;

    /**
     * @param context   上下文对象
     * @param key       存储的对象的key值
     * @param value     存储的对象的value值
     */
    //读
    public static void putBoolean(Context context, String key, boolean value) {
        //参数 存储文件名称  读写方式
        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        }

        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * @param context   上下文对象
     * @param key       存储的对象的key值
     * @param defValue  读取的默认值
     * @return          返回读取到的值
     */
    //写
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        //参数 存储文件名称  读写方式
        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        }

        return sp.getBoolean(key, defValue);
    }

    /**
     * @param context   上下文对象
     * @param key       存储的对象的key值
     * @param value     存储的对象的value值
     */
    //读
    public static void putString(Context context, String key, String value) {
        //参数 存储文件名称  读写方式
        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        }

        sp.edit().putString(key, value).commit();
    }

    /**
     * @param context   上下文对象
     * @param key       存储的对象的key值
     * @param defValue  读取的默认值
     * @return          返回读取到的值
     */
    //写
    public static String getString(Context context, String key, String defValue) {
        //参数 存储文件名称  读写方式
        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        }

        return sp.getString(key, defValue);
    }

    /**
     * 从Sp中移除一个节点
     * @param context 上下文环境
     * @param key      需要移除节点的名称
     */
    public static void remove(Context context, String key) {

        if (sp == null) {

            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        }

        sp.edit().remove(key).commit();

    }
}
