package com.msy.globalaccess.data.bean.team;

import java.util.List;

/**
 * 游客实体bean
 * Created by chensh on 2017/2/16 0016.
 */

public class TeamMemberWrapper {
    private String totalNum;
    private List<TeamMemberBean> memberList;

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public List<TeamMemberBean> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<TeamMemberBean> memberList) {
        this.memberList = memberList;
    }
}
