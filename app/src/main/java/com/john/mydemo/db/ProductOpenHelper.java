package com.john.mydemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.john.mydemo.bean.DaoMaster;
import com.john.mydemo.bean.DaoSession;
import com.john.mydemo.bean.Student;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by John on 2017/6/10.
 */

public class ProductOpenHelper extends DaoMaster.OpenHelper {
    private final static String TAG = "ProductOpenHelper";
    public ProductOpenHelper(Context context, String name) {
        super(context, name);
    }

    public ProductOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "green dao upgrade from: "+oldVersion+" to: "+newVersion);
//        TODO 这种方式不行， 如果新增了字段， 老表中没有此字段，所以查询会出错
        DaoSession daoSession = new DaoMaster(db).newSession();
        db.beginTransaction();
        try {
        //备份旧数据到内存中
        List<Student> stuList = daoSession.getStudentDao().queryBuilder().list();
            //删除旧表
            DaoMaster.dropAllTables(db, true);

            ///恢复数据
            if(stuList != null && stuList.size() > 0) {
                for(Student stu : stuList) {
                    daoSession.getStudentDao().insert(stu);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "升级失败", e);
        } finally {
            db.endTransaction();
        }
    }
}
