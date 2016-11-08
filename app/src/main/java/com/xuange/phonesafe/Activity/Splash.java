package com.xuange.phonesafe.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xuange.phonesafe.R;
import com.xuange.phonesafe.Util.ConstantValue;
import com.xuange.phonesafe.Util.SpUtil;
import com.xuange.phonesafe.Util.StreamUtil;
import com.xuange.phonesafe.Util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * App登录界面
 */
public class Splash extends AppCompatActivity {

    protected static final String tag = "Splash";
    //更新程序的状态码
    private static final int UPDATE_VERSOIN = 100;
    //进入主界面的状态码
    private static final int ENTER_HOME = 101;
    //URL错误的状态码
    private static final int URL_ERROR = 201;
    //IO错误的状态码
    private static final int IO_ERROR = 202;
    //JSON错误的状态码
    private static final int JSON_ERROR = 203;

    private TextView tv_vision;
    private ProgressBar pb_vision;
    private RelativeLayout rl_root;
    private int mLocaleVersionCode;
    private String mVersionName;
    private String mVersionDes;
    private String mVersionCode;
    private String mDownloadUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPDATE_VERSOIN:
                    //提示更新，弹出对话框
                    showUpdateDialog();

                    break;
                case ENTER_HOME:
                    //进入主界面
                    enterHome();

                    break;
                case URL_ERROR:
                    //URL错误
                    ToastUtil.show(Splash.this, "URl异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    //IO错误
                    ToastUtil.show(Splash.this, "IO异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    //JSON错误
                    ToastUtil.show(Splash.this, "JSON异常");
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
        initAnimation();
    }

    private void initView() {
        tv_vision = (TextView) findViewById(R.id.tv_vision);
        pb_vision = (ProgressBar) findViewById(R.id.pb_vision);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }

    /**
     * 淡入淡出动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);

    }

    //弹出对话框，提示用户更新
    private void showUpdateDialog() {
        //对话框需要依赖activity
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        //设置对话框内容
        builder.setMessage(mVersionDes);
        //积极按钮，立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载APK，APK连接，downloadUrl
                //用XUtils，获取HttpUtils对象，下载指定连接地址的APK
                downloadApk();
            }
        });

        //消极按钮，暂不更新
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
                dialog.dismiss();
                finish();
            }
        });

        //点击取消的事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    //进入应用主界面
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //销毁导航界面
        finish();
    }

    private void initData() {
        //1 获取本地版本名称
        tv_vision.setText("版本名称：" + getVersionName());
        //2 检测版本是否最新，否则提示更新
        mLocaleVersionCode = getVersionCode();
        //3 获取服务器版本号(客户端发请求，服务器端给响应(json))
        //如果用户关闭自动更新，则不检查，直接进入主界面；否则，检查版本并更新，默认是不更新
        if (SpUtil.getBoolean(Splash.this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
            //由于要延时进入，所以要发送消息handler，在发送消息之后4秒处理事件
            //mHandler.sendMessageDelayed(msg, 4000);
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }

    }

    //检查版本信息并获取更新版本
    private void checkVersion() {

        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                //发送请求获取数据，参数为JSON的链接地址
                try {
                    //封装URL
                    URL url = new URL("http://10.0.2.2:8080/update74.json");
                    //开始一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //链接超时
                    connection.setConnectTimeout(3000);
                    //读取超时
                    connection.setReadTimeout(3000);
                    //设置请求方式，默认为GET
                    //connection.setRequestMethod("GET");

                    //获取请求成功的响应码
                    if (connection.getResponseCode() == 200) {
                        //以流的形式获取数据
                        InputStream is = connection.getInputStream();
                        //将流装换为字符串
                        String json = StreamUtil.stream2String(is);
                        Log.i(tag, json);
                        //解析得到的JSON
                        JSONObject jsonObject = new JSONObject(json);

                        mVersionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        mVersionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        Log.i(tag, mVersionName);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, mVersionCode);
                        Log.i(tag, mDownloadUrl);

                        if (mLocaleVersionCode != Integer.parseInt(mVersionName)) {
                            //提示更新
                            msg.what = UPDATE_VERSOIN;
                        } else {
                            //进入主界面
                            msg.what = ENTER_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {

                    //如果请求网络时长超过4秒，不做处理。否则，请求网络+停顿=4秒后再发送消息
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mHandler.sendMessage(msg);
                }
            }
        }.start();

    }

    //更新下载Apk
    private void downloadApk() {

        //1.Apk下载连接地址   2.放置Apk的所在路径
        //如果想要放在SD卡中，要判断SD卡是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //挂载，获取SD卡路径（mobilesafe.apk为下载好的名字）
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilesafe.apk";
            //发送请求，获取Apk，并且下载到指定路径(XUtils)
            HttpUtils httpUtils = new HttpUtils();
            //发送请求，传递参数（mDownloadUrl 下载地址，path 下载文件所放位置，下载成功和失败的抽象方法）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                //下载成功的方法
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功放置在SD卡中的Apk
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);

                }

                //下载失败的方法
                @Override
                public void onFailure(HttpException e, String s) {
                    ToastUtil.show(Splash.this, "下载失败。");
                }

                //刚刚开始下载的方法
                @Override
                public void onStart() {
                    super.onStart();
                }

                //下载过程中的方法（total 总数，current 当前进度，isUploading 是否正在下载）
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }

                //取消下载的方法
                @Override
                public void onCancelled() {
                    super.onCancelled();
                }
            });

        }

    }


    /**
     * 安装下载好的Apk
     *
     * @param file 被安装的文件
     */
    private void installApk(File file) {

        //打包Apk时，需要包名相同，签名文件相同！！！
        //用系统的安装工具
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
/*        //把文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        enterHome();
        finish();
        super.onActivityReenter(resultCode, data);
    }

    /**
     * 获取版本号
     *
     * @return 应用版本号，返回null代表异常
     */
    private int getVersionCode() {

        //1 包管理者对象
        PackageManager pm = getPackageManager();
        //2 从包管理者对象中获取信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3 获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 在清单文件里获得
     *
     * @return 应用版本名称，返回null代表异常
     */
    private String getVersionName() {
        //1 包管理者对象
        PackageManager pm = getPackageManager();
        //2 从包管理者对象中获取信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //3 获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
