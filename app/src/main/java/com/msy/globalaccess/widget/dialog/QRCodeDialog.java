package com.msy.globalaccess.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.msy.globalaccess.R;
import com.msy.globalaccess.utils.QRCodeUtils;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * Created by pepys on 2017/2/10.
 * description:弹出条形码
 */
public class QRCodeDialog {

    public Dialog dialog;
    public Context mContext;
    public LayoutInflater mInflater;
    public View layout;
    private ImageView iv_qrcode;
    private ImageView qrcode_close;
    private String msg;//要转换成二维码的文本
    private int imageWidth = 0;
    private int imageHeight = 0;
    public QRCodeDialog(Context context, String msg) {
        this.mContext = context;
        this.msg = msg;
        init();
        initView();
        initData();
    }

    public void init() {
        dialog = new Dialog(mContext, R.style.my_dialog);
    }

    public void initView() {
        mInflater = LayoutInflater.from(mContext);
        layout = mInflater.inflate(R.layout.dialog_qrcode, null);
        iv_qrcode = (ImageView) layout.findViewById(R.id.iv_qrcode);
        qrcode_close = (ImageView) layout.findViewById(R.id.qrcode_close);
        qrcode_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        int width = DisplayUtil.getScreenWidth();
        int padWidth = DisplayUtil.dip2px(100);
        int layoutWidth = width - padWidth;
        int layoutHeight = layoutWidth*2/3;
        imageWidth = layoutWidth - DisplayUtil.dip2px(48);
        imageHeight = layoutHeight - DisplayUtil.dip2px(48);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void initData() {
        Bitmap bitmap = null;
        try {
            bitmap = QRCodeUtils.creatBarCode(mContext, msg,imageWidth,imageHeight,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            iv_qrcode.setImageBitmap(bitmap);
        }
    }

    //显示dialog
    public void show() {
        dialog.show();
    }

    //关闭dialog
    public void dismiss() {
        dialog.dismiss();
    }

}
