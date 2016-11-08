package com.xuange.phonesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;

/**
 * Created by Xuange on 2016-10-18.
 */
public class SettingItemView extends RelativeLayout {

    private CheckBox cb;
    private TextView update_des;
    private TextView update_titel;
    private String mDestitle;
    private String mDesoff;
    private String mDesopen;

    public SettingItemView(Context context) {
        //指向双参的
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        //指向三参的
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        //进行xml转换为view
        super(context, attrs, defStyleAttr);
        //inflate 最后一个参数要传递this，得到xml布局对象
        View.inflate(context, R.layout.setting_itme_view, this);
        /*等同于下面代码：
        View view = View.inflate(context, R.layout.setting_itme_view, null);
        this.addView(view);*/

        //获取自定义以及源生的属性的操作，写在调用它的构造方法中，本次是写在此处
        initAttrs(attrs);

        update_titel = (TextView) this.findViewById(R.id.update_titel);
        update_des = (TextView) this.findViewById(R.id.update_des);
        cb = (CheckBox) this.findViewById(R.id.update_cb);

        update_titel.setText(mDestitle);

    }

    /**
     * @param attrs 构造方法中的属性集合
     *              返回属性集合中自定义属性的属性值
     */
    private void initAttrs(AttributeSet attrs) {

        //attrs.getAttributeCount();获取属性个数
        //获取属性名称和属性值
        /*for (int i = 0; i < attrs.getAttributeCount(); i++) {
            Log.e("11111", attrs.getAttributeName(i) + "<><>" + attrs.getAttributeValue(i));
        }*/

        mDestitle = attrs.getAttributeValue(ConstantValue.SETTINGITEMVIEWNAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(ConstantValue.SETTINGITEMVIEWNAMESPACE, "desoff");
        mDesopen = attrs.getAttributeValue(ConstantValue.SETTINGITEMVIEWNAMESPACE, "desopen");

    }

    /**
     * 判断CheckBox是否别选择
     */
    public boolean isCheck() {
        //返回CheckBox的状态
        return cb.isChecked();
    }


    /**
     * @param isCheck 在点击CheckBox时，切换状态
     */
    public void setCheck(boolean isCheck) {
        cb.setChecked(isCheck);
        if (isCheck) {
            update_des.setText(mDesopen);
        } else {
            update_des.setText(mDesoff);
        }
    }

}
