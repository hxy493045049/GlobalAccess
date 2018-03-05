package com.msy.globalaccess.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build.VERSION;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.msy.globalaccess.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.msy.zc.commonutils.TimeFormat;


/**
 * 日期时间选择控件 使用方法： private EditText inputDate;//需要设置的日期时间文本编辑框 private String
 * initDateTime="xxxx年xx月xx日 xx:xx",//初始日期时间值 在点击事件中使用：
 * inputDate.setOnClickListener(new OnClickListener() {
 * <p>
 * dateTimePicKDialog=new
 * DateTimePickDialogUtil(SinvestigateActivity.this,initDateTime);
 * dateTimePicKDialog.dateTimePicKDialog(inputDate);
 * <p>
 * } });
 */
@SuppressLint("NewApi")
public class DateTimePickDialog implements OnDateChangedListener,
        OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Dialog ad;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    private long startDateTime;
    private long endDateTime;
    /**
     * 时间格式
     * type =0  yyyy年M月d日 (默认)
     * type =1  yyyy-M-d
     * type =2  HH:mm
     */
    private int TYPE = 0;
    public final static int TYPE_DEFAULT = 0;
    public final static int TYPE_SIMPLIFY = 1;
    public final static int TYPE_TIME = 2;

    /**
     * 格式 yyyy年M月d日 HH:mm
     */
    private SimpleDateFormat sdf  = new SimpleDateFormat(TimeFormat.regular10, Locale.getDefault());

    /**
     * 是否显示默认形态
     * 默认：yyyy年M月d日
     * 形态一：yyyy-M-d
     */
    public DateTimePickDialog(Activity activity) {
        this.activity = activity;
    }

    private void switchType() {
        switch (TYPE) {
            case TYPE_DEFAULT:
                sdf = new SimpleDateFormat(TimeFormat.regular10, Locale.getDefault());
                break;
            case TYPE_SIMPLIFY:
                sdf = new SimpleDateFormat(TimeFormat.regular7, Locale.getDefault());
                break;
            case TYPE_TIME:
                sdf = new SimpleDateFormat(TimeFormat.regular8, Locale.getDefault());
                break;
        }
    }

    /**
     * 日期时间弹出选择框构造函数
     *
     * @param activity     ：调用的父activity
     * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     * @param startDateTime System.currentTimeMillis()
     */
    public DateTimePickDialog(Activity activity, String initDateTime, int datetype, long startDateTime, long endDateTime) {
        this.activity = activity;
        this.initDateTime = initDateTime;
        this.TYPE = datetype;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    private int sysVersion = VERSION.SDK_INT;


    /**
     * 日期时间弹出选择框构造函数
     *
     * @param activity     ：调用的父activity
     * @param initDateTime 初始日期时间值，作为弹出窗口的标题和日期时间初始值
     */
    public DateTimePickDialog(Activity activity, String initDateTime, int datetype) {
        this.activity = activity;
        this.initDateTime = initDateTime;
        this.TYPE = datetype;
        switchType();
    }


    /**
     * @param activity 上下文
     * @param datetype 时间格式类型
     */
    public DateTimePickDialog(Activity activity, int datetype) {
        this.TYPE = datetype;
        this.activity = activity;
        switchType();
    }


    /**
     * 初始化 日期控件
     *
     * @return 日历
     */
    private Calendar inittDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime(calendar);
        }
        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        return calendar;

    }

    /**
     * 初始化时间控件
     *
     * @return 日历
     */
    private Calendar initTimePicker() {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime(calendar);
        }
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        return calendar;
    }

    /**
     * 初始化日历
     *
     * @param calendar 日历
     */
    private void initDateTime(Calendar calendar) {
        switch (TYPE) {
            case TYPE_DEFAULT:
                initDateTime = calendar.get(Calendar.YEAR) + "年"
                        + (calendar.get(Calendar.MONTH) + 1) + "月"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "日 "
                        + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + calendar.get(Calendar.MINUTE);
                break;
            case TYPE_SIMPLIFY:
                initDateTime = calendar.get(Calendar.YEAR) + "-"
                        + (calendar.get(Calendar.MONTH) + 1) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + " "
                        + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + calendar.get(Calendar.MINUTE);
                break;
            case TYPE_TIME:
                initDateTime = calendar.get(Calendar.HOUR_OF_DAY) + ":"
                        + calendar.get(Calendar.MINUTE);
                break;
        }
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @return dialog
     * @throws ParseException
     */
    public Dialog buildDialog(){
        return showDialog(null);
    }

    /**
     * 弹出日期时间选择框方法
     *
     * @param inputDate :为需要设置的日期时间文本编辑框
     * @return dialog
     * @throws ParseException
     */
    public Dialog buildDialog(final TextView inputDate) {
        return showDialog(inputDate);
    }

    /**
     * 显示dialog
     *
     * @param inputDate 显示时间控件
     * @return dialog
     * @throws ParseException 异常
     */
    private Dialog showDialog(final TextView inputDate){
        Calendar calendar;
        LinearLayout dateTimeLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_datetime, null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        // 设置开始和结束时间
        if (startDateTime != 0) {
            // 获取系统版本，当系统版本大于3.0的时候才有这个方法不然会有异常java.lang.NoSuchMethodError
            if (sysVersion > 10)
                datePicker.setMinDate(startDateTime);
        }
        if (endDateTime != 0) {
            // 获取系统版本，当系统版本大于3.0的时候才有这个方法不然会有异常java.lang.NoSuchMethodError
            if (sysVersion > 10)
                datePicker.setMaxDate(endDateTime);
        }
        if (TYPE == TYPE_TIME) {
            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.VISIBLE);
            calendar = initTimePicker();
        } else {
            calendar = inittDatePicker();
        }
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        ad = new Dialog(activity, R.style.no_border_dialog);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);
        ad.addContentView(dateTimeLayout, params);
        TextView cancel = (TextView) dateTimeLayout.findViewById(R.id.cancel);
        TextView ok = (TextView) dateTimeLayout.findViewById(R.id.ok);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (inputDate != null) {
                    inputDate.setText(dateTime);
                }
                if (callBack != null) {
                    callBack.doSomething(dateTime);
                }
                initDateTime = dateTime;
                ad.dismiss();
            }
        });
        // 设置居下
        ad.getWindow().setGravity(Gravity.BOTTOM);
        ad.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        if (TYPE != TYPE_TIME) {
            onDateChanged(null, 0, 0, 0);
        } else {
            dateTime = sdf.format(calendar.getTime());
        }
        return ad;
    }

    /**
     * 显示
     */
    public void show() {
        if (ad != null) {
            ad.show();
        }
    }

    /**
     * 重写timepick的监听方法
     *
     * @param view      TimePicker
     * @param hourOfDay 小时
     * @param minute    分钟
     */
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, hourOfDay, minute);
    }

    /**
     * 获取时间选择器结果
     *
     * @param view        DatePicker
     * @param year        年
     * @param monthOfYear 月/ 小时
     * @param dayOfMonth  日/  分钟
     */
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        if (TYPE == TYPE_TIME) {
            String hour;
            String minute;
            if (monthOfYear < 10) {
                hour = "0" + monthOfYear;
            } else {
                hour = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 10) {
                minute = "0" + dayOfMonth;
            } else {
                minute = String.valueOf(dayOfMonth);
            }
            dateTime = hour + ":" + minute;
        } else {
            // 获得日历实例
            Calendar calendar = Calendar.getInstance();

            calendar.set(datePicker.getYear(), datePicker.getMonth(),
                    datePicker.getDayOfMonth());
            dateTime = sdf.format(calendar.getTime());
        }
        ad.setTitle(dateTime);
    }

    /**
     * 实现将初始日期时间xxxx年xx月xx日 xx:xx 拆分成年 月 日 时 分,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar   日历
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();
        switch (TYPE) {
            case TYPE_DEFAULT:
                // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
                String date = spliteString(initDateTime, "日", "index", "front"); // 日期
                String yearStr = spliteString(date, "年", "index", "front"); // 年份
                String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

                String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
                String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

                int currentYear = Integer.valueOf(yearStr.trim());
                int currentMonth = Integer.valueOf(monthStr.trim()) - 1;
                int currentDay = Integer.valueOf(dayStr.trim());
                calendar.set(currentYear, currentMonth, currentDay);
                break;
            case TYPE_SIMPLIFY:
                String timer[] = initDateTime.split("-");
                int currentYear1 = Integer.valueOf(timer[0].trim());
                int currentMonth1 = Integer.valueOf(timer[1].trim()) - 1;
                int currentDay1 = Integer.valueOf(timer[2].trim());
                calendar.set(currentYear1, currentMonth1, currentDay1);
                break;
            case TYPE_TIME:
                String hour[] = initDateTime.split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour[0]));
                calendar.set(Calendar.MINUTE, Integer.valueOf(hour[1]));
                break;
        }
        return calendar;
    }


    /**
     * 截取子串
     *
     * @param srcStr      源串
     * @param pattern     匹配模式
     * @param indexOrLast 第一次出现的位置
     * @param frontOrBack 最后一次出现的位置
     * @return string
     */
    private static String spliteString(String srcStr, String pattern,
                                       String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void doSomething(String dateTime);
    }

}
