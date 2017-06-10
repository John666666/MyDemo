package com.john.mydemo;

import android.app.Application;

import com.john.mydemo.bean.DaoMaster;
import com.john.mydemo.bean.DaoSession;
import com.john.mydemo.db.ProductOpenHelper;

/**
 * Created by John on 2017/6/10.
 */

public class MyApplication extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        //这个类此适用于开发环境， 每次升级都重建所有表
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test_green");
        ProductOpenHelper helper = new ProductOpenHelper(this, "product_green");
        daoSession = new DaoMaster(helper.getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
