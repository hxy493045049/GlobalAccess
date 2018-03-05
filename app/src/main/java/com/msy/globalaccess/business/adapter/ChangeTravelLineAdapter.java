package com.msy.globalaccess.business.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;

import java.util.ArrayList;


/**
 * Created by pepys on 2017/3/16.
 * description:
 */
public class ChangeTravelLineAdapter extends BaseQuickAdapter<TeamLineInfoBean,BaseViewHolder> {
    Context mContext;
    public ChangeTravelLineAdapter(Context context, int layoutResId, ArrayList<TeamLineInfoBean> lineListInfo) {
        super(layoutResId, lineListInfo);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, TeamLineInfoBean item) {
        //本来默认开始是0，因为有head.默认会加一
        int position = viewHolder.getAdapterPosition()-1;
        if(position > 0){
            ArrayList<TeamLineInfoBean> lineListInfo = (ArrayList<TeamLineInfoBean>) getData();
            TeamLineInfoBean teamLineInfo = lineListInfo.get(position-1);
            if(teamLineInfo.getTripDate().equals(item.getTripDate())){
                item.setDataPosition(teamLineInfo.getDataPosition());
            }else{
                item.setDataPosition((teamLineInfo.getDataPosition()+1));
            }
        }else{
            item.setDataPosition((position+1));
        }


        viewHolder.setText(R.id.change_travel_tv_title,"第"+item.getDataPosition()+"天  "+item.getTripDate())
                  .setText(R.id.change_travel_tv_county,item.getPlaceInfo());
    }
}
