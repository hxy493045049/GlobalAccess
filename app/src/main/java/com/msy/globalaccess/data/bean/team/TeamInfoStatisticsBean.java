package com.msy.globalaccess.data.bean.team;

import com.msy.globalaccess.data.bean.travel.TravelAgentStatisticsBean;

import java.util.List;

/**
 * Created by WuDebin on 2017/5/17.
 */

public class TeamInfoStatisticsBean {

    private String teamCount;//总团数
    private String peopleCount;//总人数
    private String adultCount;//成人总人数
    private String childCount;//儿童总人数

    private List<TravelAgentStatisticsBean> travelAgentCountList;//旅行社接待团队统计

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(String adultCount) {
        this.adultCount = adultCount;
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public List<TravelAgentStatisticsBean> getTravelAgentStatisticsBeen() {
        return travelAgentCountList;
    }

    public void setTravelAgentStatisticsBeen(List<TravelAgentStatisticsBean> travelAgentCountList) {
        this.travelAgentCountList = travelAgentCountList;
    }
}
