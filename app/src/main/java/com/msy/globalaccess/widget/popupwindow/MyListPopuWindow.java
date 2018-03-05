package com.msy.globalaccess.widget.popupwindow;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListPopupWindow;

import com.msy.globalaccess.base.App;

/**
 * poplist
 * Created by chensh on 2017/1/22 0022.
 */

public class MyListPopuWindow extends ListPopupWindow {
    private MyListPopuWindow popuWindow;

    public MyListPopuWindow(Context context) {
        super(context, null);
        popuWindow = this;
    }

    public MyListPopuWindow(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        popuWindow = this;
    }

    public MyListPopuWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        popuWindow = this;
    }

    /**
     * 初始化
     *
     * @param Width      宽度
     * @param Height     高度
     * @param AnchorView 锚点
     * @param isRespond  是否使用物理键
     */
    public void initView(int Width, int Height, View AnchorView, boolean isRespond) {
        if (popuWindow != null) {
            setWidth(Width);//设置宽度
            setHeight(Height);//设置高度
            setAnchorView(AnchorView);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
            setModal(isRespond);//设置是否响应物理键
        }
    }

    /**
     * 显示pop
     *
     * @param view 锚点View
     */
    public void show(View view) {
        if (popuWindow != null) {
            //解决7.0时PopupWindow中的视图高度过大时，PopupWindow会占满全屏问题
            if (Build.VERSION.SDK_INT == 24) {
                int[] a = new int[2];
                view.getLocationInWindow(a);
                setHeight(App.getAppContext().getResources().getDisplayMetrics().heightPixels - a[1] - view.getHeight());
            }
            show();
        }
    }

    /**
     * 设置垂直、水平间距
     *
     * @param vertical   垂直
     * @param horizontal 水平
     */
    private void setOffset(int vertical, int horizontal) {
        if (popuWindow != null) {
            setHorizontalOffset(horizontal);//垂直间距
            setVerticalOffset(vertical);//水平间距
        }
    }
}
