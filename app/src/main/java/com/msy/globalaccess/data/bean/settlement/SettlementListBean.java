package com.msy.globalaccess.data.bean.settlement;

/**
 * 结算列表的数据模型
 * Created by chensh on 2017/1/23 0023.
 */

public class SettlementListBean {
    //结算单id
    private String teamAuditId = "";
    //支付编号
    private String payCode = "";
    //团队编号
    private String teamCode = "";
    //结算金额（单位：分）
    private String auditMoney = "0";
    //结算单类型：0:预支付;1:追加预支付;2:支付;3:退款;
    private String auditType = "";
    //结算单状态：0:待结算;1:结算中;2:结算成功;3:结算失败;
    private String auditStatus = "";

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(String auditMoney) {
        this.auditMoney = auditMoney;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getTeamAuditId() {
        return teamAuditId;
    }

    public void setTeamAuditId(String teamAuditId) {
        this.teamAuditId = teamAuditId;
    }
}
