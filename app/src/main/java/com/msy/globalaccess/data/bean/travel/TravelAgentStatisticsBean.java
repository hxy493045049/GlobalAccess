package com.msy.globalaccess.data.bean.travel;

/**
 * Created by WuDebin on 2017/5/17.
 */

public class TravelAgentStatisticsBean {

    private String name;//旅行社名称
    private String teamCount;//团队数
    private String peopleCount;//总人数（单位：千）

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }
}
