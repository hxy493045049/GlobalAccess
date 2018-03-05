package com.msy.globalaccess.data.bean.team;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pepys on 2017/2/8.
 * description: 团队详情时间线
 */
public class TeamDetailTimeLineBean implements Parcelable {
    /**
     * 0：第几天
     * 1：行程
     * 2：景点名称
     * 3：餐饮
     * 4：住宿
     * 5：演出
     * 6：购物
     */
    private int itemType;

    private String title;
    /**
     * 第几天
     */
    private int dataHead;

    private String content;

    public TeamDetailTimeLineBean() {
    }

    /**
     *
     * @param itemType
     * @param title
     * @param content
     */
    public TeamDetailTimeLineBean(int itemType, String title, String content) {
        this.itemType = itemType;
        this.title = title;
        this.content = content;
    }

    public int getDataHead() {
        return dataHead;
    }

    public void setDataHead(int dataHead) {
        this.dataHead = dataHead;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.title);
        dest.writeInt(this.dataHead);
        dest.writeString(this.content);
    }

    protected TeamDetailTimeLineBean(Parcel in) {
        this.itemType = in.readInt();
        this.title = in.readString();
        this.dataHead = in.readInt();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<TeamDetailTimeLineBean> CREATOR = new Parcelable.Creator<TeamDetailTimeLineBean>() {
        @Override
        public TeamDetailTimeLineBean createFromParcel(Parcel source) {
            return new TeamDetailTimeLineBean(source);
        }

        @Override
        public TeamDetailTimeLineBean[] newArray(int size) {
            return new TeamDetailTimeLineBean[size];
        }
    };
}
