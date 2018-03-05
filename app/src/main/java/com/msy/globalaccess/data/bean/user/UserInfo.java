package com.msy.globalaccess.data.bean.user;

import com.msy.globalaccess.data.bean.RoleBean;

import java.util.List;

/**
 * Created by chensh on 2017/2/9 0009.
 */

public class UserInfo {
    /**
     * userRoleType : 0
     * userTel :
     * userId : 85
     * userMobile : 13952848296
     * userName : 张家界国际
     * userEmail :
     * userAddr :
     * userRegDate : 2016-12-02 14:32:31
     */

    private String userRoleType;
    private String userTel;
    private String userId;
    private String userMobile;
    private String userName;
    private String userEmail;
    private String userAddr;
    private String userRegDate;
    private String userSystem;
    private String userUnitName;
    private String userDepName;
    private String unitId;
    private String cityId;
    private String cityName;
    private String province;
    private List<RoleBean> roleList;

    public String getUserSystem() {
        return userSystem;
    }

    public void setUserSystem(String userSystem) {
        this.userSystem = userSystem;
    }

    public String getUserUnitName() {
        return userUnitName;
    }

    public void setUserUnitName(String userUnitName) {
        this.userUnitName = userUnitName;
    }

    public String getUserDepName() {
        return userDepName;
    }

    public void setUserDepName(String userDepName) {
        this.userDepName = userDepName;
    }

    public String getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(String userRoleType) {
        this.userRoleType = userRoleType;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getUserRegDate() {
        return userRegDate;
    }

    public void setUserRegDate(String userRegDate) {
        this.userRegDate = userRegDate;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<RoleBean> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleBean> roleList) {
        this.roleList = roleList;
    }
}
