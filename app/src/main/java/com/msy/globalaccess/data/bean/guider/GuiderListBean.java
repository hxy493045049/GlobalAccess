package com.msy.globalaccess.data.bean.guider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shawn on 2017/3/17 0017.
 * <p>
 * description :  注意:这个类使用了cloneable接口,注意,如果数据结构变更,有数组或者集合的时候,需要重载clone方法
 */
public class GuiderListBean implements Parcelable, Cloneable {


    public static final Parcelable.Creator<GuiderListBean> CREATOR = new Parcelable.Creator<GuiderListBean>() {
        @Override
        public GuiderListBean createFromParcel(Parcel source) {
            return new GuiderListBean(source);
        }

        @Override
        public GuiderListBean[] newArray(int size) {
            return new GuiderListBean[size];
        }
    };
    /**
     * appointComment :
     * appointEndDate : 2017-03-22 06:00
     * appointStartDate : 2017-03-22 05:00
     * changer : 康辉高桥
     * changerUid : 282
     * createTime : 2017-03-21 17:51:24
     * createTimeEnd :
     * createTimeStart :
     * creater : 康辉高桥
     * createrUid : 282
     * delFlag : 0
     * entityPage :
     * guideAppointBillId :
     * guideCode : D-mm001
     * guideId : 87
     * handleStatus :
     * isOur : 0
     * mustAppoint : 1
     * name : 马萱萱
     * opStatus : 2
     * opType : 0
     * operCom :
     * operStatus :
     * phoneNum : 13456788911
     * sex : 1
     * storeId :
     * teamCode : 10061641
     * teamCreater : 康辉高桥
     * teamGuideId : 2202
     * teamInfoId : 6164
     * teamOpRecord : 12657
     * travelAgentId : 224
     * travelAgentName : 康辉旅行社
     * tripeDate : 2017-03-22
     * tripsDate : 2017-03-22
     * updateTime : 2017-03-21 17:51:49
     * isNativeGuider : 0
     */
    private String appointComment;
    private String appointEndDate;
    private String appointStartDate;
    private String changer;
    private String changerUid;
    private String createTime;
    private String createTimeEnd;
    private String createTimeStart;
    private String creater;
    private String createrUid;
    private String delFlag;
    private String entityPage;
    private String guideAppointBillId;
    private String guideCode;
    private String guideId;
    private String handleStatus;
    private String isOur;
    private String mustAppoint;
    private String name;
    private String opStatus;
    private String opType;
    private String operCom;
    private String operStatus;
    private String phoneNum;
    private String sex;
    private String storeId;
    private String teamCode;
    private String teamCreater;
    private String teamGuideId;
    private String teamInfoId;
    private String teamOpRecord;
    private String travelAgentId;
    private String travelAgentName;
    private String tripeDate;
    private String tripsDate;
    private String updateTime;
    //自己添加的一个字段,用于区分是本地的导游(新增),还是服务器返回的导游,"1"为本地
    private String isNativeGuider = "";
    private String isModified = "0";//判断是否做过修改,1 修改过,0,未修改

    public GuiderListBean() {
    }

    protected GuiderListBean(Parcel in) {
        this.appointComment = in.readString();
        this.appointEndDate = in.readString();
        this.appointStartDate = in.readString();
        this.changer = in.readString();
        this.changerUid = in.readString();
        this.createTime = in.readString();
        this.createTimeEnd = in.readString();
        this.createTimeStart = in.readString();
        this.creater = in.readString();
        this.createrUid = in.readString();
        this.delFlag = in.readString();
        this.entityPage = in.readString();
        this.guideAppointBillId = in.readString();
        this.guideCode = in.readString();
        this.guideId = in.readString();
        this.handleStatus = in.readString();
        this.isOur = in.readString();
        this.mustAppoint = in.readString();
        this.name = in.readString();
        this.opStatus = in.readString();
        this.opType = in.readString();
        this.operCom = in.readString();
        this.operStatus = in.readString();
        this.phoneNum = in.readString();
        this.sex = in.readString();
        this.storeId = in.readString();
        this.teamCode = in.readString();
        this.teamCreater = in.readString();
        this.teamGuideId = in.readString();
        this.teamInfoId = in.readString();
        this.teamOpRecord = in.readString();
        this.travelAgentId = in.readString();
        this.travelAgentName = in.readString();
        this.tripeDate = in.readString();
        this.tripsDate = in.readString();
        this.updateTime = in.readString();
        this.isNativeGuider = in.readString();
    }

    public String getIsModified() {
        return isModified;
    }

    public void setIsModified(String isModified) {
        this.isModified = isModified;
    }

