package com.msy.globalaccess.business.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamMemberSourceBean;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/2/7.
 * description:团队详情--客源地信息Adpater
 */
public class TeamTouristSourceAdapter extends BaseQuickAdapter<TeamMemberSourceBean, BaseViewHolder> {

    public TeamTouristSourceAdapter(int layoutResId, ArrayList<TeamMemberSourceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamMemberSourceBean teamMemberSourceBean) {
        if(getData().size() == 1){
            helper.setVisible(R.id.team_detail_source_item_tv_title,false);
        }
        helper.setText(R.id.team_detail_source_item_tv_placeInfo, teamMemberSourceBean.getPlaceInfo())
                .setText(R.id.team_detail_source_item_tv_adultAmount, teamMemberSourceBean.getAdultAmount() + "位")
                .setText(R.id.team_detail_source_item_tv_childAmount, teamMemberSourceBean.getChildAmount() + "位")
                .setText(R.id.team_detail_source_item_tv_remark, teamMemberSourceBean.getRemark())
                .setText(R.id.team_detail_source_item_tv_title,"客源地" + (helper.getAdapterPosition()+1));
    }
}
