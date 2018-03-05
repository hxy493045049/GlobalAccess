package com.msy.globalaccess.data.bean;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 旅行部门
 * Created by chensh on 2017/2/14 0014.
 */

public class TravelDepBean {
    @Inject
    public TravelDepBean() {

    }
    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":1115,"travelDepList":[{"travelDepId":"151","travelDepName":" 门市 营业部"},{"travelDepId":"153","travelDepName":"系统集成部"},{"travelDepId":"156","travelDepName":"综合办事处"},{"travelDepId":"159","travelDepName":"监管部"},{"travelDepId":"160","travelDepName":"财务部"},{"travelDepId":"161","travelDepName":"系统部"},{"travelDepId":"162","travelDepName":"总裁部"},{"travelDepId":"163","travelDepName":"综合办"},{"travelDepId":"164","travelDepName":"企划部"},{"travelDepId":"165","travelDepName":"营销部"},{"travelDepId":"202","travelDepName":"测试1"},{"travelDepId":"203","travelDepName":"王惠专属部门"},{"travelDepId":"208","travelDepName":"门市二部"},{"travelDepId":"270","travelDepName":"余额测试（cai）"},{"travelDepId":"271","travelDepName":"学府路店"},{"travelDepId":"272","travelDepName":"中泽路店"},{"travelDepId":"350","travelDepName":"系统集成部1"},{"travelDepId":"351","travelDepName":"系统集成部2"},{"travelDepId":"352","travelDepName":"系统集成部3"},{"travelDepId":"353","travelDepName":"系统集成部4"}]}
     */

    /**
     * totalNum : 1115
     * travelDepList : [{"travelDepId":"151","travelDepName":" 门市 营业部"},{"travelDepId":"153","travelDepName":"系统集成部"},{"travelDepId":"156","travelDepName":"综合办事处"},{"travelDepId":"159","travelDepName":"监管部"},{"travelDepId":"160","travelDepName":"财务部"},{"travelDepId":"161","travelDepName":"系统部"},{"travelDepId":"162","travelDepName":"总裁部"},{"travelDepId":"163","travelDepName":"综合办"},{"travelDepId":"164","travelDepName":"企划部"},{"travelDepId":"165","travelDepName":"营销部"},{"travelDepId":"202","travelDepName":"测试1"},{"travelDepId":"203","travelDepName":"王惠专属部门"},{"travelDepId":"208","travelDepName":"门市二部"},{"travelDepId":"270","travelDepName":"余额测试（cai）"},{"travelDepId":"271","travelDepName":"学府路店"},{"travelDepId":"272","travelDepName":"中泽路店"},{"travelDepId":"350","travelDepName":"系统集成部1"},{"travelDepId":"351","travelDepName":"系统集成部2"},{"travelDepId":"352","travelDepName":"系统集成部3"},{"travelDepId":"353","travelDepName":"系统集成部4"}]
     */

    private int totalNum;
    private ArrayList<TravelDepListBean> travelDepList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<TravelDepListBean> getTravelDepList() {
        return travelDepList;
    }

    public void setTravelDepList(ArrayList<TravelDepListBean> travelDepList) {
        this.travelDepList = travelDepList;
    }

    public static class TravelDepListBean {
        /**
         * travelDepId : 151
         * travelDepName :  门市 营业部
         */

        private String travelDepId;
        private String travelDepName;

        public String getTravelDepId() {
            return travelDepId;
        }

        public void setTravelDepId(String travelDepId) {
            this.travelDepId = travelDepId;
        }

        public String getTravelDepName() {
            return travelDepName;
        }

        public void setTravelDepName(String travelDepName) {
            this.travelDepName = travelDepName;
        }
    }
}
