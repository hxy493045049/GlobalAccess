package cn.msy.zc.commonutils;

import android.support.annotation.StringDef;

import com.orhanobut.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hxy on 2017/2/6.
 * class description:
 */

public class TimeFormat {
    //常用事件格式
    public static final String regular1 = "yyyy-MM-dd HH:mm:ss";//等价于now.toLocaleString()
    public static final String regular2 = "MM-dd HH:mm";
    public static final String regular3 = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String regular4 = "yy/MM/dd HH:mm";
    public static final String regular5 = "yyyy年MM月dd日 HH时mm分ss秒 E";
    public static final String regular6 = "一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 z时区";
    public static final String regular7 = "yyyy-MM-dd";
    public static final String regular8 = "HH:mm";
    public static final String regular9 = "yyyy-MM-dd HH:mm";
    public static final String regular10 = "yyyy年M月d日";
    public static final String regular11 = "M月d日";

    /**
     * @param regular 时间格式,自定义 如yyyy-MM-dd HH:mm:ssafterRegular
     * @param data    日期
     */
    public static String formatData(String regular, Date data) {
        SimpleDateFormat myFmt = new SimpleDateFormat(regular, Locale.getDefault());
        return myFmt.format(data);
    }

    /**
     * @param targetRegular 时间格式,自定义 如yyyy-MM-dd HH:mm:ssafterRegular
     * @param data          日期
     */
    public static String formatData(String targetRegular, String sourceRegular, String data) {
        SimpleDateFormat format = new SimpleDateFormat(sourceRegular);
        try {
            return formatData(targetRegular, format.parse(data));
        } catch (ParseException e) {
            Logger.e(e, "时间转换错误");
            return "";
        }
    }

    /**
     * 将时间格式从格式1转为格式2
     *
     * @param before        表示时间的字符串
     * @param beforeRegular 转换前的时间格式
     * @param afterRegular  要转换成的时间格式
     */
    public static String transformData(String before, String beforeRegular, String afterRegular) {
        String after = "";
        try {
            Date date = new SimpleDateFormat(beforeRegular, Locale.getDefault()).parse(before);
            after = formatData(afterRegular, date);
        } catch (Exception e) {
            Logger.e("转换日期格式异常：" + e.toString());
            return before;
        }
        return after;
    }

    public static String getCurrentDate(String regular) {
        return formatData(regular, new Date());
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getCurrentMonthStartDay() {
        SimpleDateFormat format = new SimpleDateFormat(regular7);
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return format.format(calendar.getTime());
    }

    /**
     * 获取当前月最后一天
     *
     * @return
     */
    public static String getCurrentMonthEndDay() {
        SimpleDateFormat format = new SimpleDateFormat(regular7);
        Calendar calendar = Calendar.getInstance();//获取当前日期
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return format.format(calendar.getTime());
    }

    @StringDef({regular1, regular2, regular3, regular4, regular5, regular6, regular7, regular8, regular9, regular10,
            regular11})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeRugular {

    }

}
