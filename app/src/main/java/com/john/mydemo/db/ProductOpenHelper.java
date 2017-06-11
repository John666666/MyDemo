package com.john.mydemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.john.mydemo.bean.DaoMaster;
import com.john.mydemo.bean.DaoSession;
import com.john.mydemo.bean.StudentDao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
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

    /**
     * <pre>
     * 升级这里有两种做法：
     * 方法一： 数据备份恢复
     * 1. 先查询出老表中的数据， 备份在内存或者临时表。
     * 2. 调用DaoMaster的提供的方法重建表。
     * 3. 将老数据恢复到新建的表中。
     * 4. 删除临时表
     *
     * 方法二： 传统的sql方式
     * 1. 如果新增了字段。 直接通过alter table xx add column xx text/integer/real/null; 实现。
     * 2. 如果表中删除了字段。 由于sqlite不支持标准的drop column语句。 所以需要通过临时表实现：
     *    create table temp as select * from xx;
     *    drop table xx;
     *    alter table temp rename to xx;
     *
     * </pre>
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i(TAG, "green dao upgrade from: " + oldVersion + " to: " + newVersion);

        //方法一：
        db.beginTransaction();
        try {
            backupData(db, StudentDao.class);
            DaoMaster.dropAllTables(db, true);
            DaoMaster.createAllTables(db, true);
            restoreData(db, StudentDao.class);
            db.setTransactionSuccessful();
            Log.i(TAG, "迁移数据成功");
        } catch (Exception e) {
            Log.e(TAG, "迁移数据异常", e);
            throw new RuntimeException(e);
        } finally {
            db.endTransaction();
        }

        //方法二：
        //注意： 这里每个case下面故意不break， 因为升级行为具有继承性
        /*switch (oldVersion) {
            case 1:
                //添加了ext字段
                db.execSQL("alter table student add column ext text;");
            case 2:
                //删除了ext字段，注意greenDao会把id字段重命名为_id
                db.execSQL(
                        "create table temp as select _id,sname,age,remark from student;" +
                        "drop table student;" +
                        "alter table temp rename to student;");
            default:
                break;
        }*/
    }

    private void backupData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses == null || daoClasses.length < 1) {
            return;
        }
        DaoSession daoSession = new DaoMaster(db).newSession();
        try {
            for (Class<? extends AbstractDao<?, ?>> daoClazz : daoClasses) {
                Class<?> entityClazz = (Class<?>) ((ParameterizedType) daoClazz.getGenericSuperclass()).getActualTypeArguments()[0];
                AbstractDao dao = daoSession.getDao(entityClazz);
                String tabName = dao.getTablename();
                String tempTabName = dao.getTablename()+"_temp";
                db.execSQL("create table "+tempTabName+" as select * from "+tabName+";");
            }
        } finally {
            daoSession.clear();
        }
        Log.i(TAG, "备份数据完成");
    }

    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses == null || daoClasses.length < 1) {
            return;
        }
        DaoSession daoSession = new DaoMaster(db).newSession();
        try {
            for (Class<? extends AbstractDao<?, ?>> daoClazz : daoClasses) {
                Class<?> entityClazz = (Class<?>) ((ParameterizedType) daoClazz.getGenericSuperclass()).getActualTypeArguments()[0];
                AbstractDao dao = daoSession.getDao(entityClazz);
                String[] columns = dao.getAllColumns();
                List<String> fields = new ArrayList<>();
                String tabName = dao.getTablename();
                String tempTabName = dao.getTablename()+"_temp";
                Cursor cur = db.rawQuery("select * from "+tempTabName+" limit 1;", new String[]{});
                if(cur.moveToFirst()) {
                    for(String column : columns) {
                        if(cur.getColumnIndex(column) != -1) {
                            fields.add(column);
                        }
                    }
                }
                String selection = getSelection(fields);
                db.execSQL("insert into "+tabName+" ( "+selection+" ) select "+selection+" from "+tempTabName+";");
                db.execSQL("drop table "+tempTabName);
            }
        } finally {
            daoSession.clear();
        }
        Log.i(TAG, "恢复数据完成");
    }

    private String getSelection(List<String> fields) {
        StringBuilder sb = new StringBuilder();
        if(fields != null || fields.size() > 0) {
            for(String field : fields) {
                sb.append(field);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        } else {
            sb.append("*");
        }
        return sb.toString();
    }
}
