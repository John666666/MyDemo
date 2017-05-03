package com.john.dinghelper.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 17/3/6.
 */

public class FileLogUtil {
    private final static String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
    private String logName;
    private File logFile;
    private String charset = "UTF-8";
    private static Map<String, FileLogUtil> cache = new HashMap<>();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private FileLogUtil(String logFileName) {
        this.logName = logFileName;
        this.logFile = new File(ROOT_PATH, logName);
    }

    public synchronized static FileLogUtil getInstance(String logFileName) {
        if (cache.containsKey(logFileName)) {
            return cache.get(logFileName);
        } else {
            FileLogUtil util = new FileLogUtil(logFileName);
            cache.put(logFileName, util);
            return util;
        }
    }

    public void write(String msg) {
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(this.logFile, true), charset);
            out.write(sdf.format(new Date()) + "\t" + msg + "\n");
            out.flush();
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
