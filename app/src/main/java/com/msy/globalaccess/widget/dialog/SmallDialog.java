package com.msy.globalaccess.widget.dialog;

import android.app.Activity;
import android.content.Context;

import com.msy.globalaccess.R;
import com.orhanobut.logger.Logger;


public class SmallDialog extends CustomerDialogNoTitle {
    private static String TAG = SmallDialog.class.getSimpleName();
    private Context context;

    public SmallDialog(Context context) {
        super(context, R.style.myDialog, R.layout.small_dailog, context.getResources().getString(R.string
                .small_dialog));
        this.context = context;
    }

    public SmallDialog(Context context, String title) {
        super(context, R.style.myDialog, R.layout.small_dailog, title);
        this.context = context;
    }

    public SmallDialog(Context context, String title, float margin) {
        super(context, R.style.myDialog, R.layout.small_dailog, margin, title);
        this.context = context;
    }

    /**
     * 显示loading
     */
    public void shows() {
        if (context != null) {
            try {
                if (context instanceof Activity && !((Activity) context).isFinishing()) {
                    this.show();
                }
            } catch (Exception e) {
                Logger.e(TAG, "e   " + e.toString());
            }
        }
    }

    /**
     * 关掉loading
     */
    public void dismisss() {
        if (context != null) {
            try {
                if (context instanceof Activity && !((Activity) context).isFinishing()) {
                    this.dismiss();
                }
            } catch (Exception e) {
                Logger.e(TAG, "e   " + e.toString());
            }
        }
    }
}
