package cn.msy.zc.commonutils;

import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;

/**
 * Created by hxy on 2016/8/22 0022.
 * description : 格式转换工具
 */
public class FormatUtils {

    private static final String TAG = FormatUtils.class.getSimpleName();

    /**
     * 将二进制长度转换成文件大小
     *
     * @param length
     *
     * @return
     */
    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(".");
            result = ((float) length / 1073741824 + "000").substring(0, sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0, sub_string + 3) + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0, sub_string + 3) + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }

    public static String formatDouble(Double value) {
        if (value == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    public static String formatDouble(String value) {
        try {
            Double d = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(d);
        } catch (NumberFormatException e) {
            Logger.e(TAG, "", e);
        }
        return "";
    }

}
