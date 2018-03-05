package com.msy.globalaccess.business.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;

import java.util.List;

/**
 * Created by WuDebin on 2017/5/16.
 */

public class SearchTravelAdapter extends BaseQuickAdapter<TravelAgentListBean, BaseViewHolder> {

    public SearchTravelAdapter(@LayoutRes int layoutResId, @Nullable List<TravelAgentListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TravelAgentListBean item) {
        helper.setText(R.id.tv_travel_name,item.getTravelAgentName());
        helper.setVisible(R.id.iv_status,item.isChecked());
        if(item.isChecked()){
            helper.setTextColor(R.id.tv_travel_name, App.getResource().getColor(R.color.colorPrimary));
        }else{
            helper.setTextColor(R.id.tv_travel_name, App.getResource().getColor(R.color.text_black_color));
        }
    }
}
