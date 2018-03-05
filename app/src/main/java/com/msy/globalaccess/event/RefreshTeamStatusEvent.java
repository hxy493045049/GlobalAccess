package com.msy.globalaccess.event;

/**
 * Created by pepys on 2017/2/21.
 * description:团队详情操作成功处理列表数据
 */
public class RefreshTeamStatusEvent {
    /**
     * 操作成功的团队详情ID
     */
    private String teamID;
    /**
     * 修改成为某种状态//0:编辑中;1:已提交;2:已提交（通过审核）;3:已提交（审核失败）;4:生效;5:变更;
     * 6:变更（通过审核）;7:变更（审核失败）;8:作废;9:作废（通过审核）;10:作废（审核失败）
     */
    private String status;
    /**
     * 是否通过请求   //处理结果:0:待确认;1:已通过;2:未通过;
     */
    private int operStatus;
    /**
     * //处理类型:0:出团申请;1:作废申请;2:变更申请;
     */
    private String operType;

    public RefreshTeamStatusEvent(String teamID, String status, int operStatus,String operType) {
        this.teamID = teamID;
        this.status = status;
        this.operStatus = operStatus;
        this.operType = operType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public int getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(int operStatus) {
        this.operStatus = operStatus;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }
}
