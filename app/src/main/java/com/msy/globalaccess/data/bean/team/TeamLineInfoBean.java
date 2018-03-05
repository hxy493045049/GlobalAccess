package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/11.
 * description:团队详情线路信息
 */
public class TeamLineInfoBean implements Parcelable {
    /**
     * lunchInfo : 中餐
     * shoppingInfo : 旅游购物
     * scenicInfo :行程
     * breakFastInfo : 早餐
     * cultureInfo : 文化演绎
     * tripDate : 行程日期
     * hotelInfo : 住宿
     * dinnerInfo : 晚餐
     * placeInfo : 目的地
     */
    private String teamTripId;
    private String lunchInfo;
    private String shoppingInfo;
    private String scenicInfo;
    private String breakFastInfo;
    private String cultureInfo;
    private String tripDate;
    private String hotelInfo;
    private String dinnerInfo;
    private String placeInfo;
    /**
     * 在变更行程界面---第几天使用。
     */
    private int dataPosition = 0;

    public String getTeamTripId() {
        return teamTripId;
    }

    public void setTeamTripId(String teamTripId) {
        this.teamTripId = teamTripId;
    }

    public String getLunchInfo() {
        return lunchInfo;
    }

    public void setLunchInfo(String lunchInfo) {
        this.lunchInfo = lunchInfo;
    }

    public String getShoppingInfo() {
        return shoppingInfo;
    }

    public void setShoppingInfo(String shoppingInfo) {
        this.shoppingInfo = shoppingInfo;
    }

    public String getScenicInfo() {
        return scenicInfo;
    }

    public void setScenicInfo(String scenicInfo) {
        this.scenicInfo = scenicInfo;
    }

    public String getBreakFastInfo() {
        return breakFastInfo;
    }

    public void setBreakFastInfo(String breakFastInfo) {
        this.breakFastInfo = breakFastInfo;
    }

    public String getCultureInfo() {
        return cultureInfo;
    }

    public void setCultureInfo(String cultureInfo) {
        this.cultureInfo = cultureInfo;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getHotelInfo() {
        return hotelInfo;
    }

    public void setHotelInfo(String hotelInfo) {
        this.hotelInfo = hotelInfo;
    }

    public String getDinnerInfo() {
        return dinnerInfo;
    }

    public void setDinnerInfo(String dinnerInfo) {
        this.dinnerInfo = dinnerInfo;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public void setDataPosition(int dataPosition) {
        this.dataPosition = dataPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teamTripId);
        dest.writeString(this.lunchInfo);
        dest.writeString(this.shoppingInfo);
        dest.writeString(this.scenicInfo);
        dest.writeString(this.breakFastInfo);
        dest.writeString(this.cultureInfo);
        dest.writeString(this.tripDate);
        dest.writeString(this.hotelInfo);
        dest.writeString(this.dinnerInfo);
        dest.writeString(this.placeInfo);
        dest.writeInt(this.dataPosition);
    }

    public TeamLineInfoBean() {
    }

    protected TeamLineInfoBean(Parcel in) {
        this.teamTripId = in.readString();
        this.lunchInfo = in.readString();
        this.shoppingInfo = in.readString();
        this.scenicInfo = in.readString();
        this.breakFastInfo = in.readString();
        this.cultureInfo = in.readString();
        this.tripDate = in.readString();
        this.hotelInfo = in.readString();
        this.dinnerInfo = in.readString();
        this.placeInfo = in.readString();
        this.dataPosition = in.readInt();
    }

    public static final Parcelable.Creator<TeamLineInfoBean> CREATOR = new Parcelable.Creator<TeamLineInfoBean>() {
        @Override
        public TeamLineInfoBean createFromParcel(Parcel source) {
            return new TeamLineInfoBean(source);
        }

        @Override
        public TeamLineInfoBean[] newArray(int size) {
            return new TeamLineInfoBean[size];
        }
    };
}
