package com.msy.globalaccess.data.bean.statistics;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * 旅游局团队认证实体类
 * Created by chensh on 2017/5/17 0017.
 */

public class TravelTeamStatisticsBean {
    private String teamCount;//总团数
    private String checkPeopleCount;//认证总人数
    private ArrayList<TeamCheckCount> teamCheckCountList;//认证团队统计


    private ColumnChartData teamColumnChartData; //认证团队统计数据
    private ColumnChartData peopleColumnChartData;//认证总人数统计数据

    public ColumnChartData getPeopleColumnChartData() {
        return peopleColumnChartData;
    }

    public void setPeopleColumnChartData(ColumnChartData peopleColumnChartData) {
        this.peopleColumnChartData = peopleColumnChartData;
    }

    public ColumnChartData getTeamColumnChartData() {
        return teamColumnChartData;
    }

    public void setTeamColumnChartData(ColumnChartData teamColumnChartData) {
        this.teamColumnChartData = teamColumnChartData;
    }

    public String getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(String teamCount) {
        this.teamCount = teamCount;
    }

    public String getCheckPeopleCount() {
        return checkPeopleCount;
    }

    public void setCheckPeopleCount(String checkPeopleCount) {
        this.checkPeopleCount = checkPeopleCount;
    }

    public ArrayList<TeamCheckCount> getTeamCheckCountList() {
        return teamCheckCountList;
    }

    public void setTeamCheckCountList(ArrayList<TeamCheckCount> teamCheckCountList) {
        this.teamCheckCountList = teamCheckCountList;
    }

    public class TeamCheckCount {
        private String name;//旅行社/景区名称
        private String teamCount;//团队数
        private String peopleCount;//总人数（单位：千）

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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


    }

}
