package cn.msy.zc.commonutils;

import android.util.Patterns;

/**
 * Created by hxy on 2016/7/19 0019.
 * <p>
 * description : uri,url等资源定位符相关的工具类
 */
public class UriUtils {
    /**
     * 判断url是否网络地址
     *
     * @param str url
     * @return true表示是web地址, false表示其他, 可能是本地
     */
    public static boolean isNetworkUrl(String str) {
        if (Patterns.WEB_URL.matcher(str).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
