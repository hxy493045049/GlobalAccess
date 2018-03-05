package com.msy.globalaccess.data.bean.team;

import java.util.List;

/**
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description :
 */

public class TeamListWrapper {
    private String totalNum;
    private String changeNum;
    private String groupNum;
    private String cancelNum;
    private List<TeamListBean> teamList;

    public String getCancelNum() {
        return cancelNum;
    }

    public void setCancelNum(String cancelNum) {
        this.cancelNum = cancelNum;
    }

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

    public List<TeamListBean> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamListBean> teamList) {
        this.teamList = teamList;
    }
}
