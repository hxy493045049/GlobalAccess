package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 团队详情导游信息
 * Created by chensh on 2017/2/10 0010.
 */

public class TeamCiceronInfoBean implements Parcelable {

    /**
     * travelAgentName : 张家界国际旅行社
     * phoneNum : 13456788956
     * guideCode : D-1234-123456
     * name : 王意远
     * appointDate : 2017-02-09 06:00-2017-02-10 18:00
     */

    private String travelAgentName;
    private String phoneNum;
    private String guideCode;
    private String name;
    private String appointDate;
    private String picUrl;

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGuideCode() {
        return guideCode;
    }

    public void setGuideCode(String guideCode) {
        this.guideCode = guideCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.travelAgentName);
        dest.writeString(this.phoneNum);
        dest.writeString(this.guideCode);
        dest.writeString(this.name);
        dest.writeString(this.appointDate);
        dest.writeString(this.picUrl);
    }

    public TeamCiceronInfoBean() {
    }

    protected TeamCiceronInfoBean(Parcel in) {
        this.travelAgentName = in.readString();
        this.phoneNum = in.readString();
        this.guideCode = in.readString();
        this.name = in.readString();
        this.appointDate = in.readString();
        this.picUrl = in.readString();
    }

    public static final Parcelable.Creator<TeamCiceronInfoBean> CREATOR = new Parcelable.Creator<TeamCiceronInfoBean>() {
        @Override
        public TeamCiceronInfoBean createFromParcel(Parcel source) {
            return new TeamCiceronInfoBean(source);
        }

        @Override
        public TeamCiceronInfoBean[] newArray(int size) {
            return new TeamCiceronInfoBean[size];
        }
    };
}
