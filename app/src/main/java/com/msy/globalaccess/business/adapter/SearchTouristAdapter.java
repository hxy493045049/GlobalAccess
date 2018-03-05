package com.msy.globalaccess.business.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;

import java.util.List;

/**
 * Created by pepys on 2017/5/18.
 * description:搜索景点
 */
public class SearchTouristAdapter extends BaseQuickAdapter<ScenicListBean, BaseViewHolder> {

    public SearchTouristAdapter(@LayoutRes int layoutResId, @Nullable List<ScenicListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenicListBean item) {
        helper.setText(R.id.tv_travel_name,item.getScenicName());
        helper.setVisible(R.id.iv_status,item.isChecked());
        if(item.isChecked()){
            helper.setTextColor(R.id.tv_travel_name, App.getResource().getColor(R.color.colorPrimary));
        }else{
            helper.setTextColor(R.id.tv_travel_name, App.getResource().getColor(R.color.text_black_color));
        }
    }
}
