package com.xuange.phonesafe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xuange.phonesafe.R;

public class MobileSafeSetup1Activity extends BaseSetupActivity implements View.OnClickListener {

    private Button step_1_bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe_setup1);
        initView();
    }

    private void initView() {
        step_1_bt_next = (Button) findViewById(R.id.step_1_bt_next);

        step_1_bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_1_bt_next:
                Intent intent = new Intent(MobileSafeSetup1Activity.this, MobileSafeSetup2Activity.class);
                startActivity(intent);
                finish();

                //开启平移动画
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                break;
        }
    }


    @Override
    protected void showNextPage() {

        Intent intent = new Intent(MobileSafeSetup1Activity.this, MobileSafeSetup2Activity.class);
        startActivity(intent);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

    }

    @Override
    protected void showPrePage() {

    }
}
