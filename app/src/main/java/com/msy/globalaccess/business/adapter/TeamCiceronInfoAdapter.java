package com.msy.globalaccess.business.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.team.TeamCiceronInfoBean;

import java.util.List;

import cn.msy.zc.commonutils.glideconfig.GlideCircleTransform;

/**
 * 导游信息Adapter
 * Created by chensh on 2017/2/10 0010.
 */

public class TeamCiceronInfoAdapter extends BaseQuickAdapter<TeamCiceronInfoBean, BaseViewHolder> {

    private Context mContext;

    public TeamCiceronInfoAdapter(int layoutResId, List data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamCiceronInfoBean item) {
        helper.setText(R.id.team_detail_ciceron_guideCode,item.getGuideCode())
                .setText(R.id.team_detail_ciceron_name,item.getName())
                .setText(R.id.team_detail_ciceron_phoneNum,item.getPhoneNum())
                .setText(R.id.team_detail_ciceron_appointDate,item.getAppointDate())
                .addOnClickListener(R.id.team_detail_ciceron_head)
                .setText(R.id.team_detail_ciceron_travelAgentName,item.getTravelAgentName());
        ImageView team_detail_ciceron_head = helper.getView(R.id.team_detail_ciceron_head);
        Glide.with(mContext)
                .load(item.getPicUrl())
                .transform(new GlideCircleTransform(mContext))
                .error(R.mipmap.icon_daoyou_default)
                .into(team_detail_ciceron_head);
//        GlideHelper.urlToImageView(mContext,team_detail_ciceron_head,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494411262798&di=65e486c3c7470f40d9c89de090819095&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201605%2F19%2F20160519224441_VfMWR.thumb.700_0.jpeg",GlideHelper.Glide_Imgae_Circle);
    }
}
