package com.msy.globalaccess.event;

/**
 * Created by pepys on 2017/2/23.
 * description:刷新团队小圆点
 */
public class RefreshTeamBadgeEvent {

    private String type;
    /**
     * //处理类型:0:出团申请;1:作废申请;2:变更申请;
     */
    private String operType;

    /**
     * 数量加减  0：减   1：加
     */
    private int change;

    public RefreshTeamBadgeEvent(String type,String operType,int change) {
        this.type = type;
        this.operType = operType;
        this.change = change;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
