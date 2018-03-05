package cn.msy.zc.commonutils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

/**
 * Created by hxy on 2016/8/19 0019.
 * description : 校验权限的工具类
 */
public class PermissionUtils {
    /**
     * 校验是否允许读取sd
     *
     * @param context 上下文对象
     *
     * @return 有sd且有权限:true;  反之false
     */
    public static boolean hasExternalStoragePermission(Context context) {
        boolean hasSD = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED && hasSD;
    }
}
