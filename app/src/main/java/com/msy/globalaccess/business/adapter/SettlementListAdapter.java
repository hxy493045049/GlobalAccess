package com.msy.globalaccess.business.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.event.SettlementSuccessEvent;

import java.util.List;

import static com.msy.globalaccess.business.travelAgency.settlement.ui.SettlementFragment.SETTLEMENT_DATA_TYPE_ALL;


/**
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description : 结算列表
 */
public class SettlementListAdapter extends BaseQuickAdapter<SettlementListBean, BaseViewHolder> {
    private int green, blue, orange, refund;

    public SettlementListAdapter(List<SettlementListBean> data, int resId) {
        super(resId, data);
        green = App.getResource().getColor(R.color.green);
        blue = App.getResource().getColor(R.color.blue);
        orange = App.getResource().getColor(R.color.orange);
        refund = App.getResource().getColor(R.color.red_light);
    }

    public void updateData(String type, SettlementSuccessEvent settlementSuccessEvent) {
        if (TextUtils.isEmpty(settlementSuccessEvent.getTeamAuditId())) {
            return;
        }

        List<SettlementListBean> data = getData();
        for (int i = 0; i < data.size(); i++) {
            SettlementListBean bean = data.get(i);
            if (bean.getTeamAuditId().equals(settlementSuccessEvent.getTeamAuditId())) {
                if (type.equals(SETTLEMENT_DATA_TYPE_ALL)) {
                    bean.setAuditStatus(settlementSuccessEvent.getAuditStatus());
                    notifyItemChanged(i + getHeaderLayoutCount());
                } else {
                    data.remove(bean);
                    notifyItemRemoved(i + getHeaderLayoutCount());
                }
                break;
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, SettlementListBean item) {
        helper.setText(R.id.tvTeamNumber, item.getTeamCode());//团队编号
        helper.setText(R.id.tvPayNumber, item.getPayCode());//订单编号
        helper.setText(R.id.tvSettlementMoney, String.format(mContext.getResources().getString(R.string
                        .holder_money_double),
                Double.parseDouble(item.getAuditMoney()) / 100.0));//总预付款

        TextView tvOrderType = helper.getView(R.id.tvOrderType);
        TextView tvOrderStatus = helper.getView(R.id.tvOrderStatus);

        if (item.getAuditStatus().equals("0")) {// 待结算
            tvOrderStatus.setText(App.getResourceString(R.string.order_status_waitting));

        } else if (item.getAuditStatus().equals("1")) {// 结算中
            tvOrderStatus.setText(App.getResourceString(R.string.order_status_calculating));

        } else if (item.getAuditStatus().equals("2")) {// 结算成功
            tvOrderStatus.setText(App.getResourceString(R.string.order_status_success));

        } else if (item.getAuditStatus().equals("3")) {// 结算失败
            tvOrderStatus.setText(App.getResourceString(R.string.order_status_failed));

        }
        //团队状态
        if (item.getAuditType().equals("0")) {//预支付
            tvOrderType.setText(App.getResourceString(R.string.order_type_prepay));
            tvOrderType.setTextColor(blue);
            tvOrderStatus.setTextColor(blue);

        } else if (item.getAuditType().equals("1")) {//追加预支付
            tvOrderType.setText(App.getResourceString(R.string.order_type_append));
            tvOrderType.setTextColor(green);
            tvOrderStatus.setTextColor(green);

        } else if (item.getAuditType().equals("2")) {//支付
            tvOrderType.setText(App.getResourceString(R.string.order_type_pay));
            tvOrderType.setTextColor(orange);
            tvOrderStatus.setTextColor(orange);

        } else if (item.getAuditType().equals("3")) {//退款
            tvOrderType.setText(App.getResourceString(R.string.order_type_refund));
            tvOrderType.setTextColor(refund);
            tvOrderStatus.setTextColor(refund);
        }
    }
}
