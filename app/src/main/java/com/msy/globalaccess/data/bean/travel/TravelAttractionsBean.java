package com.msy.globalaccess.data.bean.travel;

import com.msy.globalaccess.data.bean.scenic.TripScenicBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by WuDebin on 2017/3/21.
 */

public class TravelAttractionsBean implements Serializable{

    private String tripDate;

    private String placeInfo;

    private String teamTripId;

    private int days;//用来确定是第几天

    private ArrayList<TripScenicBean> tripSceniclist;

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getTeamTripId() {
        return teamTripId;
    }

    public void setTeamTripId(String teamTripId) {
        this.teamTripId = teamTripId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public ArrayList<TripScenicBean> getTripSceniclist() {
        return tripSceniclist;
    }

    public void setTripSceniclist(ArrayList<TripScenicBean> tripSceniclist) {
        this.tripSceniclist = tripSceniclist;
    }
}
