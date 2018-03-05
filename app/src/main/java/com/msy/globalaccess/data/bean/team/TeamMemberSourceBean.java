package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/11.
 * description:团队详情客源地
 */
public class TeamMemberSourceBean implements Parcelable {
    /**
     * childAmount :
     * remark :
     * adultAmount : 2
     * placeInfo :
     */
    private String childAmount;
    private String remark;
    private String adultAmount;
    private String placeInfo;

    public String getChildAmount() {
        return childAmount;
    }

    public void setChildAmount(String childAmount) {
        this.childAmount = childAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAdultAmount() {
        return adultAmount;
    }

    public void setAdultAmount(String adultAmount) {
        this.adultAmount = adultAmount;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.childAmount);
        dest.writeString(this.remark);
        dest.writeString(this.adultAmount);
        dest.writeString(this.placeInfo);
    }

    public TeamMemberSourceBean() {
    }

    protected TeamMemberSourceBean(Parcel in) {
        this.childAmount = in.readString();
        this.remark = in.readString();
        this.adultAmount = in.readString();
        this.placeInfo = in.readString();
    }

    public static final Parcelable.Creator<TeamMemberSourceBean> CREATOR = new Parcelable.Creator<TeamMemberSourceBean>() {
        @Override
        public TeamMemberSourceBean createFromParcel(Parcel source) {
            return new TeamMemberSourceBean(source);
        }

        @Override
        public TeamMemberSourceBean[] newArray(int size) {
            return new TeamMemberSourceBean[size];
        }
    };
}
