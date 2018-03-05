package com.msy.globalaccess.business.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;

import java.util.List;

/**
 * Created by shawn on 2017/5/16 0016.
 * <p>
 * description :
 */

public class NestDropdownAdapter extends BaseQuickAdapter<TourismDropListBean.DropSelectableBean, BaseViewHolder> {
    public NestDropdownAdapter(@Nullable List<TourismDropListBean.DropSelectableBean> data) {
        super(R.layout.item_grid_cell, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TourismDropListBean.DropSelectableBean s) {
        AppCompatTextView tvCell = baseViewHolder.getView(R.id.tvCell);
        tvCell.setSelected(s.isSeleted());

        baseViewHolder.setText(R.id.tvCell, s.getName()).addOnClickListener(R.id.tvCell);
    }
}
