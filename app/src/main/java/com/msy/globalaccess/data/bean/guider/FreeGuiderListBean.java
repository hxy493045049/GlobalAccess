package com.msy.globalaccess.data.bean.guider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shawn on 2017/3/29 0029.
 * <p>
 * description :
 */

public class FreeGuiderListBean implements Parcelable {

    public static final Parcelable.Creator<FreeGuiderListBean> CREATOR = new Parcelable.Creator<FreeGuiderListBean>() {
        @Override
        public FreeGuiderListBean createFromParcel(Parcel source) {
            return new FreeGuiderListBean(source);
        }

        @Override
        public FreeGuiderListBean[] newArray(int size) {
            return new FreeGuiderListBean[size];
        }
    };
    /**
     * allGuideCode :
     * applyDate : 2017-01-09 10:50:16
     * appointEndDate :
     * appointStartDate :
     * auditDate :
     * auditDateBegin :
     * auditDateEnd :
     * birthdayMonth :
     * birthdayYear :
     * checkTag : 1
     * city :
     * cityId : 2378
     * cityIdName :
     * cityName : 黟县
     * context :
     * count :
     * current :
     * delFlag :
     * delTag : 0
     * entityPage :
     * freeType :
     * guideCode : D2011-05-052
     * guideId : 346
     * guideLevel :
     * guideLevelName :
     * guideType :
     * handPhone : 18813484011
     * ins :
     * isOur : 1
     * joinDate :
     * language :
     * languageName :
     * licCode :
     * licDate :
     * licDateBegin :
     * licDateDay :
     * licDateEnd :
     * licDateMonth :
     * licDateYear :
     * mustAppoint : 0
     * name : 空闲A（专用勿动）
     * nation :
     * notGuideId :
     * ownerType :
     * pastDue :
     * pcityId :
     * pictureUrl : 1bb6e3a57cef406ea5f35f42fb17409f.jpg
     * province :
     * regType :
     * serverType :
     * sex : 0
     * spId : 4
     * teamInfoId :
     * travelAgentId : 224
     * travelAgentIdName :
     * travelAgentJYXKCode :
     * travelAgentName : 康辉旅行社
     * updateFiled1Name :
     * updateFiled1Value :
     */

    private String allGuideCode;
    private String applyDate;
    private String appointEndDate;
    private String appointStartDate;
    private String auditDate;
    private String auditDateBegin;
    private String auditDateEnd;
    private String birthdayMonth;
    private String birthdayYear;
    private String checkTag;
    private String city;
    private String cityId;
    private String cityIdName;
    private String cityName;
    private String context;
    private String count;
    private String current;
    private String delFlag;
    private String delTag;
    private String entityPage;
    private String freeType;
    private String guideCode;
    private String guideId;
    private String guideLevel;
    private String guideLevelName;
    private String guideType;
    private String handPhone;
    private String ins;
    private String isOur;
    private String joinDate;
    private String language;
    private String languageName;
    private String licCode;
    private String licDate;
    private String licDateBegin;
    private String licDateDay;
    private String licDateEnd;
    private String licDateMonth;
    private String licDateYear;
    private String mustAppoint;
    private String name;
    private String nation;
    private String notGuideId;
    private String ownerType;
    private String pastDue;
    private String pcityId;
    private String pictureUrl;
    private String province;
    private String regType;
    private String serverType;
    private String sex;
    private String spId;
    private String teamInfoId;
    private String travelAgentId;
    private String travelAgentIdName;
    private String travelAgentJYXKCode;
    private String travelAgentName;
    private String updateFiled1Name;
    private String updateFiled1Value;

    public FreeGuiderListBean() {
    }

