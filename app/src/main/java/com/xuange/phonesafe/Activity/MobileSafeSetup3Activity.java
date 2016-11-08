package com.xuange.phonesafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.Util.ToastUtil;

public class MobileSafeSetup3Activity extends BaseSetupActivity implements View.OnClickListener {

    private EditText step_3_safephone;
    private Button step_3_safenumber;
    private Button step_3_bt_pre;
    private Button step_3_bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe_setup3);
        initView();

        //从SP中拿到
        String contact_phone = SpUtil.getString(MobileSafeSetup3Activity.this, ConstantValue.SAFE_PHONE, "");
        step_3_safephone.setText(contact_phone);
    }

    private void initView() {
        step_3_safephone = (EditText) findViewById(R.id.step_3_safephone);
        step_3_safenumber = (Button) findViewById(R.id.step_3_safenumber);
        step_3_bt_pre = (Button) findViewById(R.id.step_3_bt_pre);
        step_3_bt_next = (Button) findViewById(R.id.step_3_bt_next);

        step_3_safenumber.setOnClickListener(this);
        step_3_bt_pre.setOnClickListener(this);
        step_3_bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_3_safenumber:
                //从联系人列表中获取电话
                Intent three2contactlist = new Intent(MobileSafeSetup3Activity.this, ContactListActivity.class);
                startActivityForResult(three2contactlist, 0);

                break;
            case R.id.step_3_bt_pre:
                Intent three2two = new Intent(MobileSafeSetup3Activity.this, MobileSafeSetup2Activity.class);
                startActivity(three2two);
                finish();

                //开启平移动画
                overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
                break;
            case R.id.step_3_bt_next:

                //输入框中的电话不为空，就可以跳转
                String phoneByHand = step_3_safephone.getText().toString();

                phoneByHand = phoneByHand.replace("-", "").replace("(", "").replace(")", "").replace(" ", "").trim();

                if (!TextUtils.isEmpty(phoneByHand)) {

                    SpUtil.putString(MobileSafeSetup3Activity.this, ConstantValue.SAFE_PHONE, phoneByHand);

                    Intent three2four = new Intent(MobileSafeSetup3Activity.this, MobileSafeSetup4Activity.class);
                    startActivity(three2four);
                    finish();

                    //开启平移动画
                    overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                } else {
                    ToastUtil.show(MobileSafeSetup3Activity.this, "请指定联系人哦~");
                }

                break;
        }
    }

    //返回当前界面是，接收结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            String phone = data.getStringExtra("phone");
            //过滤特殊字符
            phone = phone.replace("-", "").replace("(", "").replace(")", "").replace(" ", "").trim();
            step_3_safephone.setText(phone);

            //存储联系人
            SpUtil.putString(MobileSafeSetup3Activity.this, ConstantValue.SAFE_PHONE, phone);
        }
    }


    @Override
    protected void showNextPage() {

        //输入框中的电话不为空，就可以跳转
        String phoneByHand = step_3_safephone.getText().toString();

        phoneByHand = phoneByHand.replace("-", "").replace("(", "").replace(")", "").replace(" ", "").trim();

        if (!TextUtils.isEmpty(phoneByHand)) {

            SpUtil.putString(MobileSafeSetup3Activity.this, ConstantValue.SAFE_PHONE, phoneByHand);

            Intent three2four = new Intent(MobileSafeSetup3Activity.this, MobileSafeSetup4Activity.class);
            startActivity(three2four);
            finish();

            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(MobileSafeSetup3Activity.this, "请指定联系人哦~");
        }


    }

    @Override
    protected void showPrePage() {

        Intent three2two = new Intent(MobileSafeSetup3Activity.this, MobileSafeSetup2Activity.class);
        startActivity(three2two);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}
