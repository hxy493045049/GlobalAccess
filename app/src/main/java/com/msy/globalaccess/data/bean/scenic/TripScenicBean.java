package com.msy.globalaccess.data.bean.scenic;

import android.os.Parcel;
import android.os.Parcelable;

import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;

import java.util.ArrayList;
import java.util.List;

import cn.msy.zc.commonutils.StringUtils;

/**
 * Created by WuDebin on 2017/3/21.
 */

public class TripScenicBean implements Parcelable {
    private String scenicId;//景点ID
    private String tripName;//景点名称
    private String isOrderTicket = "0";//景区是否支持预订 0 不支持 1支持
    private String ptripScenicId;//父景点ID
    private String pscenicName;//父景点名称
    private String remark;//备注
    private String tripScenicId;//团队行程景点关联表ID
    private String adultsAmount;//成人数量
    private String childrenAmout;//儿童数量
    private String delTag = "0";//删除标志 0未删除 1已删除
    private String auditType = "0";//结算方式0-不结算1-实时结算 根据当前景点是否需要结算来判断
    private String prePayMoney;//预付金额
    private String checkStatus = "0";// 作为关联表F_TripScenicOrderID查询团队行程景点认证单表(T_GT_TripScenicOrder)是否可以变更修改，1可以修改 0不可修改
    private String bookingType = "0";//订票状态0-未订票1-订票2-取消订票
    private String changeType = "0";//变更的状态 0未变更 1删除 2新增 3修改
    private String teamTripId;//团队行程ID（4-10需求变更）

    private List<TripScenicTicketBean> tripScenicTicketItemList;

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getPrePayMoney() {
        return prePayMoney;
    }

    public void setPrePayMoney(String prePayMoney) {
        this.prePayMoney = prePayMoney;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getDelTag() {
        return delTag;
    }

    public void setDelTag(String delTag) {
        this.delTag = delTag;
    }

    public int getChildrenAmout() {
        return StringUtils.stringConvertInt(childrenAmout);
    }

    public void setChildrenAmout(String childrenAmout) {
        this.childrenAmout = childrenAmout;
    }

    public int getAdultsAmount() {
        return StringUtils.stringConvertInt(adultsAmount);
    }

    public void setAdultsAmount(String adultsAmount) {
        this.adultsAmount = adultsAmount;
    }

    public String getTripScenicId() {
        return tripScenicId;
    }

    public void setTripScenicId(String tripScenicId) {
        this.tripScenicId = tripScenicId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPscenicName() {
        return pscenicName;
    }

    public void setPscenicName(String pscenicName) {
        this.pscenicName = pscenicName;
    }

    public String getPtripScenicId() {
        return ptripScenicId;
    }

    public void setPtripScenicId(String ptripScenicId) {
        this.ptripScenicId = ptripScenicId;
    }

    public String getIsOrderTicket() {
        return isOrderTicket;
    }

    public void setIsOrderTicket(String isOrderTicket) {
        this.isOrderTicket = isOrderTicket;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public List<TripScenicTicketBean> getTripScenicTicket() {
        return tripScenicTicketItemList;
    }

    public void setTripScenicTicket(List<TripScenicTicketBean> tripScenicTicketItemList) {
        this.tripScenicTicketItemList = tripScenicTicketItemList;
    }

    public String getTeamTripId() {
        return teamTripId;
    }

    public void setTeamTripId(String teamTripId) {
        this.teamTripId = teamTripId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.scenicId);
        dest.writeString(this.tripName);
        dest.writeString(this.isOrderTicket);
        dest.writeString(this.ptripScenicId);
        dest.writeString(this.pscenicName);
        dest.writeString(this.remark);
        dest.writeString(this.tripScenicId);
        dest.writeString(this.adultsAmount);
        dest.writeString(this.childrenAmout);
        dest.writeString(this.delTag);
        dest.writeString(this.auditType);
        dest.writeString(this.prePayMoney);
        dest.writeString(this.checkStatus);
        dest.writeString(this.bookingType);
        dest.writeString(this.changeType);
        dest.writeString(this.teamTripId);
        dest.writeList(this.tripScenicTicketItemList);
    }

    public TripScenicBean() {
    }

    protected TripScenicBean(Parcel in) {
        this.scenicId = in.readString();
        this.tripName = in.readString();
        this.isOrderTicket = in.readString();
        this.ptripScenicId = in.readString();
        this.pscenicName = in.readString();
        this.remark = in.readString();
        this.tripScenicId = in.readString();
        this.adultsAmount = in.readString();
        this.childrenAmout = in.readString();
        this.delTag = in.readString();
        this.auditType = in.readString();
        this.prePayMoney = in.readString();
        this.checkStatus = in.readString();
        this.bookingType = in.readString();
        this.changeType = in.readString();
        this.teamTripId=in.readString();
        this.tripScenicTicketItemList = new ArrayList<TripScenicTicketBean>();
        in.readList(this.tripScenicTicketItemList, TripScenicTicketBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<TripScenicBean> CREATOR = new Parcelable.Creator<TripScenicBean>() {
        @Override
        public TripScenicBean createFromParcel(Parcel source) {
            return new TripScenicBean(source);
        }

        @Override
        public TripScenicBean[] newArray(int size) {
            return new TripScenicBean[size];
        }
    };
}
