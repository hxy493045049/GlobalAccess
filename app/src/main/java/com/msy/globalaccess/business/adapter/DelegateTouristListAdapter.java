package com.msy.globalaccess.business.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.event.RefreshDelegateListEvent;

import java.util.ArrayList;
import java.util.List;

import static com.msy.globalaccess.business.travelAgency.delegate.ui.TouristDelegateFragment.TEAM_DATA_TYPE_ALL;


/**
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description : 审批导游列表
 */
public class DelegateTouristListAdapter extends BaseQuickAdapter<TouristDelegateBean, BaseViewHolder> {

    private int green, blue, refund;

    public DelegateTouristListAdapter(ArrayList<TouristDelegateBean> data, int resId) {
        super(resId, data);
        green = App.getResource().getColor(R.color.green);
        blue = App.getResource().getColor(R.color.blue);
        refund = App.getResource().getColor(R.color.red_light);
    }

    public void updateData(String searchType,RefreshDelegateListEvent refreshdelegate) {
        String teamGuideID = refreshdelegate.getTeamGuideId();
        String operStatus = refreshdelegate.getOperStatus();
        List<TouristDelegateBean> data = getData();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if(teamGuideID.equals(data.get(i).getTeamGuideId())){
                if(searchType.equals(TEAM_DATA_TYPE_ALL)){
                    data.get(i).setOperStatus(operStatus);
                    notifyItemChanged(i);
                }else{
                    remove(i+ getHeaderLayoutCount());
                }
                break;
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, TouristDelegateBean item) {

        helper.setText(R.id.tv_delegate_list_team_code, item.getTeamCode());//团队编号
        helper.setText(R.id.tv_delegate_list_guide, item.getTeamGuideName());//订单编号
        helper.setText(R.id.tv_delegate_create_time, item.getCreateTime());//总预付款

        TextView tvOrderStatus = helper.getView(R.id.tv_delegate_orderstatus);
        TextView tvOrderType = helper.getView(R.id.tv_delegate_list_ordertype);

        if (item.getOperStatus().equals("0")) {// 处理中
            tvOrderStatus.setText(App.getResourceString(R.string.delegate_state_processing));
            tvOrderType.setTextColor(blue);
            tvOrderStatus.setTextColor(blue);
        } else if (item.getOperStatus().equals("1")) {// 未通过
            tvOrderStatus.setText(App.getResourceString(R.string.delegate_state_unprocess));
            tvOrderType.setTextColor(refund);
            tvOrderStatus.setTextColor(refund);
        } else if (item.getOperStatus().equals("2")) {// 已通过
            tvOrderStatus.setText(App.getResourceString(R.string.delegate_state_processed));
            tvOrderType.setTextColor(green);
            tvOrderStatus.setTextColor(green);
        }

        //处理类型
        if (item.getOpType().equals("0")) {//申请委派
            tvOrderType.setText(App.getResourceString(R.string.delegate_state_apply_process));
        } else if (item.getOpType().equals("1")) {//取消委派
            tvOrderType.setText(App.getResourceString(R.string.delegate_state_cancle_process));
        }
    }
}
