package com.xuange.phonesafe.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.MD5Util;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.Util.ToastUtil;

/**
 * App主界面
 */
public class HomeActivity extends AppCompatActivity {

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mIconID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDate();

    }

    private void initDate() {
        //gv_home 数据准备
        mTitleStr = new String[]{
                "手机防盗",
                "通信卫士",
                "软件管理",
                "进程管理",
                "流量统计",
                "手机杀毒",
                "缓存清理",
                "高级工具",
                "设置中心",
        };

        mIconID = new int[]{
                R.drawable.home_safe,
                R.drawable.home_callmsgsafe,
                R.drawable.home_apps,
                R.drawable.home_taskmanager,
                R.drawable.home_netmanager,
                R.drawable.home_trojan,
                R.drawable.home_sysoptimize,
                R.drawable.home_tools,
                R.drawable.home_settings,
        };

        //设置数据
        gv_home.setAdapter(new gv_Home_adapter());

        //gv_home点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showLoginDialog();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        //设置中心
                        Intent home2setting = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(home2setting);
                        break;
                }
            }
        });

    }

    //登录对话框
    private void showLoginDialog() {

        //对话框分两种
        //登录的对话框（第一次登录）和确认密码的对话框（后续登录）
        //判断本地是否保存过登录密码-SP文件
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");

        if (TextUtils.isEmpty(psd)) {
            //初始设置密码对话框
            showSetPsdDialog();
        } else {
            //确认密码对话框
            showConfigPsdDialog();
        }

    }

    /**
     * 确认密码对话框
     */
    private void showConfigPsdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog setPsdDialog = builder.create();
        //自定义Dialog布局
        final View view = View.inflate(HomeActivity.this, R.layout.dialog_confirm_psd, null);
        //剩下四个参数是上下左右的内边距
        setPsdDialog.setView(view, 0, 0, 0, 0);
        setPsdDialog.show();

        Button bt_confirm_submit = (Button) view.findViewById(R.id.bt_confirm_submit);
        Button bt_confirm_cancel = (Button) view.findViewById(R.id.bt_confirm_cancel);

        bt_confirm_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd_confirm = (EditText) view.findViewById(R.id.et_confirm_psd_confirm);
                String confirmPsd = MD5Util.encoder(et_confirm_psd_confirm.getText().toString());

                if (!TextUtils.isEmpty(confirmPsd)) {

                    String psd = SpUtil.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PWD, "");

                    if (psd.equals(confirmPsd)) {
                        //进入手机防盗模块

                        Intent intent = new Intent(HomeActivity.this, MobileSafeActivity.class);
                        startActivity(intent);
                        setPsdDialog.dismiss();

                    } else {
                        ToastUtil.show(HomeActivity.this, "密码输入错误，请确认密码！");
                    }
                } else {
                    //不得进入
                    ToastUtil.show(HomeActivity.this, "请输入密码！");
                }

            }
        });

        bt_confirm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPsdDialog.dismiss();
            }
        });
    }

    /**
     * 初始设置密码对话框
     */
    private void showSetPsdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog setPsdDialog = builder.create();
        //自定义Dialog布局
        final View view = View.inflate(HomeActivity.this, R.layout.dialog_set_psd, null);
        setPsdDialog.setView(view, 0, 0, 0, 0);
        setPsdDialog.show();

        Button bt_set_submit = (Button) view.findViewById(R.id.bt_set_submit);
        Button bt_set_cancel = (Button) view.findViewById(R.id.bt_set_cancel);

        bt_set_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd_set = (EditText) view.findViewById(R.id.et_set_psd_set);
                EditText et_set_psd_confirm = (EditText) view.findViewById(R.id.et_set_psd_confirm);

                String psd = et_set_psd_set.getText().toString();
                String confirmPsd = et_set_psd_confirm.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {
                    if (psd.equals(confirmPsd)) {
                        //进入手机防盗模块

                        Intent intent = new Intent(HomeActivity.this, MobileSafeActivity.class);
                        startActivity(intent);
                        setPsdDialog.dismiss();
                        SpUtil.putString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PWD, MD5Util.encoder(psd));

                    } else {
                        ToastUtil.show(HomeActivity.this, "两次密码输入不一致！");
                    }
                } else {
                    //不得进入
                    ToastUtil.show(HomeActivity.this, "请输入密码！");
                }
            }
        });

        bt_set_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPsdDialog.dismiss();
            }
        });

    }

    private void initView() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    class gv_Home_adapter extends BaseAdapter {
        @Override
        public int getCount() {
            //条目的总数，文字组数=图片张数
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this, R.layout.gv_home_item, null);

            TextView gv_home_text = (TextView) view.findViewById(R.id.gv_home_text);
            ImageView gv_home_icon = (ImageView) view.findViewById(R.id.gv_home_icon);

            gv_home_text.setText(mTitleStr[position]);
            gv_home_icon.setImageResource(mIconID[position]);

            return view;
        }
    }
}
