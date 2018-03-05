package com.msy.globalaccess.data.bean.navigation;

import java.util.ArrayList;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;

/**
 * Created by pepys on 2017/5/22.
 * description:首页数据
 */
public class NavigationDataBean {

    /**
     * 图片连接地址（http://xxxx.xx.xx/xxx/xxx），图片名称：1.jpg，2.jpg,3.jpg,4.jpg
     */
    private String picUrl;
    /**
     * 图片张数（注：从1开始）
     */
    private int picNum;
    /**
     * 旅行社统计
     */
    private ArrayList<SubBean> travelAgentTeamCountList;
    /**
     * 导游接团统计
     */
    private ArrayList<SubBean> guideTeamCountList;
    /**
     * 统计消息
     */
    private StatisticsDataBean statisticsDataBean;


    public StatisticsDataBean getStatisticsDataBean() {
        return statisticsDataBean;
    }

    public void setStatisticsDataBean(StatisticsDataBean statisticsDataBean) {
        this.statisticsDataBean = statisticsDataBean;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public ArrayList<SubBean> getTravelAgentTeamCountList() {
        return travelAgentTeamCountList;
    }

    public void setTravelAgentTeamCountList(ArrayList<SubBean> travelAgentTeamCountList) {
        this.travelAgentTeamCountList = travelAgentTeamCountList;
    }

    public ArrayList<SubBean> getGuideTeamCountList() {
        return guideTeamCountList;
    }

    public void setGuideTeamCountList(ArrayList<SubBean> guideTeamCountList) {
        this.guideTeamCountList = guideTeamCountList;
    }

    public class SubBean{
        /**
         * 描述  如：旅行社名称  导游名称
         */
        private String name;
        /**
         * 人数
         */
        private int countNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCountNum() {
            return countNum;
        }

        public void setCountNum(int countNum) {
            this.countNum = countNum;
        }
    }


    public class StatisticsDataBean {
        /**
         * 饼图内容
         */
        private PieChartData pieChartData;
        /**
         * 柱形图数据
         */
        private ColumnChartData columnChartData;

        public PieChartData getPieChartData() {
            return pieChartData;
        }

        public void setPieChartData(PieChartData pieChartData) {
            this.pieChartData = pieChartData;
        }

        public ColumnChartData getColumnChartData() {
            return columnChartData;
        }

        public void setColumnChartData(ColumnChartData columnChartData) {
            this.columnChartData = columnChartData;
        }
    }
}
