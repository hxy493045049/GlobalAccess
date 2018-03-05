package com.msy.globalaccess.business.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.navigation.NavigationListBean;

import java.util.List;

/**
 * Created by WuDebin on 2017/2/6.
 */

public class GuideAdapter extends BaseQuickAdapter<NavigationListBean, BaseViewHolder> implements View.OnClickListener {


    public GuideAdapter(int layoutResId, List<NavigationListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationListBean item) {
        helper.setText(R.id.tv_guide_title, item.getTitle());
        if (item.getSubListBean() != null && item.getSubListBean().size() > 0) {
            helper.setText(R.id.tv_guide_description1, item.getSubListBean().get(0).getDescription());
            helper.setText(R.id.tv_guide_content1, item.getSubListBean().get(0).getContent());
            helper.setText(R.id.tv_guide_description2, item.getSubListBean().get(1).getDescription());
            helper.setText(R.id.tv_guide_content2, item.getSubListBean().get(1).getContent());
            helper.setText(R.id.tv_guide_description3, item.getSubListBean().get(2).getDescription());
            helper.setText(R.id.tv_guide_content3, item.getSubListBean().get(2).getContent());
        }

    }

    @Override
    public void onClick(View v) {

    }
}