    protected FreeGuiderListBean(Parcel in) {
        this.allGuideCode = in.readString();
        this.applyDate = in.readString();
        this.appointEndDate = in.readString();
        this.appointStartDate = in.readString();
        this.auditDate = in.readString();
        this.auditDateBegin = in.readString();
        this.auditDateEnd = in.readString();
        this.birthdayMonth = in.readString();
        this.birthdayYear = in.readString();
        this.checkTag = in.readString();
        this.city = in.readString();
        this.cityId = in.readString();
        this.cityIdName = in.readString();
        this.cityName = in.readString();
        this.context = in.readString();
        this.count = in.readString();
        this.current = in.readString();
        this.delFlag = in.readString();
        this.delTag = in.readString();
        this.entityPage = in.readString();
        this.freeType = in.readString();
        this.guideCode = in.readString();
        this.guideId = in.readString();
        this.guideLevel = in.readString();
        this.guideLevelName = in.readString();
        this.guideType = in.readString();
        this.handPhone = in.readString();
        this.ins = in.readString();
        this.isOur = in.readString();
        this.joinDate = in.readString();
        this.language = in.readString();
        this.languageName = in.readString();
        this.licCode = in.readString();
        this.licDate = in.readString();
        this.licDateBegin = in.readString();
        this.licDateDay = in.readString();
        this.licDateEnd = in.readString();
        this.licDateMonth = in.readString();
        this.licDateYear = in.readString();
        this.mustAppoint = in.readString();
        this.name = in.readString();
        this.nation = in.readString();
        this.notGuideId = in.readString();
        this.ownerType = in.readString();
        this.pastDue = in.readString();
        this.pcityId = in.readString();
        this.pictureUrl = in.readString();
        this.province = in.readString();
        this.regType = in.readString();
        this.serverType = in.readString();
        this.sex = in.readString();
        this.spId = in.readString();
        this.teamInfoId = in.readString();
        this.travelAgentId = in.readString();
        this.travelAgentIdName = in.readString();
        this.travelAgentJYXKCode = in.readString();
        this.travelAgentName = in.readString();
        this.updateFiled1Name = in.readString();
        this.updateFiled1Value = in.readString();
    }

    public String getAllGuideCode() {
        return allGuideCode;
    }

