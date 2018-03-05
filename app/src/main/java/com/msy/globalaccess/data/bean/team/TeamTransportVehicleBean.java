package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/7.
 * description:
 */
public class TeamTransportVehicleBean implements Parcelable {
    /**
     * vehicleNum : 苏L12344
     * prePayMoney : 0.0
     * vehicleTypeName :
     * seatAmount :
     * useTypeName : 游客自带
     * driverPhone : 13909088765
     * companyName :
     * comments :
     */

    private String vehicleNum;
    private String prePayMoney;
    private String vehicleTypeName;
    private String seatAmount;
    private String useTypeName;
    private String driverPhone;
    private String companyName;
    private String comments;

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getPrePayMoney() {
        return prePayMoney;
    }

    public void setPrePayMoney(String prePayMoney) {
        this.prePayMoney = prePayMoney;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getSeatAmount() {
        return seatAmount;
    }

    public void setSeatAmount(String seatAmount) {
        this.seatAmount = seatAmount;
    }

    public String getUseTypeName() {
        return useTypeName;
    }

    public void setUseTypeName(String useTypeName) {
        this.useTypeName = useTypeName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vehicleNum);
        dest.writeString(this.prePayMoney);
        dest.writeString(this.vehicleTypeName);
        dest.writeString(this.seatAmount);
        dest.writeString(this.useTypeName);
        dest.writeString(this.driverPhone);
        dest.writeString(this.companyName);
        dest.writeString(this.comments);
    }

    public TeamTransportVehicleBean() {
    }

    protected TeamTransportVehicleBean(Parcel in) {
        this.vehicleNum = in.readString();
        this.prePayMoney = in.readString();
        this.vehicleTypeName = in.readString();
        this.seatAmount = in.readString();
        this.useTypeName = in.readString();
        this.driverPhone = in.readString();
        this.companyName = in.readString();
        this.comments = in.readString();
    }

    public static final Parcelable.Creator<TeamTransportVehicleBean> CREATOR = new Parcelable.Creator<TeamTransportVehicleBean>() {
        @Override
        public TeamTransportVehicleBean createFromParcel(Parcel source) {
            return new TeamTransportVehicleBean(source);
        }

        @Override
        public TeamTransportVehicleBean[] newArray(int size) {
            return new TeamTransportVehicleBean[size];
        }
    };
}
