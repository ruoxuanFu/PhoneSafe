package com.xuange.phonesafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;

public class MobileSafeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView safe_iv_locked;
    private Button safe_bt_reset;
    private Intent intent;
    private LinearLayout safe_linear_pawer;
    private TextView safe_tv_warming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe);

        initView();

        intent = new Intent(MobileSafeActivity.this, MobileSafeSetup1Activity.class);

        boolean setup_over = SpUtil.getBoolean(MobileSafeActivity.this, ConstantValue.SETUP_OVER, false);
        boolean open_safe = SpUtil.getBoolean(MobileSafeActivity.this, ConstantValue.OPEN_SAFE, false);

        if (setup_over) {
            //密码正确且安全设置完成，停留在这个页面
        } else {
            //否则，请先完成安全设置
            startActivity(intent);
            finish();
        }

        if (open_safe) {
            //把“防盗保护”的配图换为对号,显示功能介绍，隐藏默认图片
            safe_iv_locked.setBackgroundResource(R.drawable.tick);
            safe_linear_pawer.setVisibility(View.VISIBLE);
            safe_tv_warming.setVisibility(View.GONE);
        } else {
            //显示默认图片，隐藏功能介绍
            safe_linear_pawer.setVisibility(View.GONE);
            safe_tv_warming.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        safe_iv_locked = (ImageView) findViewById(R.id.safe_iv_locked);
        safe_bt_reset = (Button) findViewById(R.id.safe_bt_reset);
        safe_linear_pawer = (LinearLayout) findViewById(R.id.safe_linear_pawer);
        safe_tv_warming = (TextView) findViewById(R.id.safe_tv_warming);

        safe_bt_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safe_bt_reset:

                startActivity(intent);

                finish();

                break;
        }
    }
}
