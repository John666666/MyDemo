package com.john.mydemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by John on 2017/6/10.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    
    private final static String TAG = "MyDBHelper";
    
    public final static int VERSION = 4;
    public final static String DB_NAME = "test";

    public final static String TABLE_STUDENT = "student";

    public final static String CREATE_TABLE_STUDENT = "create table "+TABLE_STUDENT+" (" +
            "id integer primary key autoincrement," +
            "sname text not null," +
            "age integer default 16 not null" +
            "remark text);";

    public final static String UPGRADE_TABLE_STUDENT_1 = "alter table "+TABLE_STUDENT+" add column remark text;";
    public final static String UPGRADE_TABLE_STUDENT_2 = "alter table "+TABLE_STUDENT+" add column ext text;";

    //删除字段， sqlite不支持标准的alter table drop column xx语句的解决方法
    public final static String UPGRADE_TABLE_STUDENT_3 = "create table temp as select id, sname, age, remark from "+TABLE_STUDENT+";" +
            "drop table "+TABLE_STUDENT+"; alter table rename temp "+TABLE_STUDENT+";";


    private static MyDBHelper dbHelper;

    private MyDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static MyDBHelper getInstance(Context ctx) {
        if(dbHelper == null) {
            synchronized (MyDBHelper.class) {
                if(dbHelper == null) {
                    dbHelper = new MyDBHelper(ctx);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENT);
        Log.i(TAG, "onCreate, exec -> "+CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade oldVersion: "+oldVersion+", newVersion: "+newVersion);
        switch (oldVersion) {
            case 1:
                db.execSQL(UPGRADE_TABLE_STUDENT_1);
            case 2:
                db.execSQL(UPGRADE_TABLE_STUDENT_2);
            case 3:
                db.execSQL(UPGRADE_TABLE_STUDENT_3);
                Log.i(TAG, "exec -> "+UPGRADE_TABLE_STUDENT_3);
            default:
                break;

        }
    }
}
