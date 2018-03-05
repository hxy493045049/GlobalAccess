package com.msy.globalaccess.data.bean.travel;

import java.io.Serializable;
import java.util.ArrayList;

import cn.msy.zc.commonutils.StringUtils;

/**
 * Created by WuDebin on 2017/4/10.
 */

public class TravelAttractionsParentBean implements Serializable{

    private String adultsAmount;

    private String childrenAmout;

    private ArrayList<TravelAttractionsBean> teamTripList;

    public int getAdultsAmount() {
        return StringUtils.stringConvertInt(adultsAmount);
    }

    public void setAdultsAmount(String adultsAmount) {
        this.adultsAmount = adultsAmount;
    }

    public int getChildrenAmout() {
        return StringUtils.stringConvertInt(childrenAmout);
    }

    public void setChildrenAmout(String childrenAmout) {
        this.childrenAmout = childrenAmout;
    }

    public ArrayList<TravelAttractionsBean> getTeamTripList() {
        return teamTripList;
    }

    public void setTeamTripList(ArrayList<TravelAttractionsBean> teamTripList) {
        this.teamTripList = teamTripList;
    }
}
