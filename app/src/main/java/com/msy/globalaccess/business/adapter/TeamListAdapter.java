package com.msy.globalaccess.business.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description : 团队列表
 */
@SuppressWarnings("deprecation")
public class TeamListAdapter extends BaseQuickAdapter<TeamListBean, BaseViewHolder> {
    private String type;//因为需求,当列表为全部时不显示公司信息,其他状态都显示,做一个flag判断
    private int green, blue, orange, discard, red;
    private String holderMoney = App.getResourceString(R.string.holder_money_double_no_money);

    public TeamListAdapter(List<TeamListBean> data, int resId, String type) {
        super(resId, data);
        this.type = type;
        green = App.getResource().getColor(R.color.green);
        blue = App.getResource().getColor(R.color.blue);
        orange = App.getResource().getColor(R.color.orange);
        discard = App.getResource().getColor(R.color.gray_discard);
        red = App.getResource().getColor(R.color.red_dark);

    }

    public void converData(RefreshTeamStatusEvent refreshTeam) {
        String teamID = refreshTeam.getTeamID();
        String teamStatus = refreshTeam.getStatus();
        String operType = refreshTeam.getOperType();
        int operStatus = refreshTeam.getOperStatus();
        List<TeamListBean> data = getData();
        TeamListBean team;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            team = data.get(i + getHeaderLayoutCount());
            if (teamID.equals(team.getTeamId())) {
                if (TextUtils.isEmpty(type)) {
                    team.setTeamStatus(teamStatus);
                    team.setOperStauts(operStatus + "");
                    team.setOperType(operType);
                    notifyItemChanged(i);
                    return;
                } else {
                    remove(i + getHeaderLayoutCount());
                    return;
                }
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamListBean item) {
        helper.setText(R.id.tvTeamId, item.getTeamCode());//团队编号
        helper.setText(R.id.tvTotalPeople, item.getPeopleNum());//总人数
        helper.setText(R.id.tvPrepayment, String.format(holderMoney, Double.parseDouble(item.getPrePayMoney()) /
                100.0));//总预付款

        helper.setText(R.id.tvCompany, item.getTravelDepName());//公司信息
        helper.setVisible(R.id.tvCompany, !TextUtils.isEmpty(type));//是否显示公司信息

        TextView tvTeamStatus = helper.getView(R.id.tvTeamStatus);
        TextView tvOperType = helper.getView(R.id.tvOperType);
        TextView tvOperStatus = helper.getView(R.id.tvOperStatus);

        changeOperType(item, tvOperType);
        changeOperStatus(item, tvOperStatus);
        chageTextColorAndTeamStatus(item, tvOperStatus, tvOperType, tvTeamStatus);

    }

    private void changeOperStatus(TeamListBean item, TextView tvOperStatus) {
        switch (item.getOperStauts()) {//处理结果:0:待确认;1:已通过;2:未通过;
            case "0":
                tvOperStatus.setText(App.getResourceString(R.string.billing_oper_status_wait));
                break;
            case "1":
                tvOperStatus.setText(App.getResourceString(R.string.billing_oper_status_pass));
                break;
            case "2":
                tvOperStatus.setText(App.getResourceString(R.string.billing_oper_status_refuse));
                break;
        }
    }

    private void changeOperType(TeamListBean item, TextView tvOperType) {
        switch (item.getOperType()) {//处理类型:0:出团申请;1:作废申请;2:变更申请;
            case "0":
                tvOperType.setText(App.getResourceString(R.string.billing_oper_type_group));
                break;
            case "1":
                tvOperType.setText(App.getResourceString(R.string.billing_oper_type_discard));
                break;
            case "2":
                tvOperType.setText(App.getResourceString(R.string.billing_oper_type_change));
                break;
        }
    }

    private void chageTextColorAndTeamStatus(TeamListBean item, TextView tvOperStatus, TextView tvOperType, TextView
            tvTeamStatus) {
        //0:编辑中;1:已提交;2:已提交（通过审核）;3:已提交（审核失败）;4:生效;5:变更;
        // 6:变更（通过审核）;7:变更（审核失败）;8:作废;9:作废（通过审核）;10:作废（审核失败）
        int textColor = App.getResource().getColor(R.color.text_primary_dark);
        switch (item.getTeamStatus()) {
            case "0":
                textColor = orange;
                tvTeamStatus.setText("编辑中");
                break;
            case "1":
                textColor = green;
                tvTeamStatus.setText("已提交");
                break;
            case "2":
                textColor = green;
                tvTeamStatus.setText("已提交(通过审核)");
                break;
            case "3":
                textColor = green;
                tvTeamStatus.setText("已提交(审核失败)");
                break;
            case "4":
                textColor = blue;
                tvTeamStatus.setText("生效");
                break;
            case "5":
                textColor = red;
                tvTeamStatus.setText("变更");
                break;
            case "6":
                textColor = red;
                tvTeamStatus.setText("变更(通过审核)");
                break;
            case "7":
                textColor = red;
                tvTeamStatus.setText("变更(审核失败)");
                break;
            case "8":
                textColor = discard;
                tvTeamStatus.setText("作废");
                break;
            case "9":
                textColor = discard;
                tvTeamStatus.setText("作废(通过审核)");
                break;
            case "10":
                textColor = discard;
                tvTeamStatus.setText("作废(审核失败)");
                break;
            default:
                Logger.e("缺少teamstatus");
                tvTeamStatus.setText("");
        }
        tvOperType.setTextColor(textColor);
        tvOperStatus.setTextColor(textColor);
        tvTeamStatus.setTextColor(textColor);
    }
}
