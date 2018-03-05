package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/11.
 * description:团队详情基本信息
 */
public class TeamBaseInfoBean implements Parcelable {

    /**
     * travelAgentName : 测试旅行社1
     * insInfo : [未购旅行社责任险][未购团队意外险]
     * departureInfo : 2016-11-21 2016-11-21 09:00 张家界/汽车湘A304540班次1
     * teamCode : 112101
     * teamTypeName : 地接国内
     * teamStatus : 0
     * teamGroup : 测试组团社
     * teamId : 1
     * returnInfo :  2016-11-25 09:00 张家界/汽车
     * leader : 王康
     * lineName : 黄龙洞 3日游
     * travelDepName :
     * teamQrCode :
     */

    private String travelAgentName;
    private String insInfo;
    private String departureInfo;
    private String teamCode;
    private String teamNum;
    private String teamTypeName;
    private int teamStatus;
    private String teamGroup;
    private String teamId;
    private String returnInfo;
    private String leader;
    private String lineName;
    private String travelDepName;
    private String teamQrCode;

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getInsInfo() {
        return insInfo;
    }

    public void setInsInfo(String insInfo) {
        this.insInfo = insInfo;
    }

    public String getDepartureInfo() {
        return departureInfo;
    }

    public void setDepartureInfo(String departureInfo) {
        this.departureInfo = departureInfo;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamTypeName() {
        return teamTypeName;
    }

    public void setTeamTypeName(String teamTypeName) {
        this.teamTypeName = teamTypeName;
    }

    public String getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(String teamNum) {
        this.teamNum = teamNum;
    }

    public int getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(int teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getTeamGroup() {
        return teamGroup;
    }

    public void setTeamGroup(String teamGroup) {
        this.teamGroup = teamGroup;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getTravelDepName() {
        return travelDepName;
    }

    public void setTravelDepName(String travelDepName) {
        this.travelDepName = travelDepName;
    }

    public String getTeamQrCode() {
        return teamQrCode;
    }

    public void setTeamQrCode(String teamQrCode) {
        this.teamQrCode = teamQrCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.travelAgentName);
        dest.writeString(this.insInfo);
        dest.writeString(this.departureInfo);
        dest.writeString(this.teamCode);
        dest.writeString(this.teamNum);
        dest.writeString(this.teamTypeName);
        dest.writeInt(this.teamStatus);
        dest.writeString(this.teamGroup);
        dest.writeString(this.teamId);
        dest.writeString(this.returnInfo);
        dest.writeString(this.leader);
        dest.writeString(this.lineName);
        dest.writeString(this.travelDepName);
        dest.writeString(this.teamQrCode);
    }

    public TeamBaseInfoBean() {
    }

    protected TeamBaseInfoBean(Parcel in) {
        this.travelAgentName = in.readString();
        this.insInfo = in.readString();
        this.departureInfo = in.readString();
        this.teamCode = in.readString();
        this.teamNum = in.readString();
        this.teamTypeName = in.readString();
        this.teamStatus = in.readInt();
        this.teamGroup = in.readString();
        this.teamId = in.readString();
        this.returnInfo = in.readString();
        this.leader = in.readString();
        this.lineName = in.readString();
        this.travelDepName = in.readString();
        this.teamQrCode = in.readString();
    }

    public static final Parcelable.Creator<TeamBaseInfoBean> CREATOR = new Parcelable.Creator<TeamBaseInfoBean>() {
        @Override
        public TeamBaseInfoBean createFromParcel(Parcel source) {
            return new TeamBaseInfoBean(source);
        }

        @Override
        public TeamBaseInfoBean[] newArray(int size) {
            return new TeamBaseInfoBean[size];
        }
    };
}
