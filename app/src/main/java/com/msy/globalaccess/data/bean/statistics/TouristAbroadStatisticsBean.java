package com.msy.globalaccess.data.bean.statistics;


import java.util.List;

/**
 * Created by pepys on 2017/7/17
 * description: 境内外游客对比
 *
 */
public class TouristAbroadStatisticsBean {
    /**
     * name : 境内游客
     * customFromCountBillList : [{"date":"11月","countNum":"0"},{"date":"12月","countNum":"929"},{"date":"1月","countNum":"52814"},{"date":"2月","countNum":"117508"},{"date":"3月","countNum":"237732"},{"date":"4月","countNum":"447246"}]
     */

    private String name;
    private List<TendencyBean> customFromCountBillList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TendencyBean> getCustomFromCountBillList() {
        return customFromCountBillList;
    }

    public void setCustomFromCountBillList(List<TendencyBean> customFromCountBillList) {
        this.customFromCountBillList = customFromCountBillList;
    }

}
