package com.xuange.phonesafe.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.Util.ToastUtil;
import com.xuange.phonesafe.View.SettingItemView;

public class MobileSafeSetup2Activity extends BaseSetupActivity implements View.OnClickListener {

    private Button step_2_bt_pre;
    private Button step_2_bt_next;
    private SettingItemView step_2_simbound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe_setup2);
        initView();
    }

    private void initView() {
        step_2_bt_pre = (Button) findViewById(R.id.step_2_bt_pre);
        step_2_bt_next = (Button) findViewById(R.id.step_2_bt_next);
        step_2_simbound = (SettingItemView) findViewById(R.id.step_2_simbound);

        step_2_bt_pre.setOnClickListener(this);
        step_2_bt_next.setOnClickListener(this);
        step_2_simbound.setOnClickListener(this);

        //读取是否绑定过Sim卡
        String sim_number = SpUtil.getString(MobileSafeSetup2Activity.this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            step_2_simbound.setCheck(false);
        } else {
            step_2_simbound.setCheck(true);
        }

        step_2_simbound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischeck = step_2_simbound.isCheck();
                step_2_simbound.setCheck(!ischeck);
                if (!ischeck) {
                    //!ischeck 为true，则存储Sim卡号
                    //获取Sim卡号
                    TelephonyManager tphmanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = tphmanager.getSimSerialNumber();
                    SpUtil.putString(MobileSafeSetup2Activity.this, ConstantValue.SIM_NUMBER, simSerialNumber);
                } else {
                    //否则不存储,把存储Sim卡号的节点从sp中删除掉
                    SpUtil.remove(MobileSafeSetup2Activity.this, ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_2_bt_pre:
                Intent tow2one = new Intent(MobileSafeSetup2Activity.this, MobileSafeSetup1Activity.class);
                startActivity(tow2one);
                finish();

                //开启平移动画
                overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
                break;
            case R.id.step_2_bt_next:

                String isBoundSim = SpUtil.getString(MobileSafeSetup2Activity.this, ConstantValue.SIM_NUMBER, "");

                if (!TextUtils.isEmpty(isBoundSim)) {

                    Intent tow2three = new Intent(MobileSafeSetup2Activity.this, MobileSafeSetup3Activity.class);
                    startActivity(tow2three);
                    finish();

                    //开启平移动画
                    overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                } else {
                    ToastUtil.show(MobileSafeSetup2Activity.this,"请先绑定Sim卡才能跳转下一步。");
                }

                break;
        }
    }


    @Override
    protected void showNextPage() {


        String isBoundSim = SpUtil.getString(MobileSafeSetup2Activity.this, ConstantValue.SIM_NUMBER, "");

        if (!TextUtils.isEmpty(isBoundSim)) {

            Intent tow2three = new Intent(MobileSafeSetup2Activity.this, MobileSafeSetup3Activity.class);
            startActivity(tow2three);
            finish();

            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(MobileSafeSetup2Activity.this,"请先绑定Sim卡才能跳转下一步。");
        }

    }

    @Override
    protected void showPrePage() {

        Intent tow2one = new Intent(MobileSafeSetup2Activity.this, MobileSafeSetup1Activity.class);
        startActivity(tow2one);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}
