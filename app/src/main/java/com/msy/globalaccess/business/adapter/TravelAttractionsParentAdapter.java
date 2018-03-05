package com.msy.globalaccess.business.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsBean;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.utils.StringUtils;

import java.util.List;

import cn.msy.zc.commonutils.TimeFormat;

/**
 * Created by WuDebin on 2017/4/10.
 */

public class TravelAttractionsParentAdapter extends BaseQuickAdapter<TravelAttractionsBean, BaseViewHolder> {

    TravelAttractionsAdapter.TravelAttractionsCallback callback;
    TravelAttractionsParentCallback parentCallback;
    private List<TravelAttractionsBean> data;
    private int oldDay = 1;
    private int areaAdultsAmount;

    private int areaChildrenAmout;

    public TravelAttractionsParentAdapter(int layoutResId, List<TravelAttractionsBean> data,
                                          TravelAttractionsParentCallback parentCallback, TravelAttractionsAdapter
                                                  .TravelAttractionsCallback callback) {
        super(layoutResId, data);
        this.data = data;
        this.callback = callback;
        this.parentCallback = parentCallback;
    }

    public void setAreaChildrenAmout(int areaChildrenAmout) {
        this.areaChildrenAmout = areaChildrenAmout;
    }

    public void setAreaAdultsAmount(int areaAdultsAmount) {
        this.areaAdultsAmount = areaAdultsAmount;
    }

    @Override
    public void setNewData(List<TravelAttractionsBean> data) {
        this.oldDay = 1;
        this.data = data;
        setDays();
        super.setNewData(data);
    }

    public void setDays() {
        int days = 1;
        data.get(0).setDays(days);
        for (int i = 1; i < data.size(); i++) {
            if (TimeHelper.compareTime(data.get(i).getTripDate(), TimeFormat.regular7, data.get(i - 1).getTripDate(),
                    TimeFormat.regular7) == -1) {
                days++;
            }
            data.get(i).setDays(days);
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TravelAttractionsBean item) {
        helper.setText(R.id.tv_days, "第" + StringUtils.toHanzi(item.getDays()) + "天");
        helper.setText(R.id.tv_date, item.getTripDate());
        helper.setText(R.id.tv_address, item.getPlaceInfo());

        helper.getView(R.id.tv_add_attractions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentCallback.addAttractionsClick(helper.getAdapterPosition());
            }
        });

        RecyclerView recyclerView = helper.getView(R.id.rv_travel_attractions);
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager
                .VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        TravelAttractionsAdapter attractionsAdapter = new TravelAttractionsAdapter(R.layout.item_travel_attractions,
                item.getTripSceniclist(), helper.getAdapterPosition(), callback, areaAdultsAmount, areaChildrenAmout);
        recyclerView.setAdapter(attractionsAdapter);
    }

    public interface TravelAttractionsParentCallback {
        void addAttractionsClick(int parentPosition);
    }
}
