package com.john.dinghelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.john.dinghelper.utils.FileLogUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by john on 17/2/23.
 */

public class DingDingHelper {

    public final static String LOGFILENAME = DingDingHelper.class.getSimpleName()+".log";

    public static void startDingDing(Context context) {
        Calendar cal = Calendar.getInstance();

        int dw = cal.get(Calendar.DAY_OF_WEEK);
        FileLogUtil.getInstance(DingDingHelper.LOGFILENAME).write("准备启动钉钉");
        if(dw != Calendar.SUNDAY && dw != Calendar.SATURDAY) {
            Intent intent = new Intent();
            intent.setPackage("com.alibaba.android.rimet");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            PackageManager pm = context.getPackageManager();
            java.util.List<ResolveInfo> activities = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL);
            if(activities == null || activities.size() < 1) {
                FileLogUtil.getInstance(DingDingHelper.LOGFILENAME).write("没有找到合适的应用");
            } else {
                ResolveInfo resolveInfo = activities.get(0);
                String packageName = resolveInfo.activityInfo.packageName;
                String className = resolveInfo.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                FileLogUtil.getInstance(DingDingHelper.LOGFILENAME).write("启动成功");
            }
        } else {
            FileLogUtil.getInstance(DingDingHelper.LOGFILENAME).write("周末不用打卡");
        }
        //启动完成后， 设置下一次定时启动任务
        startAlarm(context, true);
    }

    public static void startAlarm(Context context) {
        startAlarm(context, false);
    }

    public static void startAlarm(Context context, boolean isTomorrow) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        if(isTomorrow) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }
//        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        //在53分基础上随机往后浮动5分钟以内
        Random random = new Random();
        cal.set(Calendar.MINUTE, 56 + random.nextInt(2));

        //如果启动时时间已经过去， 启动时间定为明天
        if(cal.getTime().getTime() < System.currentTimeMillis()) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        }
        LogUtils.i("设定启动时间："+cal.getTime());
        FileLogUtil.getInstance(DingDingHelper.LOGFILENAME).write("下次启动时间："+cal.getTime());
        ToastUtils.showShortToast(cal.getTime()+"");
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
//        long rate = 24 * 60 * 60 * 1000;
//        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), rate, pi);
    }

}
