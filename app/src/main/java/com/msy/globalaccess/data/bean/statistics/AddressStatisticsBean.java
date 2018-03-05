package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * 旅游局团队地区bean
 * Created by chensh on 2017/5/17 0017.
 */
public class AddressStatisticsBean {


    /**
     * message : 查询成功
     * status : 0
     * data : {"customSourceCountList":[{"peopleCount":"0.01","name":"北京","teamCount":"4"},{"peopleCount":"0","name":"天津","teamCount":"0"},{"peopleCount":"0","name":"河北","teamCount":"1"},{"peopleCount":"0","name":"山西","teamCount":"1"},{"peopleCount":"0","name":"内蒙古自治区","teamCount":"0"},{"peopleCount":"0","name":"辽宁","teamCount":"0"},{"peopleCount":"0.01","name":"吉林","teamCount":"3"},{"peopleCount":"0.04","name":"黑龙江","teamCount":"1"},{"peopleCount":"0","name":"上海","teamCount":"4"},{"peopleCount":"0.03","name":"江苏","teamCount":"17"},{"peopleCount":"0.01","name":"浙江","teamCount":"2"},{"peopleCount":"0.01","name":"安徽","teamCount":"1"},{"peopleCount":"0","name":"大洋洲","teamCount":"0"},{"peopleCount":"0.22","name":"亚洲","teamCount":"19"},{"peopleCount":"0.01","name":"欧洲","teamCount":"1"},{"peopleCount":"0","name":"美洲","teamCount":"0"},{"peopleCount":"0.04","name":"其他","teamCount":"1"},{"peopleCount":"0.02","name":"非洲","teamCount":"2"},{"peopleCount":"0","name":"福建","teamCount":"0"},{"peopleCount":"0","name":"江西","teamCount":"1"},{"peopleCount":"0","name":"山东","teamCount":"0"},{"peopleCount":"0","name":"河南","teamCount":"0"},{"peopleCount":"0.04","name":"湖北","teamCount":"10"},{"peopleCount":"1.36","name":"湖南","teamCount":"426"},{"peopleCount":"0","name":"广东","teamCount":"3"},{"peopleCount":"0","name":"广西壮族自治区","teamCount":"0"},{"peopleCount":"0","name":"海南","teamCount":"0"},{"peopleCount":"0","name":"重庆","teamCount":"0"},{"peopleCount":"0","name":"四川","teamCount":"1"},{"peopleCount":"0","name":"贵州","teamCount":"2"},{"peopleCount":"0","name":"云南","teamCount":"0"},{"peopleCount":"0","name":"西藏自治区","teamCount":"0"},{"peopleCount":"0","name":"陕西","teamCount":"0"},{"peopleCount":"0","name":"甘肃","teamCount":"0"},{"peopleCount":"0","name":"青海","teamCount":"0"},{"peopleCount":"0","name":"宁夏回族自治区","teamCount":"0"},{"peopleCount":"0","name":"新疆维吾尔自治区","teamCount":"0"},{"peopleCount":"0","name":"台湾","teamCount":"0"},{"peopleCount":"0","name":"香港特别行政区","teamCount":"0"},{"peopleCount":"0","name":"澳门特别行政区","teamCount":"0"}],"peopleCount":"1821","teamCount":"500"}
     */
    /**
     * customSourceCountList : [{"peopleCount":"0.01","name":"北京","teamCount":"4"},{"peopleCount":"0","name":"天津","teamCount":"0"},{"peopleCount":"0","name":"河北","teamCount":"1"},{"peopleCount":"0","name":"山西","teamCount":"1"},{"peopleCount":"0","name":"内蒙古自治区","teamCount":"0"},{"peopleCount":"0","name":"辽宁","teamCount":"0"},{"peopleCount":"0.01","name":"吉林","teamCount":"3"},{"peopleCount":"0.04","name":"黑龙江","teamCount":"1"},{"peopleCount":"0","name":"上海","teamCount":"4"},{"peopleCount":"0.03","name":"江苏","teamCount":"17"},{"peopleCount":"0.01","name":"浙江","teamCount":"2"},{"peopleCount":"0.01","name":"安徽","teamCount":"1"},{"peopleCount":"0","name":"大洋洲","teamCount":"0"},{"peopleCount":"0.22","name":"亚洲","teamCount":"19"},{"peopleCount":"0.01","name":"欧洲","teamCount":"1"},{"peopleCount":"0","name":"美洲","teamCount":"0"},{"peopleCount":"0.04","name":"其他","teamCount":"1"},{"peopleCount":"0.02","name":"非洲","teamCount":"2"},{"peopleCount":"0","name":"福建","teamCount":"0"},{"peopleCount":"0","name":"江西","teamCount":"1"},{"peopleCount":"0","name":"山东","teamCount":"0"},{"peopleCount":"0","name":"河南","teamCount":"0"},{"peopleCount":"0.04","name":"湖北","teamCount":"10"},{"peopleCount":"1.36","name":"湖南","teamCount":"426"},{"peopleCount":"0","name":"广东","teamCount":"3"},{"peopleCount":"0","name":"广西壮族自治区","teamCount":"0"},{"peopleCount":"0","name":"海南","teamCount":"0"},{"peopleCount":"0","name":"重庆","teamCount":"0"},{"peopleCount":"0","name":"四川","teamCount":"1"},{"peopleCount":"0","name":"贵州","teamCount":"2"},{"peopleCount":"0","name":"云南","teamCount":"0"},{"peopleCount":"0","name":"西藏自治区","teamCount":"0"},{"peopleCount":"0","name":"陕西","teamCount":"0"},{"peopleCount":"0","name":"甘肃","teamCount":"0"},{"peopleCount":"0","name":"青海","teamCount":"0"},{"peopleCount":"0","name":"宁夏回族自治区","teamCount":"0"},{"peopleCount":"0","name":"新疆维吾尔自治区","teamCount":"0"},{"peopleCount":"0","name":"台湾","teamCount":"0"},{"peopleCount":"0","name":"香港特别行政区","teamCount":"0"},{"peopleCount":"0","name":"澳门特别行政区","teamCount":"0"}]
     * peopleCount : 1821
     * teamCount : 500
     */

