package com.john.mydemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.john.mydemo.MyApplication;
import com.john.mydemo.R;
import com.john.mydemo.bean.DaoSession;
import com.john.mydemo.bean.Student;

import java.util.List;

public class GreenDaoActiviy extends Activity implements View.OnClickListener {

    private TextView tvResult;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_activiy);

        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.tv_result);

        daoSession = ((MyApplication) this.getApplication()).getDaoSession();
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
//        sb.append("id\tsname\tage\tremark\n");
        sb.append("id\tsname\tage\tremark\text\n");
        new Thread(() -> {
            List<Student> students = daoSession.getStudentDao().queryBuilder().list();
            if (students != null && students.size() > 0) {
                for (Student stu : students) {
                    sb.append(stu.getId() + " \t");
                    sb.append(stu.getSname() + " \t");
                    sb.append(stu.getAge() + " \t");
                    sb.append(stu.getRemark() + " \t");
                    sb.append(stu.getExt()+" \t");
                    sb.append("\n");
                }
            } else {
                sb.append("暂无记录");
            }
            tvResult.post(() -> {
                tvResult.setText(sb.toString());
            });
        }).start();
    }

    private void add() {
        new Thread(() -> {
            Student stu = new Student();
            stu.setSname("李雷");
            stu.setAge((int)(System.currentTimeMillis() % 30));
            stu.setRemark("雍景城" + System.currentTimeMillis() % 30 + "号");
            stu.setExt("扩展字段");
            daoSession.getStudentDao().insert(stu);
            //after insert auto refresh ui
            query();
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
