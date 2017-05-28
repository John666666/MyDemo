package com.john.breadpointresume;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by John on 2017/5/28.
 */

public class SPUtil {
    private static final String FILENAME = "download";

    private SPUtil() {
    }

    private static SPUtil spUtil;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor edit;

    public static SPUtil getInstance(Context context) {
        if (spUtil == null) {
            synchronized (SPUtil.class) {
                if (spUtil == null) {
                    spUtil = new SPUtil();
                    sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
                    edit = sp.edit();
                    edit.apply();
                }
            }
        }
        return spUtil;
    }

    public void putValue(String key, String val) {
        edit.putString(key, val);
        edit.apply();
    }

    public String getValue(String key, String defVal) {
        return sp.getString(key, defVal);
    }

    public void removeValue(String key) {
        if(contains(key)) {
            edit.remove(key);
            edit.apply();
        }
    }

    public boolean contains(String key) {
        return sp.contains(key);
    }
}
