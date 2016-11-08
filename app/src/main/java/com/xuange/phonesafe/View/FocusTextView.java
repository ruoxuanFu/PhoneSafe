package com.xuange.phonesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Xuange on 2016-10-18.
 * 能够一直获取焦点的自定义TextView
 */
public class FocusTextView extends TextView {

    public FocusTextView(Context context) {
        //使用在通过代码创建的控件，例如 new TextView(context);
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        //通过布局文件进行创建的控件
        super(context, attrs);
    }

    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        //通过布局文件进行创建的控件（在布局文件中有 style="@style/ 样式的）
        super(context, attrs, defStyleAttr);
    }

    //重写获取焦点的方法

    @Override
    public boolean isFocused() {
        return true;
    }
}
