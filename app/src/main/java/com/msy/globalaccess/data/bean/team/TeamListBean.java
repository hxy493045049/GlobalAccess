package com.msy.globalaccess.data.bean.team;

/**
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description : 团队管理列表的数据模型
 */

public class TeamListBean {
    private String teamId = "";//团队id
    private String teamCode = "";//团队编号
    private String travelDepName = "";//部门名称
    private String prePayMoney = "0";//总预付款
    private String peopleNum = "";//总人数
    private String accountStatus = "";//结算状态
    private String teamStatus = "";//团队状态
    private String operType = "";//处理类型:0:出团申请;1:作废申请;2:变更申请;
    private String operStauts = "";//处理结果:0:待确认;1:已通过;2:未通过;

    private String travelAgentName;

    public String getOperStauts() {
        return operStauts;
    }

    public void setOperStauts(String operStauts) {
        this.operStauts = operStauts;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getTravelDepName() {
        return travelDepName;
    }

    public void setTravelDepName(String travelDepName) {
        this.travelDepName = travelDepName;
    }


    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPrePayMoney() {
        return prePayMoney;
    }

    public void setPrePayMoney(String prePayMoney) {
        this.prePayMoney = prePayMoney;
    }

}
