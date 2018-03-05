package com.msy.globalaccess.data.bean.settlement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxy on 2017/2/13.
 * class description:
 */

public class SettlementListWrapper {
    private String backNum;//退款数量
    private String totalNum;//总数量
    private String prePayNum;//预支付数量
    private String addPrePayNum;//追加预支付数量
    private List<SettlementListBean> teamAuditList;

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public List<SettlementListBean> getTeamAuditList() {
        return teamAuditList;
    }

    public void setTeamAuditList(List<SettlementListBean> teamAuditList) {
        this.teamAuditList = teamAuditList;
    }

    public void setTeamAuditList(ArrayList<SettlementListBean> teamAuditList) {
        this.teamAuditList = teamAuditList;
    }

    public String getAddPrePayNum() {
        return addPrePayNum;
    }

    public void setAddPrePayNum(String addPrePayNum) {
        this.addPrePayNum = addPrePayNum;
    }

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getPrePayNum() {
        return prePayNum;
    }

    public void setPrePayNum(String prePayNum) {
        this.prePayNum = prePayNum;
    }
}
