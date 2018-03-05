package com.msy.globalaccess.business.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamDetailTimeLineBean;

import java.util.ArrayList;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * Created by pepys on 2017/2/8.
 * description:团队详情-行程信息
 */
public class TeamDetailLineAdapter extends BaseQuickAdapter<TeamDetailTimeLineBean,BaseViewHolder> {
    Context mContext;
    public TeamDetailLineAdapter(Context context, int layoutResId, ArrayList<TeamDetailTimeLineBean> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, TeamDetailTimeLineBean item) {
        switch (item.getItemType()){
            case 0:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_day1)
                            .setVisible(R.id.team_detail_timeline_content,false);
                TextView line_up = viewHolder.getView(R.id.line_up);
                line_up.setVisibility(View.INVISIBLE);
                line_up.getLayoutParams().height=DisplayUtil.dip2px(18);
                TextView tv_title = viewHolder.getView(R.id.team_detail_timeline_title);
                TextPaint textPaint = tv_title.getPaint();
                textPaint.setFakeBoldText(true);

                int position = viewHolder.getAdapterPosition();
                if(position > 0){
                    ArrayList<TeamDetailTimeLineBean> data = (ArrayList<TeamDetailTimeLineBean>) getData();
                    TeamDetailTimeLineBean timeLine = data.get(position-7);
                    if(timeLine.getTitle().equals(item.getTitle())){
                        item.setDataHead(timeLine.getDataHead());
                    }else{
                        item.setDataHead((timeLine.getDataHead()+1));
                    }
                }else{
                    item.setDataHead((position+1));
                }

                tv_title.setText("第"+item.getDataHead()+"天 "+item.getTitle());
                tv_title.setTextColor(Color.BLACK);
                tv_title.setTextSize(13);
                break;
            case 1:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_xingcheng)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setVisible(R.id.team_detail_timeline_content,false);
                break;
            case 2:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_didian)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setVisible(R.id.team_detail_timeline_content,false);
                TextView line_up1 = viewHolder.getView(R.id.line_up);
                line_up1.getLayoutParams().height=DisplayUtil.dip2px(20);
                break;
            case 3:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_canyin)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setText(R.id.team_detail_timeline_content,item.getContent());
                break;
            case 4:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_zhusu)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setText(R.id.team_detail_timeline_content,item.getContent());
                break;
            case 5:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_yanchu)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setText(R.id.team_detail_timeline_content,item.getContent());
                break;
            case 6:
                viewHolder.setImageResource(R.id.team_detail_timeline_icon,R.mipmap.icon_tdxq_gouwu)
                        .setText(R.id.team_detail_timeline_title,item.getTitle())
                        .setText(R.id.team_detail_timeline_content,item.getContent());
                break;
        }
    }
}
