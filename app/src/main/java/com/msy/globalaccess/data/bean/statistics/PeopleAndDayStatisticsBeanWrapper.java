package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description :
 */

public class PeopleAndDayStatisticsBeanWrapper {
    private List<PeopleAndDayStatisticsBean> customDaysCountList;

    public List<PeopleAndDayStatisticsBean> getCustomDaysCountList() {
        return customDaysCountList;
    }

    public void setCustomDaysCountList(List<PeopleAndDayStatisticsBean> customDaysCountList) {
        this.customDaysCountList = customDaysCountList;
    }

    public static class PeopleAndDayStatisticsBean {
        private String name;
        private String countNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountNum() {
            return countNum;
        }

        public void setCountNum(String countNum) {
            this.countNum = countNum;
        }
    }

    public static class ResultBean{

        private List<PeopleAndDayStatisticsBean> originData;
        private ColumnChartData columnChartData;

        public void setOriginData(List<PeopleAndDayStatisticsBean> originData) {
            this.originData = originData;
        }

        public List<PeopleAndDayStatisticsBean> getOriginData() {
            return originData;
        }

        public void setColumnChartData(ColumnChartData columnChartData) {
            this.columnChartData = columnChartData;
        }

        public ColumnChartData getColumnChartData() {
            return columnChartData;
        }
    }
}
