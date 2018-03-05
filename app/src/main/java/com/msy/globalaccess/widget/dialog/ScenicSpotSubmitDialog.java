package com.msy.globalaccess.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * 编辑/新增景点dialog
 * Created by chensh on 2017/3/20 0020.
 */

public class ScenicSpotSubmitDialog extends Dialog {
    public ScenicSpotSubmitDialog(Context context) {
        super(context);
    }

    ScenicSpotSubmitDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private int titleSize, messageSize, positiveButtonTextSize, negativeButtonTextSize;
        private TextView number_little;
        private LinearLayout prepay_lin;
        private View contentView;
        private TextView dialog_prepay;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public ScenicSpotSubmitDialog.Builder setMessage(String message, int messageSize) {
            this.message = message;
            if (messageSize > 0) {
                this.messageSize = messageSize;
            }
            return this;
        }

        public ScenicSpotSubmitDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public ScenicSpotSubmitDialog.Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public ScenicSpotSubmitDialog.Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置标题文字和文字的大小
         *
         * @param title
         * @param tv_size 单位是dp
         * @return
         */
        public ScenicSpotSubmitDialog.Builder setTitle(String title, int tv_size) {
            this.title = title;
            if (tv_size > 0)
                titleSize = tv_size;
            return this;
        }

        public ScenicSpotSubmitDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public ScenicSpotSubmitDialog.Builder setPositiveButton(int positiveButtonText,
                                                                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public ScenicSpotSubmitDialog.Builder setPositiveButton(String positiveButtonText,
                                                                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public ScenicSpotSubmitDialog.Builder setNegativeButton(int negativeButtonText,
                                                                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ScenicSpotSubmitDialog.Builder setNegativeButton(String negativeButtonText,
                                                                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public PopUpWindowAlertDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //自定义透明背景的弹出窗
            final PopUpWindowAlertDialog dialog = new PopUpWindowAlertDialog(context,
                    R.style.my_dialog);
            View layout = inflater.inflate(R.layout.dialog_scenic_spot, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            number_little = (TextView) layout.findViewById(R.id.number_little);
            prepay_lin = (LinearLayout) layout.findViewById(R.id.prepay_lin);
            dialog_prepay = (TextView) layout.findViewById(R.id.dialog_prepay);

            // 设置弹出窗标题
            if (title != null) {
                TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
                tvTitle.setText(title);
                if (titleSize > 0)
                    tvTitle.setTextSize(titleSize);
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                layout.findViewById(R.id.tvTitle).setVisibility(View.GONE);
            }
            // 设置确定按钮文字
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
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
                if (negativeButtonClickListener != null) {
                    (layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                    dialog.dismiss();
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.llNegative).setVisibility(
                        View.GONE);
            }

            if (message != null) {
                TextView tvMessage = (TextView) layout.findViewById(R.id.tvMessage);
                tvMessage.setText(message);
                if (messageSize > 0)
                    tvMessage.setTextSize(messageSize);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                (layout.findViewById(R.id.content)).setVisibility(View.GONE);
            }
            dialog.show();
            dialog.setContentView(layout, new ViewGroup.LayoutParams(DisplayUtil.getScreenWidth(), DisplayUtil
                    .getScreenHeight()));
            show(dialog);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            return dialog;
        }

        //显示票数小于人数文字提示
        public void ShowNumberView() {
            if (number_little != null) {
                number_little.setVisibility(View.VISIBLE);
            }
        }

        //隐藏票数小于人数文字提示
        public void GoneNumberView() {
            if (number_little != null) {
                number_little.setVisibility(View.GONE);
            }
        }

        //隐藏预付金额布局
        public void GonePrepayLin() {
            if (prepay_lin != null) {
                prepay_lin.setVisibility(View.GONE);
            }
        }

        //隐藏预付金额布局
        public void showPrepayLin(String money) {
            if (prepay_lin != null) {
                prepay_lin.setVisibility(View.VISIBLE);
                dialog_prepay.setText("￥ " +money);
            }
        }

        private void show(PopUpWindowAlertDialog dialog) {
            dialog.show();
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = DisplayUtil.getScreenWidth();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }
}
