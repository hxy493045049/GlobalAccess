package com.msy.globalaccess.data.bean.settlement;

/**
 * Created by hxy on 2017/2/14.
 * class description: 结算单详情的数据模型
 */
public class SettlementDetailBean {
    private String teamAuditId = "";//结算单ID
    private String payCode = "";//支付编号
    private String teamCode = "";//团队编号
    private String auditType = "";//结算单类型：0:预支付;1:追加预支付;2:支付;3:退款;
    private String auditMethod = "";//结算方式:0:实时结算;1:暂延支付;
    private String bankName = "";//结算银行
    private String outComeUnitName = "";//支出单位
    private String inComeUnitName = "";//收入单位
    private String prePayMoney = "0";//预付款（分）
    private String auditMoney = "0";//提交结算金额（分）
    private String linkUnitName = "";//相关单位
    private String payMemo = "";//支付描述
    private String creater = "";//支付创建者
    private String createTime = "";//支付创建时间（yyyy-MM-dd HH:mm:ss）
    private String auditStatus = "";//结算单状态：0:待结算;1:结算中;2:结算成功;3:结算失败;
    private String tripStartDate = "";//行程开始日期

    public String getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(String tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public String getTeamAuditId() {
        return teamAuditId;
    }

    public void setTeamAuditId(String teamAuditId) {
        this.teamAuditId = teamAuditId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditMethod() {
        return auditMethod;
    }

    public void setAuditMethod(String auditMethod) {
        this.auditMethod = auditMethod;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOutComeUnitName() {
        return outComeUnitName;
    }

    public void setOutComeUnitName(String outComeUnitName) {
        this.outComeUnitName = outComeUnitName;
    }

    public String getInComeUnitName() {
        return inComeUnitName;
    }

    public void setInComeUnitName(String inComeUnitName) {
        this.inComeUnitName = inComeUnitName;
    }

    public String getPrePayMoney() {
        return prePayMoney;
    }

    public void setPrePayMoney(String prePayMoney) {
        this.prePayMoney = prePayMoney;
    }

    public String getAuditMoney() {
        return auditMoney;
    }

    public void setAuditMoney(String auditMoney) {
        this.auditMoney = auditMoney;
    }

    public String getLinkUnitName() {
        return linkUnitName;
    }

    public void setLinkUnitName(String linkUnitName) {
        this.linkUnitName = linkUnitName;
    }

    public String getPayMemo() {
        return payMemo;
    }

    public void setPayMemo(String payMemo) {
        this.payMemo = payMemo;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
