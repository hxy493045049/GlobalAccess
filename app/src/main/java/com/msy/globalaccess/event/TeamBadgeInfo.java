package com.msy.globalaccess.event;

/**
 * Created by hxy on 2017/2/22 0022.
 * <p>
 * description : 接受团队列表的小圆点信息
 */

public class TeamBadgeInfo {

    private String totalNum;
    private String changeNum;
    private String groupNum;
    private String cancelNum;

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getCancelNum() {

        return cancelNum;
    }

    public void setCancelNum(String cancelNum) {
        this.cancelNum = cancelNum;
    }
}
