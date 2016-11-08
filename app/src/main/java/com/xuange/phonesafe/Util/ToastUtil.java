package com.xuange.phonesafe.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Xuange on 2016-10-17.
 * 提示类
 */
public class ToastUtil {

    public static void show(Context context, String text) {

        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }

}
