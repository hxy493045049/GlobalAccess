package cn.msy.zc.commonutils;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by WuDebin on 2016/7/13.
 */
public class KeyBoardUtil {
    /**
     * 隐藏输入法
     *
     * @param paramContext
     * @param paramEditText
     */
    public static void hideSoftKeyboard(Context paramContext,
                                        EditText paramEditText) {
        ((InputMethodManager) paramContext
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     *
     * @param paramContext
     * @param paramEditText
     */
    public static void showSoftKeyborad(Context paramContext,
                                        EditText paramEditText) {
        ((InputMethodManager) paramContext
                .getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(
                paramEditText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 实现文本复制功能
     * @param content
     */
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
    /**
     * 实现粘贴功能
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}
