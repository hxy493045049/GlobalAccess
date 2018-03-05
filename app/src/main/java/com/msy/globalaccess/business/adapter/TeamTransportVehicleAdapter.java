package com.msy.globalaccess.business.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamTransportVehicleBean;

import java.util.ArrayList;

import cn.msy.zc.commonutils.StringUtils;

/**
 * Created by pepys on 2017/2/7.
 * description:团队详情--运输车辆信息Adpater
 */
public class TeamTransportVehicleAdapter extends BaseQuickAdapter<TeamTransportVehicleBean, BaseViewHolder> {

    public TeamTransportVehicleAdapter(int layoutResId, ArrayList<TeamTransportVehicleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamTransportVehicleBean item) {

        if(getData().size() == 1){
            helper.setVisible(R.id.team_detail_transport_tv_title,false);
        }else{
            helper.setVisible(R.id.team_detail_transport_tv_title,true);
        }
        helper.setText(R.id.team_detail_transport_tv_useTypeName,item.getUseTypeName())
                .setText(R.id.team_detail_transport_tv_companyName,item.getCompanyName())
                .setText(R.id.team_detail_transport_tv_driverPhone,item.getDriverPhone())
                .setText(R.id.team_detail_transport_tv_vehicleNum,item.getVehicleNum())
                .setText(R.id.team_detail_transport_tv_vehicleTypeName,item.getVehicleTypeName())
                .setText(R.id.team_detail_transport_tv_seatAmount,item.getSeatAmount())
                .setText(R.id.team_detail_transport_tv_prePayMoney, StringUtils.getMsyPrice(item.getPrePayMoney()))
                .setText(R.id.team_detail_transport_tv_comments,item.getComments())
                .setText(R.id.team_detail_transport_tv_title,"车辆" + (helper.getAdapterPosition()+1));
       /* helper.setText(R.id.item_transport_name, item.getName());
        helper.setText(R.id.item_transport_model, item.getType());
        Log.i("LoginPre", "convert");*/
    }
}
