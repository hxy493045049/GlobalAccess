package com.msy.globalaccess.event;

/**
 * Created by hxy on 2017/2/17.
 * class description: 结算成功后,从成功页面发送消息到详情页,刷新数据
 */

public class SettlementSuccessEvent {
    private String teamAuditId;

    //结算单状态：0:待结算;1:结算中;2:结算成功;3:结算失败;
    private String auditStatus = "";

    public SettlementSuccessEvent(String teamAuditId) {
        this.teamAuditId = teamAuditId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public SettlementSuccessEvent setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
        return this;
    }

    public String getTeamAuditId() {
        return teamAuditId;
    }

    public void setTeamAuditId(String teamAuditId) {
        this.teamAuditId = teamAuditId;
    }
}
