package com.msy.globalaccess.data.bean.user;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hxy on 2016/12/6 0006.
 * <p>
 * description :
 */

@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserLoginStatus() {
        return userLoginStatus;
    }

    public void setUserLoginStatus(int userLoginStatus) {
        this.userLoginStatus = userLoginStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public String getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(String userRoleType) {
        this.userRoleType = userRoleType;
    }

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


    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleList() {
        return this.roleList;
    }

    public void setRoleList(String roleList) {
        this.roleList = roleList;
    }

    private String userId;
    private String userAccount;
    private String userPassword;
    private int userLoginStatus;
    private String username;
    private String userTel;
    private String userMobile;
    private String userEmail;
    private String userAddr;
    private String userRegDate;
    private String userRoleType;
    private String userSystem;
    private String userUnitName;
    private String userDepName;
    private String roleList;
    private String unitId;
    private String cityId;
    private String cityName;
    private String province;

    @Generated(hash = 1845084766)
    public User(Long id, String userId, String userAccount, String userPassword,
            int userLoginStatus, String username, String userTel, String userMobile,
            String userEmail, String userAddr, String userRegDate,
            String userRoleType, String userSystem, String userUnitName,
            String userDepName, String roleList, String unitId, String cityId,
            String cityName, String province) {
        this.id = id;
        this.userId = userId;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userLoginStatus = userLoginStatus;
        this.username = username;
        this.userTel = userTel;
        this.userMobile = userMobile;
        this.userEmail = userEmail;
        this.userAddr = userAddr;
        this.userRegDate = userRegDate;
        this.userRoleType = userRoleType;
        this.userSystem = userSystem;
        this.userUnitName = userUnitName;
        this.userDepName = userDepName;
        this.roleList = roleList;
        this.unitId = unitId;
        this.cityId = cityId;
        this.cityName = cityName;
        this.province = province;
    }

    @Generated(hash = 586692638)
    public User() {
    }



}
