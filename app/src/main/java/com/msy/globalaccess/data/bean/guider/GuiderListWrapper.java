package com.msy.globalaccess.data.bean.guider;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by shawn on 2017/3/23 0023.
 * <p>
 * description :
 */

public class GuiderListWrapper implements Parcelable {
    public static final Parcelable.Creator<GuiderListWrapper> CREATOR = new Parcelable.Creator<GuiderListWrapper>() {
        @Override
        public GuiderListWrapper createFromParcel(Parcel source) {
            return new GuiderListWrapper(source);
        }

        @Override
        public GuiderListWrapper[] newArray(int size) {
            return new GuiderListWrapper[size];
        }
    };
    private List<GuiderListBean> teamGuideList;

    public GuiderListWrapper() {
    }

    protected GuiderListWrapper(Parcel in) {
        this.teamGuideList = in.createTypedArrayList(GuiderListBean.CREATOR);
    }

    public List<GuiderListBean> getTeamGuideList() {
        return teamGuideList;
    }

    public void setTeamGuideList(List<GuiderListBean> teamGuideList) {
        this.teamGuideList = teamGuideList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.teamGuideList);
    }
}
