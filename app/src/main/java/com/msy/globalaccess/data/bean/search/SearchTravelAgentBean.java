package com.msy.globalaccess.data.bean.search;

import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;

import java.util.ArrayList;

/**
 * Created by chensh on 2017/2/15 0015.
 */

public class SearchTravelAgentBean {


    /**
     * status : 0
     * message : 查询成功
     * data : {"totalNum":"100","travelAgentList":[{"travelAgentId":"\u201d001\u201d","travelAgentName":"中国旅行社"},{"travelAgentId":"\u201d002\u201d","travelAgentName":"康辉旅行社"}]}
     */
    /**
     * totalNum : 100
     * travelAgentList : [{"travelAgentId":"\u201d001\u201d","travelAgentName":"中国旅行社"},{"travelAgentId":"\u201d002\u201d","travelAgentName":"康辉旅行社"}]
     */

    private String totalNum;
    private ArrayList<TravelAgentListBean> travelAgentList;

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<TravelAgentListBean> getTravelAgentList() {
        return travelAgentList;
    }

    public void setTravelAgentList(ArrayList<TravelAgentListBean> travelAgentList) {
        this.travelAgentList = travelAgentList;
    }
}
