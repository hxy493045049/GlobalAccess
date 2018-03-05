package com.msy.globalaccess.event;

/**
 * Created by pepys on 2017/2/21.
 * description:团队详情审批导游委派处理
 */
public class RefreshDelegateListEvent {
    /**
     * 委派编号ID
     */
    private String teamGuideId;
    /**
     * 处理之后的状态 0:处理中;1:未通过;2:已通过;
     */
    private String operStatus;

    public RefreshDelegateListEvent(String teamGuideId, String operStatus) {
        this.teamGuideId = teamGuideId;
        this.operStatus = operStatus;
    }

    public String getTeamGuideId() {
        return teamGuideId;
    }

    public void setTeamGuideId(String teamGuideId) {
        this.teamGuideId = teamGuideId;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }
}