    private String peopleCount;//总人数
    private String teamCount;//总团数
    private List<CustomSourceCountListBean> customSourceCountList;
    private StatisticsBean statisticsBean;

    public StatisticsBean getStatisticsBean() {
        return statisticsBean;
    }

    public void setStatisticsBean(StatisticsBean statisticsBean) {
        this.statisticsBean = statisticsBean;
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

    public List<CustomSourceCountListBean> getCustomSourceCountList() {
        return customSourceCountList;
    }

    public void setCustomSourceCountList(List<CustomSourceCountListBean> customSourceCountList) {
        this.customSourceCountList = customSourceCountList;
    }

    public static class CustomSourceCountListBean {
        /**
         * peopleCount : 0.01
         * name : 北京
         * teamCount : 4
         */
        private String peopleCount;//总人数（单位：千）
        private String name;//地区名称
        private String teamCount;//团队数

        public String getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(String peopleCount) {
            this.peopleCount = peopleCount;
        }

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
    }

    public class StatisticsBean{

        private ColumnChartData travelTeamCount;//旅行社接待团队统计
        private ColumnChartData travelTeamPeopleCount;//旅行社接待团队人数

        public ColumnChartData getTravelTeamCount() {
            return travelTeamCount;
        }

        public void setTravelTeamCount(ColumnChartData travelTeamCount) {
            this.travelTeamCount = travelTeamCount;
        }

        public ColumnChartData getTravelTeamPeopleCount() {
            return travelTeamPeopleCount;
        }

        public void setTravelTeamPeopleCount(ColumnChartData travelTeamPeopleCount) {
            this.travelTeamPeopleCount = travelTeamPeopleCount;
        }
    }
}
