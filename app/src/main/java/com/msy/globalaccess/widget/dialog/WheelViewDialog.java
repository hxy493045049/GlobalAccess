package com.msy.globalaccess.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.base.CurrencyBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.search.PerequisiteBean;
import com.msy.globalaccess.data.bean.search.SearchBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.bean.TravelDepBean;
import com.msy.globalaccess.data.holder.TravelHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.adapter.AbstractWheelTextAdapter;
import com.msy.globalaccess.widget.wheelview.OnWheelChangedListener;
import com.msy.globalaccess.widget.wheelview.OnWheelClickedListener;
import com.msy.globalaccess.widget.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 滚轮选择器
 * Created by chensh on 2016/12/7.
 */
public class WheelViewDialog<T> extends Dialog {
    private final static int DEFAULT_ITEMS = 5;
    AbstractWheelTextAdapter wheelTextAdapter;
    WheelView wheel_view;

    private AppCompatEditText edt_search;
    private Activity mContext;
    private ArrayList<T> mDateList;

    private T currentItem;
    private int currentPosition;
    private View rootView;
    private String searchEdtText = "";
    private int searchPosition = 0;

    @SuppressWarnings("unchecked")
    public WheelViewDialog(Activity context, List<T> list, final View view, final onWheelViewPickedListener listener) {
        super(context);
        mContext = context;
        this.mDateList = new ArrayList<>(list);
        int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getWindow().setWindowAnimations(R.style.AnimBottom);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        rootView = getLayoutInflater().inflate(R.layout.dialog_wheelview, null);
        setContentView(rootView, params);
        currentItem = list.get(0);
        initViews();
        setDefaultArea(currentItem);

        edt_search = (AppCompatEditText) rootView.findViewById(R.id.edt_search);
        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInput();
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!searchEdtText.equals(s.toString())) {
                    searchPosition = 0;
                }
                searchEdtText = s.toString();
                mDateList = (ArrayList<T>) TravelHelper.getInstance().getKeyList(searchEdtText);
                if (mDateList.size() > 0) {
                    currentItem = mDateList.get(0);
                }
                wheel_view.invalidateWheel(true);
                wheel_view.setCurrentItem(searchPosition, false);
            }
        });

        View done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDateList.size() == 0) {
                    ToastUtils.showToast("请输入正确的名称");
                    return;
                }
                if (listener != null) {
                    if (currentItem instanceof TravelAgentListBean) {
                        //是为了在旅行社搜索的时候把搜索框的值传回去
                        ((TravelAgentListBean) currentItem).setSearchText(searchEdtText);
                        ((TravelAgentListBean) currentItem).setSearchPosition(currentPosition);
                    }
                    listener.onPicked(currentItem, view);
                }
                hideInput();
                dismiss();
            }
        });

        View cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideInput();
                dismiss();
            }
        });

    }

    @SuppressWarnings("unchecked")
    public WheelViewDialog(Activity context, List<T> list, final onWheelViewPickedListener1 listener) {
        super(context);
        mContext = context;
        this.mDateList = new ArrayList<>(list);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        getWindow().setWindowAnimations(R.style.AnimBottom);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        View rootView = getLayoutInflater().inflate(R.layout.dialog_wheelview, null);
        int screenWidth = mContext.getWindowManager().getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        super.setContentView(rootView, params);

        if (list.size() > 0) {
            currentItem = list.get(0);
            initViews();
            setDefaultArea(currentItem);
        }


        View done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPicked(currentItem, currentPosition);
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

    @Override
    public boolean isShowing() {
        return super.isShowing();
    }

    public void setSearchShow(boolean ishow, String edtText, int psotion) {
        if (ishow) {
            edt_search.setVisibility(View.VISIBLE);
            searchPosition = psotion;
            searchEdtText = edtText;
            edt_search.setText(edtText);
            edt_search.setSelection(edtText.length());
        } else {
            edt_search.setVisibility(View.GONE);
        }
    }

    /**
     * 设置默认item
     *
     * @param defaultItems
     */
    public void setDefaultItems(T defaultItems) {
        if (mDateList != null && wheel_view != null) {
            setDefaultArea(defaultItems);
        }
    }

    private void setDefaultArea(T defaultItem) {
        if (defaultItem != null) {
            for (int i = 0; i < mDateList.size(); i++) {
                if (mDateList.get(i).equals(defaultItem)) {
                    wheel_view.setCurrentItem(i, false);
                }
            }
        } else {
            wheel_view.setCurrentItem(0, false);
        }
    }

    public void setSelectedPosition(int pos) {
        if (wheel_view != null && pos > 0 && pos < mDateList.size()) {
            wheel_view.setCurrentItem(pos, false);
        }
    }

    private void initViews() {

        wheel_view = (WheelView) findViewById(R.id.wheel_view);

        wheelTextAdapter = new AbstractWheelTextAdapter(mContext, R.layout.wheel_text) {
            @Override
            public int getItemsCount() {
                return mDateList.size();
            }

            @Override
            protected CharSequence getItemText(int index) {
                T object = mDateList.get(index);
                if (object instanceof String) {
                    return (String) mDateList.get(index);
                } else if (object instanceof SearchTeamTypeBean.TeamTypeListBean) {
                    return ((SearchTeamTypeBean.TeamTypeListBean) mDateList.get(index)).getTeamTypeName();
                } else if (object instanceof PerequisiteBean) {
                    return ((PerequisiteBean) mDateList.get(index)).getName();
                } else if (object instanceof TravelDepBean.TravelDepListBean) {
                    return ((TravelDepBean.TravelDepListBean) mDateList.get(index)).getTravelDepName();
                } else if (object instanceof CurrencyBean) {
                    return ((CurrencyBean) mDateList.get(index)).getName();
                } else if (object instanceof KeyMapBean) {
                    return ((KeyMapBean) mDateList.get(index)).getMapValue();
                } else if (object instanceof CityBean) {
                    return ((CityBean) mDateList.get(index)).getCityName();
                } else if (object instanceof TravelAgentListBean) {
                    return ((TravelAgentListBean) mDateList.get(index)).getTravelAgentName();
                } else if (object instanceof SearchBean) {
                    return ((SearchBean) mDateList.get(index)).getKey();
                } else if (object instanceof IWheelKey) {
                    return ((IWheelKey) object).getKey();
                } else {
                    return "";
                }
            }
        };
        wheel_view.setViewAdapter(wheelTextAdapter);
        wheel_view.setCyclic(false);
        wheel_view.setVisibleItems(DEFAULT_ITEMS);

        OnWheelClickedListener clickListener = new OnWheelClickedListener() {

            @Override
            public void onItemClicked(WheelView wheel, int itemIndex) {
                if (itemIndex != wheel.getCurrentItem()) {
                    wheel.setCurrentItem(itemIndex, true, 300);
                }
            }
        };

        wheel_view.addClickingListener(clickListener);

        wheel_view.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                currentItem = mDateList.get(newValue);
                currentPosition = newValue;
            }
        });

    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        }
    }

    private void showInput() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(rootView, InputMethodManager.SHOW_FORCED);

    }

    public interface onWheelViewPickedListener {
        void onPicked(Object pickedItem, View view);
    }

    public interface onWheelViewPickedListener1<T> {
        void onPicked(T pickedItem, int position);
    }

    public interface IWheelKey {
        String getKey();
    }
}
