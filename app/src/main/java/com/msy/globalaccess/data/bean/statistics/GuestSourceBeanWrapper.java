package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;

/**
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description : 客源地
 */

public class GuestSourceBeanWrapper {
    private List<GuestSourceBean> customSourceCountList;

    public List<GuestSourceBean> getCustomSourceCountList() {
        return customSourceCountList;
    }

    public void setCustomSourceCountList(List<GuestSourceBean> customSourceCountList) {
        this.customSourceCountList = customSourceCountList;
    }

    public static class GuestSourceBean {
        /**
         * name : 湖南
         * countNum : 548
         */

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
        private List<GuestSourceBeanWrapper.GuestSourceBean> origin;
        private PieChartData pieChartData;

        public List<GuestSourceBeanWrapper.GuestSourceBean> getOrigin() {
            return origin;
        }

        public void setOrigin(List<GuestSourceBeanWrapper.GuestSourceBean> origin) {
            this.origin = origin;
        }

        public PieChartData getPieChartData() {
            return pieChartData;
        }

        public void setPieChartData(PieChartData pieChartData) {
            this.pieChartData = pieChartData;
        }
    }
}