    public void setAllGuideCode(String allGuideCode) {
        this.allGuideCode = allGuideCode;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
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

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditDateBegin() {
        return auditDateBegin;
    }

    public void setAuditDateBegin(String auditDateBegin) {
        this.auditDateBegin = auditDateBegin;
    }

    public String getAuditDateEnd() {
        return auditDateEnd;
    }

    public void setAuditDateEnd(String auditDateEnd) {
        this.auditDateEnd = auditDateEnd;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(String birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getCheckTag() {
        return checkTag;
    }

    public void setCheckTag(String checkTag) {
        this.checkTag = checkTag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityIdName() {
        return cityIdName;
    }

    public void setCityIdName(String cityIdName) {
        this.cityIdName = cityIdName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelTag() {
        return delTag;
    }

    public void setDelTag(String delTag) {
        this.delTag = delTag;
    }

    public String getEntityPage() {
        return entityPage;
    }

    public void setEntityPage(String entityPage) {
        this.entityPage = entityPage;
    }

    public String getFreeType() {
        return freeType;
    }

    public void setFreeType(String freeType) {
        this.freeType = freeType;
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

    public String getGuideLevel() {
        return guideLevel;
    }

    public void setGuideLevel(String guideLevel) {
        this.guideLevel = guideLevel;
    }

    public String getGuideLevelName() {
        return guideLevelName;
    }

    public void setGuideLevelName(String guideLevelName) {
        this.guideLevelName = guideLevelName;
    }

    public String getGuideType() {
        return guideType;
    }

    public void setGuideType(String guideType) {
        this.guideType = guideType;
    }

    public String getHandPhone() {
        return handPhone;
    }

    public void setHandPhone(String handPhone) {
        this.handPhone = handPhone;
    }

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public String getIsOur() {
        return isOur;
    }

    public void setIsOur(String isOur) {
        this.isOur = isOur;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLicCode() {
        return licCode;
    }

    public void setLicCode(String licCode) {
        this.licCode = licCode;
    }

    public String getLicDate() {
        return licDate;
    }

    public void setLicDate(String licDate) {
        this.licDate = licDate;
    }

    public String getLicDateBegin() {
        return licDateBegin;
    }

    public void setLicDateBegin(String licDateBegin) {
        this.licDateBegin = licDateBegin;
    }

    public String getLicDateDay() {
        return licDateDay;
    }

    public void setLicDateDay(String licDateDay) {
        this.licDateDay = licDateDay;
    }

    public String getLicDateEnd() {
        return licDateEnd;
    }

    public void setLicDateEnd(String licDateEnd) {
        this.licDateEnd = licDateEnd;
    }

    public String getLicDateMonth() {
        return licDateMonth;
    }

    public void setLicDateMonth(String licDateMonth) {
        this.licDateMonth = licDateMonth;
    }

    public String getLicDateYear() {
        return licDateYear;
    }

    public void setLicDateYear(String licDateYear) {
        this.licDateYear = licDateYear;
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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNotGuideId() {
        return notGuideId;
    }

    public void setNotGuideId(String notGuideId) {
        this.notGuideId = notGuideId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getPastDue() {
        return pastDue;
    }

    public void setPastDue(String pastDue) {
        this.pastDue = pastDue;
    }

    public String getPcityId() {
        return pcityId;
    }

    public void setPcityId(String pcityId) {
        this.pcityId = pcityId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getTeamInfoId() {
        return teamInfoId;
    }

    public void setTeamInfoId(String teamInfoId) {
        this.teamInfoId = teamInfoId;
    }

    public String getTravelAgentId() {
        return travelAgentId;
    }

    public void setTravelAgentId(String travelAgentId) {
        this.travelAgentId = travelAgentId;
    }

    public String getTravelAgentIdName() {
        return travelAgentIdName;
    }

    public void setTravelAgentIdName(String travelAgentIdName) {
        this.travelAgentIdName = travelAgentIdName;
    }

    public String getTravelAgentJYXKCode() {
        return travelAgentJYXKCode;
    }

    public void setTravelAgentJYXKCode(String travelAgentJYXKCode) {
        this.travelAgentJYXKCode = travelAgentJYXKCode;
    }

    public String getTravelAgentName() {
        return travelAgentName;
    }

    public void setTravelAgentName(String travelAgentName) {
        this.travelAgentName = travelAgentName;
    }

    public String getUpdateFiled1Name() {
        return updateFiled1Name;
    }

    public void setUpdateFiled1Name(String updateFiled1Name) {
        this.updateFiled1Name = updateFiled1Name;
    }

    public String getUpdateFiled1Value() {
        return updateFiled1Value;
    }

    public void setUpdateFiled1Value(String updateFiled1Value) {
        this.updateFiled1Value = updateFiled1Value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.allGuideCode);
        dest.writeString(this.applyDate);
        dest.writeString(this.appointEndDate);
        dest.writeString(this.appointStartDate);
        dest.writeString(this.auditDate);
        dest.writeString(this.auditDateBegin);
        dest.writeString(this.auditDateEnd);
        dest.writeString(this.birthdayMonth);
        dest.writeString(this.birthdayYear);
        dest.writeString(this.checkTag);
        dest.writeString(this.city);
        dest.writeString(this.cityId);
        dest.writeString(this.cityIdName);
        dest.writeString(this.cityName);
        dest.writeString(this.context);
        dest.writeString(this.count);
        dest.writeString(this.current);
        dest.writeString(this.delFlag);
        dest.writeString(this.delTag);
        dest.writeString(this.entityPage);
        dest.writeString(this.freeType);
        dest.writeString(this.guideCode);
        dest.writeString(this.guideId);
        dest.writeString(this.guideLevel);
        dest.writeString(this.guideLevelName);
        dest.writeString(this.guideType);
        dest.writeString(this.handPhone);
        dest.writeString(this.ins);
        dest.writeString(this.isOur);
        dest.writeString(this.joinDate);
        dest.writeString(this.language);
        dest.writeString(this.languageName);
        dest.writeString(this.licCode);
        dest.writeString(this.licDate);
        dest.writeString(this.licDateBegin);
        dest.writeString(this.licDateDay);
        dest.writeString(this.licDateEnd);
        dest.writeString(this.licDateMonth);
        dest.writeString(this.licDateYear);
        dest.writeString(this.mustAppoint);
        dest.writeString(this.name);
        dest.writeString(this.nation);
        dest.writeString(this.notGuideId);
        dest.writeString(this.ownerType);
        dest.writeString(this.pastDue);
        dest.writeString(this.pcityId);
        dest.writeString(this.pictureUrl);
        dest.writeString(this.province);
        dest.writeString(this.regType);
        dest.writeString(this.serverType);
        dest.writeString(this.sex);
        dest.writeString(this.spId);
        dest.writeString(this.teamInfoId);
        dest.writeString(this.travelAgentId);
        dest.writeString(this.travelAgentIdName);
        dest.writeString(this.travelAgentJYXKCode);
        dest.writeString(this.travelAgentName);
        dest.writeString(this.updateFiled1Name);
        dest.writeString(this.updateFiled1Value);
    }
}
