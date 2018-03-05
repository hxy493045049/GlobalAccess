package com.msy.globalaccess.data.bean.guider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/7/4
 * description: 导游委派~导游信息
 *
 */
public class TouristDelegateBean implements Parcelable {

    private String teamGuideId;
    private String teamId;
    private String teamCode;
    private String teamGuideName;
    private String createTime;
    private String opType;
    private String operStatus;
    private String operName;

    public String getTeamGuideId() {
        return teamGuideId;
    }

    public void setTeamGuideId(String teamGuideId) {
        this.teamGuideId = teamGuideId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamGuideName() {
        return teamGuideName;
    }

    public void setTeamGuideName(String teamGuideName) {
        this.teamGuideName = teamGuideName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teamGuideId);
        dest.writeString(this.teamId);
        dest.writeString(this.teamCode);
        dest.writeString(this.teamGuideName);
        dest.writeString(this.createTime);
        dest.writeString(this.opType);
        dest.writeString(this.operStatus);
        dest.writeString(this.operName);
    }

    public TouristDelegateBean() {
    }

    protected TouristDelegateBean(Parcel in) {
        this.teamGuideId = in.readString();
        this.teamId = in.readString();
        this.teamCode = in.readString();
        this.teamGuideName = in.readString();
        this.createTime = in.readString();
        this.opType = in.readString();
        this.operStatus = in.readString();
        this.operName = in.readString();
    }

    public static final Parcelable.Creator<TouristDelegateBean> CREATOR = new Parcelable.Creator<TouristDelegateBean>() {
        @Override
        public TouristDelegateBean createFromParcel(Parcel source) {
            return new TouristDelegateBean(source);
        }

        @Override
        public TouristDelegateBean[] newArray(int size) {
            return new TouristDelegateBean[size];
        }
    };
}
