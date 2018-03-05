package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/2/10.
 * description:团队详情bean
 */
public class TeamDetailBean implements Parcelable {
    public static final Parcelable.Creator<TeamDetailBean> CREATOR = new Parcelable.Creator<TeamDetailBean>() {
        @Override
        public TeamDetailBean createFromParcel(Parcel source) {
            return new TeamDetailBean(source);
        }

        @Override
        public TeamDetailBean[] newArray(int size) {
            return new TeamDetailBean[size];
        }
    };
    /**
     * 团款情况
     */
    private TeamMoneyInfoBean teamMoneyInfo;
    /**
     * 团队基本信息
     */
    private TeamBaseInfoBean teamBaseInfo;
    /**
     * 运输车辆信息
     */
    private ArrayList<TeamTransportVehicleBean> vehicleInfoList;
    /**
     * 导游信息
     */
    private ArrayList<TeamCiceronInfoBean> guideInfoList;
    /**
     * 团队线路信息
     */
    private ArrayList<TeamLineInfoBean> lineListInfo;
    /**
     * 团队客源地
     */
    private ArrayList<TeamMemberSourceBean> teamMemberSourceList;
    /**
     * 团队成员信息
     */
    private ArrayList<TeamMemberBean> teamMemberList;

    public TeamDetailBean() {
    }

    protected TeamDetailBean(Parcel in) {
        this.teamMoneyInfo = in.readParcelable(TeamMoneyInfoBean.class.getClassLoader());
        this.teamBaseInfo = in.readParcelable(TeamBaseInfoBean.class.getClassLoader());
        this.vehicleInfoList = new ArrayList<TeamTransportVehicleBean>();
        in.readList(this.vehicleInfoList, TeamTransportVehicleBean.class.getClassLoader());
        this.guideInfoList = new ArrayList<TeamCiceronInfoBean>();
        in.readList(this.guideInfoList, TeamCiceronInfoBean.class.getClassLoader());
        this.lineListInfo = new ArrayList<TeamLineInfoBean>();
        in.readList(this.lineListInfo, TeamLineInfoBean.class.getClassLoader());
        this.teamMemberSourceList = new ArrayList<TeamMemberSourceBean>();
        in.readList(this.teamMemberSourceList, TeamMemberSourceBean.class.getClassLoader());
        this.teamMemberList = in.createTypedArrayList(TeamMemberBean.CREATOR);
    }

    public TeamMoneyInfoBean getTeamMoneyInfo() {
        return teamMoneyInfo;
    }

    public void setTeamMoneyInfo(TeamMoneyInfoBean teamMoneyInfo) {
        this.teamMoneyInfo = teamMoneyInfo;
    }

    public TeamBaseInfoBean getTeamBaseInfo() {
        return teamBaseInfo;
    }

    public void setTeamBaseInfo(TeamBaseInfoBean teamBaseInfo) {
        this.teamBaseInfo = teamBaseInfo;
    }

    public ArrayList<TeamTransportVehicleBean> getVehicleInfoList() {
        return vehicleInfoList;
    }

    public void setVehicleInfoList(ArrayList<TeamTransportVehicleBean> vehicleInfoList) {
        this.vehicleInfoList = vehicleInfoList;
    }

    public ArrayList<TeamCiceronInfoBean> getGuideInfoList() {
        return guideInfoList;
    }

    public void setGuideInfoList(ArrayList<TeamCiceronInfoBean> guideInfoList) {
        this.guideInfoList = guideInfoList;
    }

    public ArrayList<TeamLineInfoBean> getLineListInfo() {
        return lineListInfo;
    }

    public void setLineListInfo(ArrayList<TeamLineInfoBean> lineListInfo) {
        this.lineListInfo = lineListInfo;
    }

    public ArrayList<TeamMemberSourceBean> getTeamMemberSourceList() {
        return teamMemberSourceList;
    }

    public void setTeamMemberSourceList(ArrayList<TeamMemberSourceBean> teamMemberSourceList) {
        this.teamMemberSourceList = teamMemberSourceList;
    }

    public ArrayList<TeamMemberBean> getTeamMemberList() {
        return teamMemberList;
    }

    public void setTeamMemberList(ArrayList<TeamMemberBean> teamMemberList) {
        this.teamMemberList = teamMemberList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.teamMoneyInfo, flags);
        dest.writeParcelable(this.teamBaseInfo, flags);
        dest.writeList(this.vehicleInfoList);
        dest.writeList(this.guideInfoList);
        dest.writeList(this.lineListInfo);
        dest.writeList(this.teamMemberSourceList);
        dest.writeTypedList(this.teamMemberList);
    }

    public static class TeamMoneyInfoBean implements Parcelable {
        public static final Creator<TeamMoneyInfoBean> CREATOR = new Creator<TeamMoneyInfoBean>() {
            @Override
            public TeamMoneyInfoBean createFromParcel(Parcel source) {
                return new TeamMoneyInfoBean(source);
            }

            @Override
            public TeamMoneyInfoBean[] newArray(int size) {
                return new TeamMoneyInfoBean[size];
            }
        };
        /**
         * payMoney : 0.0
         * prePayMoney : 0.0
         * backMoney : 0.0
         * remainMoney : 0.0
         */

        private String payMoney;
        private String prePayMoney;
        private String backMoney;
        private String remainMoney;

        public TeamMoneyInfoBean() {
        }

        protected TeamMoneyInfoBean(Parcel in) {
            this.payMoney = in.readString();
            this.prePayMoney = in.readString();
            this.backMoney = in.readString();
            this.remainMoney = in.readString();
        }

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getPrePayMoney() {
            return prePayMoney;
        }

        public void setPrePayMoney(String prePayMoney) {
            this.prePayMoney = prePayMoney;
        }

        public String getBackMoney() {
            return backMoney;
        }

        public void setBackMoney(String backMoney) {
            this.backMoney = backMoney;
        }

        public String getRemainMoney() {
            return remainMoney;
        }

        public void setRemainMoney(String remainMoney) {
            this.remainMoney = remainMoney;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.payMoney);
            dest.writeString(this.prePayMoney);
            dest.writeString(this.backMoney);
            dest.writeString(this.remainMoney);
        }
    }
}
