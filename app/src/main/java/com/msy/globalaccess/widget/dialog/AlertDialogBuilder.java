package com.msy.globalaccess.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.msy.globalaccess.R;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * 这个类耦合度太高,不建议使用
 */
public class AlertDialogBuilder {
    private Dialog dialog;
    private Context mContext;
    private View layout;
    private LayoutInflater mInflater;
    private String positiveButtonText, negativeButtonText;
    private DialogInterface.OnClickListener positiveListener, negativeListener;
    private int mContentId;

    public AlertDialogBuilder(Context mContext) {
        this.mContext = mContext;
        init();
    }

    public AlertDialogBuilder setContentLayout(int contentId) {
        mContentId = contentId;
        return this;
    }

    public Dialog create() {
        initView(layout, dialog);
        dialog.setContentView(layout, new ViewGroup.LayoutParams(DisplayUtil.getScreenWidth(), ViewGroup.LayoutParams
                .WRAP_CONTENT));
        return dialog;
    }

    public AlertDialogBuilder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
        this.positiveButtonText = positiveButtonText;
        this.positiveListener = listener;
        return this;
    }

    public AlertDialogBuilder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
        this.negativeButtonText = negativeButtonText;
        this.negativeListener = listener;
        return this;
    }

    private void initView(View layout, final Dialog dialog) {
        layout.findViewById(R.id.tvTitle).setVisibility(View.GONE);
        layout.findViewById(R.id.alert_dialog_ll_btn).setVisibility(View.VISIBLE);
        // 设置确定按钮文字
        if (positiveButtonText != null) {
            ((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            if (positiveListener != null) {
                layout.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }
        } else {
            layout.findViewById(R.id.positiveButton).setVisibility(
                    View.GONE);
        }
        // 设置取消按钮文字显示
        if (negativeButtonText != null) {
            ((Button) layout.findViewById(R.id.negativeButton))
                    .setText(negativeButtonText);
            if (negativeListener != null) {
                (layout.findViewById(R.id.negativeButton))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                dialog.dismiss();
                            }
                        });
            }
        } else {
            layout.findViewById(R.id.llNegative).setVisibility(
                    View.GONE);
        }
        LinearLayout content = (LinearLayout) layout.findViewById(R.id.content);
        content.removeAllViews();
        if (mContentId == 0) {
            mInflater.inflate(R.layout.dialog_pwd, content, true);
        } else {
            mInflater.inflate(mContentId, content, true);
        }
    }

    private void init() {
        dialog = new Dialog(mContext, R.style.my_dialog);
        mInflater = LayoutInflater.from(mContext);
        layout = mInflater.inflate(R.layout.popup_alert_dialog, null);

    }
}
