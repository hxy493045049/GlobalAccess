package com.msy.globalaccess.widget.dialog;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.NumberCodeView;

/**
 * Created by hxy on 2016/12/27 0027.
 * <p>
 * description :
 */
public class PasswordPopWindow extends PopupWindow {
    private View anchor;
    private NumberCodeView numberCodeView;

    /**
     * 弹出支付密码输入框
     *
     * @param codeCallback 回调
     */
    public PasswordPopWindow(View anchor, final NumberCodeView.OnInputNumberCodeCallback codeCallback) {
        this.anchor = anchor;
        View view = View.inflate(App.getAppContext(), R.layout.popupwindow_payment_password, null);
        numberCodeView = (NumberCodeView) view.findViewById(R.id.number_code_View);
        numberCodeView.setHintVisible(View.GONE);
        numberCodeView.setNumberCodeCallback(new NumberCodeView.OnInputNumberCodeCallback() {
            @Override
            public void onResult(final String code) {
                if (null != codeCallback) {
                    codeCallback.onResult(code);
                }
            }

            @Override
            public void onForget() {
                if (null != codeCallback) {
                    codeCallback.onForget();
                }
                dismiss();
            }

            @Override
            public void onClose() {
                if (null != codeCallback) {
                    codeCallback.onClose();
                }
                dismiss();
            }
        });

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        update();

    }

    public void show() {
        View v = anchor.findViewById(android.R.id.content);
        if (v == null) {
            v = anchor;
            do {
                v = (View) v.getParent();
                if (v != null && v.getId() == android.R.id.content) {
                    break;
                }
            } while (v != null);
        }
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

    public void restore(){
        numberCodeView.restoreViews();
    }
}
