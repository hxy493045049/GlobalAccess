package com.msy.globalaccess.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.msy.globalaccess.base.App;

import cn.msy.zc.commonutils.StackTraceUtils;

/**
 * Created by hxy on 2016/7/26 0026.
 * description :
 */
public class ApplicationUtils {

    private static final String TAG = ApplicationUtils.class.getSimpleName();

    /**
     * 获取App安装包信息
     *
     * @param ctx 上下文环境
     * @return 应用程序的信息
     * @throws Exception 当ctx为null时返回null
     */
    public static PackageInfo getPackageInfo(Context ctx) {
        StackTraceUtils.checkContext(ctx);
        Context appCtx = ctx.getApplicationContext();
        PackageInfo info = null;
        try {
            info = appCtx.getPackageManager().getPackageInfo(appCtx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取应用versionName 和 versionCode
     *
     * @return
     */
    public static String[] getVersion() {
        try {
            Context ctx = App.getAppContext();
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return new String[]{info.versionName,info.versionCode+""};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"",""};
        }
    }
}
