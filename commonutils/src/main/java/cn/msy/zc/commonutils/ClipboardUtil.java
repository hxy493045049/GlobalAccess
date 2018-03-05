package cn.msy.zc.commonutils;

import android.content.ClipData;
import android.content.Context;

/**
 * 剪切板工具
 * Created by chensh on 2017/1/5 0005.
 */
public class ClipboardUtil {
    /**
     * 设置剪切板内容
     *
     * @param context     上下文
     * @param copycontent 复制内容
     */
    public static void setClip(Context context, String copycontent) {
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("Label", copycontent));
        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setText(copycontent);
        }
    }


}
