package com.msy.globalaccess.business.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;

import java.util.ArrayList;



/**
 * Created by pepys on 2017/3/16.
 * description:
 */
public class TouristSpotsAdapter extends BaseQuickAdapter<ScenicListBean,BaseViewHolder> {

    Context mContext;
    private int text_primary_light;
    public TouristSpotsAdapter(Context context, int layoutResId, ArrayList<ScenicListBean> lineListInfo) {
        super(layoutResId, lineListInfo);
        mContext = context;
        text_primary_light = mContext.getResources().getColor(R.color.text_primary_light);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, ScenicListBean item) {
        TextView spot_item_title = viewHolder.getView(R.id.spot_item_title);
        spot_item_title.setText(item.getScenicName());
        if(item.getIsOrderTicket().equals("1")){
            spot_item_title.setTextColor(Color.RED);
        }else{
            spot_item_title.setTextColor(text_primary_light);
        }

    }
}
