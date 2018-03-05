package cn.msy.zc.commonutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hxy on 2016/5/18 0018. <p/> description :  Sharedpreferences的工具类
 */
public class PreferencesUtils {
    /**
     * 将键值对存入app所对应的sharedpreferences文件中
     *
     * @param ctx   app上下文
     * @param key   键
     * @param value 值
     */
    public static void putString2DefoultSP(Context ctx, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        sp.edit().putString(key, value).commit();
    }

    /**
     * 根据preferences名字获取指定的sp
     *
     * @param ctx             上下文环境
     * @param preferencesName sp名字
     * @return 指定的sp
     */
    public static SharedPreferences getSPByName(Context ctx, String preferencesName) {
        SharedPreferences sp = ctx.getSharedPreferences(preferencesName, ctx.MODE_PRIVATE);
        return sp;
    }

    /**
     * 从app对应的文件中取出指定的字符串
     *
     * @param ctx app上下文
     * @param key 键
     * @return 对应的值, 如果sp中不存在, 返回""
     */
    public static String getStringFromDefoultSP(Context ctx, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sp.getString(key, "");
    }
}
