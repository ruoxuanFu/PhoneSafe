package com.xuange.phonesafe.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.View.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    private void initUpdate() {

        final SettingItemView update = (SettingItemView) findViewById(R.id.set_update);

        final SettingItemView calladdress = (SettingItemView) findViewById(R.id.set_calladdress);

        //获取已有的开关状态，用作显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //根据已经获取的开关状态，让CheckBox做出相应的状态
        update.setCheck(open_update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取之前的选择状态
                boolean updateIsCheck = update.isCheck();
                update.setCheck(!updateIsCheck);
                //将取反后的状态存储到相应的Sp文件中
                SpUtil.putBoolean(SettingActivity.this, ConstantValue.OPEN_UPDATE, !updateIsCheck);
            }
        });

        calladdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updateIsCheck = calladdress.isCheck();
                calladdress.setCheck(!updateIsCheck);
            }
        });

    }
}
