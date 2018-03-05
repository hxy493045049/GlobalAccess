package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/11.
 * description:团队详情游客信息
 */
public class TeamMemberBean implements Parcelable {
    /**
     * sex : 1
     * cardTypeName : 身份证
     * name : klay
     * birthdate : 1988-08-08
     * cardNum : 321102198808080808
     */

    private int sex;
    private String cardTypeName;
    private String name;
    private String birthdate;
    private String cardNum;
    private String phoneNum;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sex);
        dest.writeString(this.cardTypeName);
        dest.writeString(this.name);
        dest.writeString(this.birthdate);
        dest.writeString(this.cardNum);
        dest.writeString(this.phoneNum);
    }

    public TeamMemberBean() {
    }

    protected TeamMemberBean(Parcel in) {
        this.sex = in.readInt();
        this.cardTypeName = in.readString();
        this.name = in.readString();
        this.birthdate = in.readString();
        this.cardNum = in.readString();
        this.phoneNum = in.readString();
    }

    public static final Parcelable.Creator<TeamMemberBean> CREATOR = new Parcelable.Creator<TeamMemberBean>() {
        @Override
        public TeamMemberBean createFromParcel(Parcel source) {
            return new TeamMemberBean(source);
        }

        @Override
        public TeamMemberBean[] newArray(int size) {
            return new TeamMemberBean[size];
        }
    };
}
