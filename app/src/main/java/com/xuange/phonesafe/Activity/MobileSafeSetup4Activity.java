package com.xuange.phonesafe.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Matrix4f;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.Util.ToastUtil;

public class MobileSafeSetup4Activity extends BaseSetupActivity implements View.OnClickListener {

    private CheckBox step_4_cb;
    private Button step_4_bt_pre;
    private Button step_4_bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe_setup4);
        initView();
    }

    private void initView() {
        step_4_cb = (CheckBox) findViewById(R.id.step_4_cb);
        step_4_bt_pre = (Button) findViewById(R.id.step_4_bt_pre);
        step_4_bt_next = (Button) findViewById(R.id.step_4_bt_next);

        step_4_bt_pre.setOnClickListener(this);
        step_4_bt_next.setOnClickListener(this);

        //判断之前有没有开启过此功能
        final boolean opensafe = SpUtil.getBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, false);
        //根据状态修改checkbox的文字和状态
        step_4_cb.setChecked(opensafe);
        if (opensafe) {
            step_4_cb.setText("安全设置已开启。");
        } else {
            step_4_cb.setText("安全设置已关闭。");
        }

        step_4_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked 点击之后的状态，系统已经维护好
                if (isChecked) {
                    step_4_cb.setText("安全设置已开启。");
                } else {
                    step_4_cb.setText("安全设置已关闭。");
                }
                SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, isChecked);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_4_bt_pre:
                Intent four2three = new Intent(MobileSafeSetup4Activity.this, MobileSafeSetup3Activity.class);
                startActivity(four2three);
                finish();

                //开启平移动画
                overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
                break;
            case R.id.step_4_bt_next:

                boolean tonext = SpUtil.getBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, false);

                if (tonext) {
                    Intent four2over = new Intent(MobileSafeSetup4Activity.this, MobileSafeActivity.class);
                    startActivity(four2over);

                    SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.SETUP_OVER, true);

                    finish();

                    //开启平移动画
                    overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
                } else {
                    //ToastUtil.show(MobileSafeSetup4Activity.this, "请开启防盗保护。");
                    showSafeOpenDialog();
                }

                break;
        }
    }

    //弹出对话框，提示用户真的不需要开启保护吗？
    private void showSafeOpenDialog() {
        //对话框需要依赖activity
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("您确定不要开启防盗保护吗？");
        //设置对话框内容
        builder.setMessage("如果您不开启防盗保护的功能，之前所做的一切都要化为泡影了呢...");
        //积极按钮，立即更新
        builder.setPositiveButton("开启保护", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent four2over = new Intent(MobileSafeSetup4Activity.this, MobileSafeActivity.class);
                startActivity(four2over);

                SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.SETUP_OVER, true);
                SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, true);

                finish();

                //开启平移动画
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            }
        });

        //消极按钮，暂不更新
        builder.setNegativeButton("暂不开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent four2over = new Intent(MobileSafeSetup4Activity.this, MobileSafeActivity.class);
                startActivity(four2over);

                SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.SETUP_OVER, true);
                SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, false);

                dialog.dismiss();
                finish();
            }
        });

        //点击取消的事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    @Override
    protected void showNextPage() {

        boolean tonext = SpUtil.getBoolean(MobileSafeSetup4Activity.this, ConstantValue.OPEN_SAFE, false);

        if (tonext) {
            Intent four2over = new Intent(MobileSafeSetup4Activity.this, MobileSafeActivity.class);
            startActivity(four2over);

            SpUtil.putBoolean(MobileSafeSetup4Activity.this, ConstantValue.SETUP_OVER, true);

            finish();

            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            //ToastUtil.show(MobileSafeSetup4Activity.this, "请开启防盗保护。");
            showSafeOpenDialog();
        }

    }

    @Override
    protected void showPrePage() {

        Intent four2three = new Intent(MobileSafeSetup4Activity.this, MobileSafeSetup3Activity.class);
        startActivity(four2three);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }
}
