package com.john.mydemo;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.utils.CrashUtils;
import com.blankj.utilcode.utils.Utils;
import com.john.mydemo.bean.DaoMaster;
import com.john.mydemo.bean.DaoSession;
import com.john.mydemo.db.ProductOpenHelper;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by John on 2017/6/10.
 */

public class MyApplication extends Application {

    private DaoSession daoSession;

    private static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        ctx = this;
        Utils.init(this);

        CrashUtils.getInstance().init();

        //这个类此适用于开发环境， 每次升级都重建所有表
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "test_green");
        ProductOpenHelper helper = new ProductOpenHelper(this, "product_green");
        daoSession = new DaoMaster(helper.getWritableDb()).newSession();

        LeakCanary.install(this);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext() {
        return ctx;
    }
}
