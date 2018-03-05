package com.msy.globalaccess.utils.helper;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.msy.zc.commonutils.TimeFormat;

/**
 * Created by shawn on 2017/7/10 0010.
 * <p>
 * description :
 */

public class TimeHelper {
    public static final int TODAY = 0;
    public static final int LAST_WEEK = 1;
    public static final int LAST_MOHTH = 2;
    public static final int LAST_THREE_MONTH = 3;
    public static final int LAST_SIX_MONTH = 4;
    public static final int LAST_YEAR = 5;

    public static String[] getTimeByStrategy(@TimeStrategy int strategy, @TimeFormat.TimeRugular String timeRegular) {
        String[] times = new String[2];
        times[1] = TimeFormat.getCurrentDate(timeRegular);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        switch (strategy) {
            case TODAY://0
                times[0] = TimeFormat.getCurrentDate(timeRegular);
                break;
            case LAST_WEEK://1
                calendar.set(Calendar.DAY_OF_YEAR, day - 7);
                break;
            case LAST_MOHTH://2
                calendar.set(Calendar.DAY_OF_YEAR, day - 30);
                break;
            case LAST_THREE_MONTH://3
                calendar.set(Calendar.DAY_OF_YEAR, day - 90);
                break;
            case LAST_SIX_MONTH://4
                calendar.set(Calendar.DAY_OF_YEAR, day - 180);
                break;
            case LAST_YEAR://5
                calendar.set(Calendar.DAY_OF_YEAR, day - 360);
                break;
        }
        if (TextUtils.isEmpty(times[0])) {
            times[0] = TimeFormat.formatData(timeRegular, calendar.getTime());
        }
        return times;
    }

    /**
     * 比较两个时间的大小
     * 如果time2>time1 返回1
     * 如果time2=time1 返回0
     * 如果time2<time1 返回-1
     * 如果时间格式异常 返回-2
     */
    public static int compareTime(String time1, @TimeFormat.TimeRugular String timeRegular1, String time2,
                                  @TimeFormat.TimeRugular String timeRegular2) {
        try {
            Date startTime = new SimpleDateFormat(timeRegular1, Locale.getDefault()).parse(time1);
            Date endTime = new SimpleDateFormat(timeRegular2, Locale.getDefault()).parse(time2);
            return endTime.compareTo(startTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return -2;
        }
    }

    @IntDef({TODAY, LAST_WEEK, LAST_MOHTH, LAST_THREE_MONTH, LAST_SIX_MONTH, LAST_YEAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeStrategy {

    }
}
