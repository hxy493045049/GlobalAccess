package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by shawn on 2017/7/18 0018.
 * <p>
 * description : 游客年龄构成数据模型
 */

public class GuestAgeStatisticsBeanWrapper {
    private List<GuestAgeStatisticsBean> customAgeCountList;

    public List<GuestAgeStatisticsBean> getCustomAgeCountList() {
        return customAgeCountList;
    }

    public void setCustomAgeCountList(List<GuestAgeStatisticsBean> customAgeCountList) {
        this.customAgeCountList = customAgeCountList;
    }

    public static class GuestAgeStatisticsBean {
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

    public static class ResultBean {
        private List<GuestAgeStatisticsBean> origin;
        private ColumnChartData columnChartData;

        public ColumnChartData getColumnChartData() {
            return columnChartData;
        }

        public void setColumnChartData(ColumnChartData columnChartData) {
            this.columnChartData = columnChartData;
        }

        public List<GuestAgeStatisticsBean> getOrigin() {
            return origin;
        }

        public void setOrigin(List<GuestAgeStatisticsBean> origin) {
            this.origin = origin;
        }
    }
}
