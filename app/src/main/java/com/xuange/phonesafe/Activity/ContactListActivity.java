package com.xuange.phonesafe.Activity;

/**
 * 获取联系人
 */

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xuange.phonesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView step_3_contact_list;
    private List<HashMap<String, String>> contactslist = new ArrayList<HashMap<String, String>>();
    private MyAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    mAdapter = new MyAdapter();
                    step_3_contact_list.setAdapter(mAdapter);

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
        initDate();
    }

    private void initDate() {

        //读取联系人可能是一个耗时操作，所以要放在子线程里面进行
        new Thread() {
            @Override
            public void run() {
                super.run();
                //用内容解析者，通过内容提供者，匹配URL，获取系统联系人数据
                ContentResolver resolver = getContentResolver();
                //查询系统联系人数据库
                Cursor cursor = resolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts")
                        , new String[]{"contact_id"}
                        , null, null, null);

                contactslist.clear();

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);

                    //根据用户唯一性标示id值，查询data和mimetype表生成的视图，获取data和mimetype字段
                    Cursor cursor1 = resolver.query(
                            Uri.parse("content://com.android.contacts/data")
                            , new String[]{"data1", "mimetype"}
                            , "raw_contact_id = ?"
                            , new String[]{id}
                            , null);

                    HashMap<String, String> contactsMap = new HashMap<String, String>();

                    while (cursor1.moveToNext()) {
                        String data = cursor1.getString(0);
                        String mimetype = cursor1.getString(1);

                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            if (!TextUtils.isEmpty(data)) {
                                contactsMap.put("phone", data);
                            }
                        } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                contactsMap.put("name", data);
                            }
                        }

                    }

                    cursor1.close();

                    contactslist.add(contactsMap);

                    mHandler.sendEmptyMessage(0);

                }
                cursor.close();
            }
        }.start();
    }

    private void initView() {
        step_3_contact_list = (ListView) findViewById(R.id.step_3_contact_list);

        step_3_contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点中条目的索引指向集合中的对象
                if (mAdapter != null) {
                    HashMap<String, String> hashMap = mAdapter.getItem(position);
                    String phone = hashMap.get("phone");

                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);

                    finish();
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactslist.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactslist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ContactListActivity.this, R.layout.listview_contact_item, null);

            TextView tvname = (TextView) view.findViewById(R.id.contact_name);
            TextView tvphone = (TextView) view.findViewById(R.id.contact_phone);

            tvname.setText(getItem(position).get("name"));
            tvphone.setText(getItem(position).get("phone"));

            return view;
        }
    }
}
