package com.msy.globalaccess.data.bean.tourism;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/14.
 * description: 游客名单上面的信息
 */
public class TouristsInfoBean implements Parcelable {

    private String travelAgentName;
    private String teamCode;
    private String driverPhone;
    private String tripDates;
    private String guideInfos;
    private String vehicleInfos;
    private String peopleCount;

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getTripDates() {
        return tripDates;
    }

    public void setTripDates(String tripDates) {
        this.tripDates = tripDates;
    }

    public String getGuideInfos() {
        return guideInfos;
    }

    public void setGuideInfos(String guideInfos) {
        this.guideInfos = guideInfos;
    }

    public String getVehicleInfos() {
        return vehicleInfos;
    }

    public void setVehicleInfos(String vehicleInfos) {
        this.vehicleInfos = vehicleInfos;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.travelAgentName);
        dest.writeString(this.teamCode);
        dest.writeString(this.driverPhone);
        dest.writeString(this.tripDates);
        dest.writeString(this.guideInfos);
        dest.writeString(this.vehicleInfos);
        dest.writeString(this.peopleCount);
    }

    public TouristsInfoBean() {
    }

    protected TouristsInfoBean(Parcel in) {
        this.travelAgentName = in.readString();
        this.teamCode = in.readString();
        this.driverPhone = in.readString();
        this.tripDates = in.readString();
        this.guideInfos = in.readString();
        this.vehicleInfos = in.readString();
        this.peopleCount = in.readString();
    }

    public static final Parcelable.Creator<TouristsInfoBean> CREATOR = new Parcelable.Creator<TouristsInfoBean>() {
        @Override
        public TouristsInfoBean createFromParcel(Parcel source) {
            return new TouristsInfoBean(source);
        }

        @Override
        public TouristsInfoBean[] newArray(int size) {
            return new TouristsInfoBean[size];
        }
    };
}
