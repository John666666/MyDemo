package com.john.ipcdemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.john.ipcdemo.util.LogUtil;

public class MyProvider extends ContentProvider {
    private static final String TAG = "MyProvider";
    public static final String AUTHORITIES = "com.john.ipcdemo.myprovider";
    public static final String AUTHORITIES_URI = "content://"+AUTHORITIES+"/";

    public static final String TABLE_MAN = "man";
    public static final String TABLE_WOMAN = "woman";

    public static final int TABLE_MAN_CODE = 1;
    public static final int TABLE_MAN_ID_CODE = 2;
    public static final int TABLE_WOMAN_CODE = 3;
    public static final int TABLE_WOMAN_ID_CODE = 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITIES, TABLE_MAN, 1);
        uriMatcher.addURI(AUTHORITIES, TABLE_MAN+"/#", 2);
        uriMatcher.addURI(AUTHORITIES, TABLE_WOMAN, 3);
        uriMatcher.addURI(AUTHORITIES, TABLE_WOMAN+"/#", 4);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TABLE_MAN_CODE:
                LogUtil.i(TAG, "delete all man");
                break;
            case TABLE_MAN_ID_CODE:
                LogUtil.i(TAG, "delete man");
                break;
            case TABLE_WOMAN_CODE:
                LogUtil.i(TAG, "delete all woman");
                break;
            case TABLE_WOMAN_ID_CODE:
                LogUtil.i(TAG, "delete woman");
                break;
            default:
                LogUtil.w(TAG, "delete uri not matched");
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        String mimeType = "";
        switch (uriMatcher.match(uri)) {
            case TABLE_MAN_CODE:
                break;
            case TABLE_MAN_ID_CODE:
                break;
            case TABLE_WOMAN_CODE:
                break;
            case TABLE_WOMAN_ID_CODE:
                break;
        }
        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case TABLE_MAN_CODE:
            case TABLE_MAN_ID_CODE:
                LogUtil.i(TAG, "insert man");
                break;
            case TABLE_WOMAN_CODE:
            case TABLE_WOMAN_ID_CODE:
                LogUtil.i(TAG, "insert woman");
                break;
            default:
                LogUtil.w(TAG, "insert uri not matched");
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        LogUtil.i(TAG, "onCreate");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TABLE_MAN_CODE:
                LogUtil.i(TAG, "query all man");
                break;
            case TABLE_MAN_ID_CODE:
                LogUtil.i(TAG, "query man");
                break;
            case TABLE_WOMAN_CODE:
                LogUtil.i(TAG, "query all woman");
                break;
            case TABLE_WOMAN_ID_CODE:
                LogUtil.i(TAG, "query woman");
                break;
            default:
                LogUtil.w(TAG, "query uri not matched");
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TABLE_MAN_CODE:
                LogUtil.i(TAG, "update all man");
                break;
            case TABLE_MAN_ID_CODE:
                LogUtil.i(TAG, "update man");
                break;
            case TABLE_WOMAN_CODE:
                LogUtil.i(TAG, "update all woman");
                break;
            case TABLE_WOMAN_ID_CODE:
                LogUtil.i(TAG, "update woman");
                break;
            default:
                LogUtil.w(TAG, "update uri not matched");
        }
        return 0;
    }
}
