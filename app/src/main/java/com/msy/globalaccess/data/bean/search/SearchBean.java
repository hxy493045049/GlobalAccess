package com.msy.globalaccess.data.bean.search;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by shawn on 2017/7/3 0003.
 * <p>
 * description : 旅游局搜索的数据结构
 */

public class SearchBean {
    public static final int TYPE_TEXT = 1;//文本类型
    public static final int TYPE_TIME = 2;//时间类型
    public static final int TYPE_SCROLL = 3;//滚轮类型
    public static final int TYPE_MULTI_TIME = 4;//多个时间

    public static final String split = "&";

    private String key = "";

    private ArrayList<String> values = new ArrayList<>();

    //选择类型,有日期,滚轮,文本,多个时间
    @ValueType
    private int valueType = TYPE_SCROLL;

    private String selectedValue = "";//这个文本有可能是2个字段:开始时间和结束时间,两者用'&'相连,用的时候根据'&'做分割,下标0位开始,下标1位结束

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    @ValueType
    public int getValueType() {
        return valueType;
    }

    public void setValueType(@ValueType int valueType) {
        this.valueType = valueType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String[] getTimes() {
        if (!TextUtils.isEmpty(selectedValue)) {
            return selectedValue.split(split);
        }
        return new String[]{};
    }

    public void setStartTime(String startTime) {
        String[] times = getTimes();
        if (times != null) {
            if (times.length == 2) {
                selectedValue = startTime + split + times[1];
            } else {
                selectedValue = startTime;
            }
        } else {
            selectedValue = startTime;
        }
    }

    public void setEndTime(String endTime) {
        String[] times = getTimes();
        if (times != null) {
            if (times.length == 2) {
                selectedValue = times[0] + split + endTime;
            } else if (selectedValue.contains(split)) {
                selectedValue = split + endTime;
            } else {
                selectedValue = selectedValue + split + endTime;
            }
        } else {
            selectedValue = split + endTime;
        }
    }

    @IntDef({TYPE_TEXT, TYPE_TIME, TYPE_SCROLL, TYPE_MULTI_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ValueType {
    }
}
