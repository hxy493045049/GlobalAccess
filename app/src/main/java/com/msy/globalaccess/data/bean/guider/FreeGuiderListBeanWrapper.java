package com.msy.globalaccess.data.bean.guider;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by shawn on 2017/3/29 0029.
 * <p>
 * description :
 */

public class FreeGuiderListBeanWrapper implements Parcelable {
    public static final Parcelable.Creator<FreeGuiderListBeanWrapper> CREATOR = new Parcelable
            .Creator<FreeGuiderListBeanWrapper>() {
        @Override
        public FreeGuiderListBeanWrapper createFromParcel(Parcel source) {
            return new FreeGuiderListBeanWrapper(source);
        }

        @Override
        public FreeGuiderListBeanWrapper[] newArray(int size) {
            return new FreeGuiderListBeanWrapper[size];
        }
    };
    private ArrayList<FreeGuiderListBean> tourGuideList;

    public FreeGuiderListBeanWrapper() {
    }

    protected FreeGuiderListBeanWrapper(Parcel in) {
        this.tourGuideList = in.createTypedArrayList(FreeGuiderListBean.CREATOR);
    }

    public ArrayList<FreeGuiderListBean> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(ArrayList<FreeGuiderListBean> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.tourGuideList);
    }
}