    public String getAppointComment() {
        return appointComment;
    }

    public void setAppointComment(String appointComment) {
        this.appointComment = appointComment;
    }

    public String getAppointEndDate() {
        return appointEndDate;
    }

    public void setAppointEndDate(String appointEndDate) {
        this.appointEndDate = appointEndDate;
    }

    public String getAppointStartDate() {
        return appointStartDate;
    }

    public void setAppointStartDate(String appointStartDate) {
        this.appointStartDate = appointStartDate;
    }

    public String getChanger() {
        return changer;
    }

    public void setChanger(String changer) {
        this.changer = changer;
    }

    public String getChangerUid() {
        return changerUid;
    }

    public void setChangerUid(String changerUid) {
        this.changerUid = changerUid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterUid() {
        return createrUid;
    }

    public void setCreaterUid(String createrUid) {
        this.createrUid = createrUid;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getEntityPage() {
        return entityPage;
    }

    public void setEntityPage(String entityPage) {
        this.entityPage = entityPage;
    }

    public String getGuideAppointBillId() {
        return guideAppointBillId;
    }

    public void setGuideAppointBillId(String guideAppointBillId) {
        this.guideAppointBillId = guideAppointBillId;
    }

    public String getGuideCode() {
        return guideCode;
    }

    public void setGuideCode(String guideCode) {
        this.guideCode = guideCode;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getIsOur() {
        return isOur;
    }

    public void setIsOur(String isOur) {
        this.isOur = isOur;
    }

    public String getMustAppoint() {
        return mustAppoint;
    }

    public void setMustAppoint(String mustAppoint) {
        this.mustAppoint = mustAppoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(String opStatus) {
        this.opStatus = opStatus;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getOperCom() {
        return operCom;
    }

    public void setOperCom(String operCom) {
        this.operCom = operCom;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public String getTeamCreater() {
        return teamCreater;
    }

    public void setTeamCreater(String teamCreater) {
        this.teamCreater = teamCreater;
    }

    public String getTeamGuideId() {
        return teamGuideId;
    }

    public void setTeamGuideId(String teamGuideId) {
        this.teamGuideId = teamGuideId;
    }

    public String getTeamInfoId() {
        return teamInfoId;
    }

    public void setTeamInfoId(String teamInfoId) {
        this.teamInfoId = teamInfoId;
    }

    public String getTeamOpRecord() {
        return teamOpRecord;
    }

    public void setTeamOpRecord(String teamOpRecord) {
        this.teamOpRecord = teamOpRecord;
    }

    public String getTravelAgentId() {
        return travelAgentId;
    }

    public void setTravelAgentId(String travelAgentId) {
        this.travelAgentId = travelAgentId;
    }

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getTripeDate() {
        return tripeDate;
    }

    public void setTripeDate(String tripeDate) {
        this.tripeDate = tripeDate;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getTripsDate() {
        return tripsDate;
    }

    public void setTripsDate(String tripsDate) {
        this.tripsDate = tripsDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIsNativeGuider() {
        return isNativeGuider;
    }

    public void setIsNativeGuider(String isNativeGuider) {
        this.isNativeGuider = isNativeGuider;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appointComment);
        dest.writeString(this.appointEndDate);
        dest.writeString(this.appointStartDate);
        dest.writeString(this.changer);
        dest.writeString(this.changerUid);
        dest.writeString(this.createTime);
        dest.writeString(this.createTimeEnd);
        dest.writeString(this.createTimeStart);
        dest.writeString(this.creater);
        dest.writeString(this.createrUid);
        dest.writeString(this.delFlag);
        dest.writeString(this.entityPage);
        dest.writeString(this.guideAppointBillId);
        dest.writeString(this.guideCode);
        dest.writeString(this.guideId);
        dest.writeString(this.handleStatus);
        dest.writeString(this.isOur);
        dest.writeString(this.mustAppoint);
        dest.writeString(this.name);
        dest.writeString(this.opStatus);
        dest.writeString(this.opType);
        dest.writeString(this.operCom);
        dest.writeString(this.operStatus);
        dest.writeString(this.phoneNum);
        dest.writeString(this.sex);
        dest.writeString(this.storeId);
        dest.writeString(this.teamCode);
        dest.writeString(this.teamCreater);
        dest.writeString(this.teamGuideId);
        dest.writeString(this.teamInfoId);
        dest.writeString(this.teamOpRecord);
        dest.writeString(this.travelAgentId);
        dest.writeString(this.travelAgentName);
        dest.writeString(this.tripeDate);
        dest.writeString(this.tripsDate);
        dest.writeString(this.updateTime);
        dest.writeString(this.isNativeGuider);
    }
}
