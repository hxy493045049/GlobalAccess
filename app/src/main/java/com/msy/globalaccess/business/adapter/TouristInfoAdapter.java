package com.msy.globalaccess.business.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamMemberBean;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/2/14.
 * description:团队详情--查看游客名单信息Adpater
 */
public class TouristInfoAdapter extends BaseQuickAdapter<TeamMemberBean, BaseViewHolder> {

    public TouristInfoAdapter(int layoutResId, ArrayList<TeamMemberBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamMemberBean teamMemberBean) {
        if( null == teamMemberBean.getPhoneNum() || teamMemberBean.getPhoneNum().equals("")){
            helper.getView(R.id.check_tourist_item_ll_phone).setVisibility(View.INVISIBLE);
        }else{
            helper.setText(R.id.check_tourist_item_tv_phone,teamMemberBean.getPhoneNum());
        }
        helper.setText(R.id.check_tourist_item_tv_name,teamMemberBean.getName())
                .setText(R.id.check_tourist_item_tv_sex,teamMemberBean.getSex()==0?"女":"男")
                .setText(R.id.check_tourist_item_tv_sequence,(helper.getAdapterPosition()+1)+"")
                .setText(R.id.check_tourist_item_tv_birthDay,teamMemberBean.getBirthdate())
                .setText(R.id.check_tourist_item_tv_card,teamMemberBean.getCardTypeName())
                .setText(R.id.check_tourist_item_tv_number,teamMemberBean.getCardNum())
                .addOnClickListener(R.id.check_tourist_item_ll_phone);

    }
}
