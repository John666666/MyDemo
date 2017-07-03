package com.john.mydemo.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.john.mydemo.R;
import com.john.mydemo.db.MyDBHelper;

public class SQLiteActiviy extends Activity implements View.OnClickListener {

    private TextView tvResult;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_activiy);

        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.tv_result);

        db = MyDBHelper.getInstance(this).getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;
            case R.id.btn_add:
                add();
                break;
        }
    }

    private void query() {
        StringBuilder sb = new StringBuilder();
//        sb.append("id\tsname\tage\tremark\text\n");
        sb.append("id\tsname\tage\tremark\n");
        new Thread(() -> {
            boolean hasRecord = false;
            Cursor cur = db.query(MyDBHelper.TABLE_STUDENT, null, null, null, null, null, null);
            if(cur != null) {
                while (cur.moveToNext()) {
                    hasRecord = true;
                    sb.append(cur.getInt(cur.getColumnIndex("id"))+" \t");
                    sb.append(cur.getString(cur.getColumnIndex("sname"))+" \t");
                    sb.append(cur.getInt(cur.getColumnIndex("age"))+" \t");
                    sb.append(cur.getString(cur.getColumnIndex("remark"))+" \t");
//                    sb.append(cur.getString(cur.getColumnIndex("ext"))+" \t");
                    sb.append("\n");
                }
            }
            if(!hasRecord) {
                sb.append("暂无记录");
            }

            tvResult.post(() -> {
                tvResult.setText(sb.toString());
            });
        }).start();
    }

    private void add() {
        new Thread(() -> {
            ContentValues cv = new ContentValues();
            cv.put("sname", "李雷");
            cv.put("age", System.currentTimeMillis() % 30);
            cv.put("remark", "雍景城" + System.currentTimeMillis() % 30+"号");
//            cv.put("ext", "扩展字段");
            db.insert(MyDBHelper.TABLE_STUDENT, null, cv);

            //after insert auto refresh ui
            query();
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db != null && db.isOpen()) {
            db.close();
        }
    }
}
