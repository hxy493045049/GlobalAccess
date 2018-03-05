package com.msy.globalaccess.business.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;

import java.util.List;

import cn.msy.zc.commonutils.StringUtils;

/**
 * Created by WuDebin on 2017/3/16.
 */

public class TravelAttractionsAdapter extends BaseQuickAdapter<TripScenicBean, BaseViewHolder> {

    TravelAttractionsCallback callback;

    private int parentPosition;

    private int areaAdultsAmount;

    private int areaChildrenAmout;

    public TravelAttractionsAdapter(int layoutResId, List<TripScenicBean> data,int parentPosition,TravelAttractionsCallback callback,int areaAdultsAmount,int areaChildrenAmout) {
        super(layoutResId, data);
        this.callback=callback;
        this.parentPosition=parentPosition;
        this.areaAdultsAmount=areaAdultsAmount;
        this.areaChildrenAmout=areaChildrenAmout;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TripScenicBean item) {

        helper.setText(R.id.tv_travel_title,item.getTripName());

        helper.setText(R.id.tv_money,"￥"+StringUtils.getMsyPrice(item.getPrePayMoney()));

        if(item.getAuditType()!=null){
            if(item.getAuditType().equals("0")){
                helper.setText(R.id.tv_audit_type,"不结算");
            }else if(item.getAuditType().equals("1")){
                helper.setText(R.id.tv_audit_type,"实时结算");
            }else{
                helper.setText(R.id.tv_audit_type,"");
            }
        }else{
            helper.setText(R.id.tv_audit_type,"");
        }

        helper.setText(R.id.tv_people_num,"成人("+areaAdultsAmount+")     儿童("+areaChildrenAmout+")");
        String ticketContent="";
        for(int i=0;i<item.getTripScenicTicket().size();i++){
            TripScenicTicketBean tripScenicTicketBean=item.getTripScenicTicket().get(i);
            if(i>0)
            {
                ticketContent=ticketContent+"     ";
            }
            ticketContent=ticketContent+tripScenicTicketBean.getTicketPriceName();
            ticketContent=ticketContent+"("+tripScenicTicketBean.getAmount()+")";
        }
        helper.setText(R.id.tv_ticket_content,ticketContent);

        helper.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.editClick(item,helper.getAdapterPosition(),parentPosition);
            }
        });
        helper.getView(R.id.tv_deleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.deletedClick(item.getTripScenicId(),helper.getAdapterPosition(),parentPosition);
            }
        });
    }


     public interface TravelAttractionsCallback{
        void editClick(TripScenicBean item,int position,int parentPosition);
        void deletedClick(String tripScenicId,int position,int parentPosition);
    }
}
