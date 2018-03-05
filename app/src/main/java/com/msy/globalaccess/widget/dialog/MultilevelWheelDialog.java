package com.msy.globalaccess.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.base.CurrencyBean;
import com.msy.globalaccess.data.bean.search.SearchValueListBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.adapter.AbstractWheelTextAdapter;
import com.msy.globalaccess.widget.wheelview.OnWheelChangedListener;
import com.msy.globalaccess.widget.wheelview.OnWheelClickedListener;
import com.msy.globalaccess.widget.wheelview.WheelView;

import java.util.ArrayList;

public class MultilevelWheelDialog extends Dialog {

    private final static int DEFAULT_ITEMS = 5;
    private final static int UPDATE_CITY_WHEEL = 11;

    private Activity mContext;
    private ArrayList<SearchValueListBean> allList = new ArrayList<>();
    private ArrayList<CurrencyBean> values = new ArrayList<>();
    AbstractWheelTextAdapter firstAdapter;
    AbstractWheelTextAdapter valueAdapter;
    WheelView firstWheel;
    WheelView valuesWheel;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isShowing()) {
                return;
            }
            switch (msg.what) {
                case UPDATE_CITY_WHEEL:
                    values.clear();
                    if (null != allList.get(msg.arg1).getData()) {
                        values.addAll(allList.get(msg.arg1).getData());
                    }
                    valuesWheel.invalidateWheel(true);
                    valuesWheel.setCurrentItem(0, false);

                    break;
                default:
                    break;
            }
        }
    };


    public interface MultilevelWheelListener {
        void onPicked(CurrencyBean currencyBean, View view);
    }

    public MultilevelWheelDialog(Activity context, ArrayList<SearchValueListBean> serviceAreaList, SearchValueListBean defaultfirst, CurrencyBean defaultBean, final MultilevelWheelListener listener, final View textView2) {
        super(context);

        mContext = context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getWindow().setWindowAnimations(R.style.AnimBottom);
        View rootView = getLayoutInflater().inflate(R.layout.dialog_city_picker, null);
        int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
        LayoutParams params = new LayoutParams(screenWidth, LayoutParams.MATCH_PARENT);
        super.setContentView(rootView, params);

        allList.addAll(serviceAreaList);

        initViews();
        setDefaultArea(defaultfirst, defaultBean);

        View done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    CurrencyBean city = values.size() > 0 ? values.get(valuesWheel.getCurrentItem()) : null;
                    if(city == null){
                        ToastUtils.showToast("请选择有效数据");
                        return;
                    }
                    listener.onPicked(city, textView2);
                }
                dismiss();
            }
        });

        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void setDefaultArea(SearchValueListBean defaultfirst, CurrencyBean secondary) {

        int provinceItem = 0;
        int cityItem = 0;

        if (defaultfirst == null) {
            defaultfirst = allList.get(0);
            provinceItem = 0;
        } else {
            for (int i = 0; i < allList.size(); i++) {
                if (allList.get(i).getItem() == defaultfirst.getItem()) {
                    provinceItem = i;
                    break;
                }
            }
        }
        values.clear();
        if (null != defaultfirst.getData()) {
            values.addAll(defaultfirst.getData());
        }
        if (values.size() == 0) {
            values.add(new CurrencyBean());
            cityItem = 0;
        } else if (secondary == null) {
            secondary = values.get(0);
            cityItem = 0;
        } else {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).getId() == secondary.getId()) {
                    cityItem = i;
                    break;
                }
            }
        }
        firstWheel.setCurrentItem(provinceItem, false);
        valuesWheel.setCurrentItem(cityItem, false);
    }

    private void initViews() {

        firstWheel = (WheelView) findViewById(R.id.provinceWheel);
        valuesWheel = (WheelView) findViewById(R.id.valuesWheel);


        firstAdapter = new AbstractWheelTextAdapter(mContext, R.layout.wheel_text) {

            @Override
            public int getItemsCount() {

                return allList.size();
            }

            @Override
            protected CharSequence getItemText(int index) {

                return allList.get(index).getName();
            }
        };

        valueAdapter = new AbstractWheelTextAdapter(mContext, R.layout.wheel_text) {

            @Override
            public int getItemsCount() {

                return values.size();
            }

            @Override
            protected CharSequence getItemText(int index) {

                return values.get(index).getName();
            }
        };
        firstWheel.setViewAdapter(firstAdapter);
        firstWheel.setCyclic(false);
        firstWheel.setVisibleItems(DEFAULT_ITEMS);

        valuesWheel.setViewAdapter(valueAdapter);
        valuesWheel.setCyclic(false);
        valuesWheel.setVisibleItems(DEFAULT_ITEMS);

        OnWheelClickedListener clickListener = new OnWheelClickedListener() {

            @Override
            public void onItemClicked(WheelView wheel, int itemIndex) {
                if (itemIndex != wheel.getCurrentItem()) {
                    wheel.setCurrentItem(itemIndex, true, 500);
                }
            }
        };
        firstWheel.addClickingListener(clickListener);
        valuesWheel.addClickingListener(clickListener);

        firstWheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                mHandler.removeMessages(UPDATE_CITY_WHEEL);
                Message msg = Message.obtain();
                msg.what = UPDATE_CITY_WHEEL;
                msg.arg1 = newValue;
                mHandler.sendMessageDelayed(msg, 50);
            }
        });

    }
}
