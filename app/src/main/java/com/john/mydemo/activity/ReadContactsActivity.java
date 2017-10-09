package com.john.mydemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.blankj.utilcode.utils.LogUtils;
import com.john.mydemo.R;

import java.util.ArrayList;
import java.util.List;

public class ReadContactsActivity extends Activity {
    private final static String TAG = "ReadContactsActivity";
    private ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);
        listView = (ListView) findViewById(R.id.lv_contacts);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogUtils.i(TAG, "当前系统版本: " + Build.VERSION.SDK_INT + ", 需要动态申请权限！");
            if (PackageManager.PERMISSION_GRANTED !=
                    getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "必须允许程序读取您的通信录！", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
                }
            } else {
                LogUtils.i(TAG, "开始加载数据...");
                loadData();
            }
        } else {
            LogUtils.i(TAG, "开始加载数据...");
            loadData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadData();
            } else {
                Toast.makeText(this, "没有权限读取通信录！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadData() {
        LogUtils.i(TAG, "加载数据中...");
        List<String> data = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            data.add(name);
            LogUtils.i(TAG, "name: " + name);
        }
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
