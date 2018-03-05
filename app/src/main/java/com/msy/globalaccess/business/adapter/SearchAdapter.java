package com.msy.globalaccess.business.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.search.SearchBean;

import java.util.List;

/**
 * Created by shawn on 2017/7/3 0003.
 * <p>
 * description :
 */

public class SearchAdapter extends BaseQuickAdapter<SearchBean, BaseViewHolder> {
    private static final String[] keys = App.getResource().getStringArray(R.array.touristAdminQueryCondition);
    private EditText editText;

    public SearchAdapter(@LayoutRes int layoutResId, @Nullable List<SearchBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SearchBean searchBean) {
        baseViewHolder.setText(R.id.settlement_text1, searchBean.getKey())
                .addOnClickListener(R.id.fl_settlement_text1)
                .addOnClickListener(R.id.fl_settlement_text2)
                .addOnClickListener(R.id.fl_settlement_text3)
                .addOnClickListener(R.id.settement_delete);

        if (keys[0].equals(searchBean.getKey())) {
            showSingleChoose(baseViewHolder, searchBean);
        } else if (keys[1].equals(searchBean.getKey())) {
            showSingleChoose(baseViewHolder, searchBean);
        } else if (keys[2].equals(searchBean.getKey())) {
            showText(baseViewHolder, searchBean);
        } else if (keys[3].equals(searchBean.getKey())) {
            showMultiTime(baseViewHolder, searchBean);
        }
    }

    private void showMultiTime(BaseViewHolder baseViewHolder, SearchBean searchBean) {
        baseViewHolder.setVisible(R.id.editText, false)
                .setVisible(R.id.fl_settlement_text2, true)
                .setVisible(R.id.fl_settlement_text3, true)
                .setVisible(R.id.split, true);


        if (!TextUtils.isEmpty(searchBean.getSelectedValue())) {
            //读取时间数据
            String[] times = searchBean.getTimes();
            if (!searchBean.getSelectedValue().contains(SearchBean.split)) {
                //如果不包含分隔号
                if (times != null && !TextUtils.isEmpty(times[0])) {//表示只有开始时间
                    baseViewHolder.setText(R.id.settlement_text2, times[0]);
                    baseViewHolder.setText(R.id.settlement_text3, "");
                } else {//或没有时间
                    baseViewHolder.setText(R.id.settlement_text2, "");
                    baseViewHolder.setText(R.id.settlement_text3, "");
                }
            } else {//如果包含分隔号
                if (times != null && times.length == 2) {//两个时间都有
                    baseViewHolder.setText(R.id.settlement_text2, times[0]);
                    baseViewHolder.setText(R.id.settlement_text3, times[1]);
                } else {//表示可能只有结束时间
                    baseViewHolder.setText(R.id.settlement_text2, "");
                    baseViewHolder.setText(R.id.settlement_text3, times != null ? times[0] : null);
                }
            }
        } else {
            baseViewHolder.setText(R.id.settlement_text2, "");
            baseViewHolder.setText(R.id.settlement_text3, "");
        }
    }

    //团队编号
    private void showText(BaseViewHolder baseViewHolder, final SearchBean searchBean) {
        baseViewHolder.setVisible(R.id.editText, true)
                .setVisible(R.id.fl_settlement_text2, false)
                .setVisible(R.id.fl_settlement_text3, false)
                .setVisible(R.id.split, false);

        editText = baseViewHolder.getView(R.id.editText);
        editText.requestFocus();
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (!TextUtils.isEmpty(searchBean.getSelectedValue())) {
            editText.setText(searchBean.getSelectedValue());
        } else {
            editText.setText("");
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchBean.setSelectedValue(editText.getText().toString().trim());
                }
            }
        });

    }

    private void showSingleChoose(BaseViewHolder baseViewHolder, SearchBean searchBean) {
        baseViewHolder.setVisible(R.id.editText, false)
                .setVisible(R.id.fl_settlement_text2, true)
                .setVisible(R.id.fl_settlement_text3, false)
                .setVisible(R.id.split, false);

        if (!TextUtils.isEmpty(searchBean.getSelectedValue())) {
            baseViewHolder.setText(R.id.settlement_text2, searchBean.getSelectedValue());
        } else {
            baseViewHolder.setText(R.id.settlement_text2, "");
        }
    }

    /**
     * 这个方法会导致et的onfocusChange,记录et的选中内容
     */
    public void saveDate() {
        if (editText != null) {
            editText.clearFocus();
        }
    }

    public void clearEditText() {
        if (editText != null) {
            editText.setText("");
        }
    }
}
