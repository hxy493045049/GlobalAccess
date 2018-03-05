package com.msy.globalaccess.data.bean.ticket;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WuDebin on 2017/3/21.
 */

public class TripScenicTicketBean implements Parcelable {

    private String scenicTicketTypeId;//票据ID
    private String ticketPriceName;//票据名称
    private String price;//单价
    private String amount = "0";//预订数量
    private String ticketType;//0-成人1-儿童2-特殊
    private String tripScenicTicketItemId;//团队行程景点关联订票明细ID
    private String changeType = "0";//变更的状态 0未变更 1删除 2新增 3修改
    private String isDate = "0";//是否预约 0或者空 未预约 1已预约

    public TripScenicTicketBean() {

    }

    protected TripScenicTicketBean(Parcel in) {
        scenicTicketTypeId = in.readString();
        ticketPriceName = in.readString();
        price = in.readString();
        amount = in.readString();
        ticketType = in.readString();
        tripScenicTicketItemId = in.readString();
        changeType = in.readString();
        isDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scenicTicketTypeId);
        dest.writeString(ticketPriceName);
        dest.writeString(price);
        dest.writeString(amount);
        dest.writeString(ticketType);
        dest.writeString(tripScenicTicketItemId);
        dest.writeString(changeType);
        dest.writeString(isDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripScenicTicketBean> CREATOR = new Creator<TripScenicTicketBean>() {
        @Override
        public TripScenicTicketBean createFromParcel(Parcel in) {
            return new TripScenicTicketBean(in);
        }

        @Override
        public TripScenicTicketBean[] newArray(int size) {
            return new TripScenicTicketBean[size];
        }
    };

    public String getScenicTicketTypeId() {
        return scenicTicketTypeId;
    }

    public void setScenicTicketTypeId(String scenicTicketTypeId) {
        this.scenicTicketTypeId = scenicTicketTypeId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getTripScenicTicketItemId() {
        return tripScenicTicketItemId;
    }

    public void setTripScenicTicketItemId(String tripScenicTicketItemId) {
        this.tripScenicTicketItemId = tripScenicTicketItemId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTicketPriceName() {
        return ticketPriceName;
    }

    public void setTicketPriceName(String ticketPriceName) {
        this.ticketPriceName = ticketPriceName;
    }

    public String getIsDate() {
        return isDate;
    }

    public void setIsDate(String isDate) {
        this.isDate = isDate;
    }
}
