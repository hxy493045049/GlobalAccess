package com.msy.globalaccess.data.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shawn on 2017/3/28 0028.
 * <p>
 * description :  景点级别或者语种级别
 */
public class KeyMapBean implements Parcelable {

    public static final Parcelable.Creator<KeyMapBean> CREATOR = new Parcelable.Creator<KeyMapBean>() {
        @Override
        public KeyMapBean createFromParcel(Parcel source) {
            return new KeyMapBean(source);
        }

        @Override
        public KeyMapBean[] newArray(int size) {
            return new KeyMapBean[size];
        }
    };

    /**
     * entityPage :
     * mapDesc : 导游语种
     * mapFilter :
     * mapId : 85
     * mapKey : 6
     * mapType : LANGUAGE
     * mapValue : 日语
     * sys : 0
     */
    private String entityPage;
    private String mapDesc;
    private String mapFilter;
    private String mapId;
    private String mapKey;
    private String mapType;
    private String mapValue;
    private String sys;

    public KeyMapBean() {
    }

    protected KeyMapBean(Parcel in) {
        this.entityPage = in.readString();
        this.mapDesc = in.readString();
        this.mapFilter = in.readString();
        this.mapId = in.readString();
        this.mapKey = in.readString();
        this.mapType = in.readString();
        this.mapValue = in.readString();
        this.sys = in.readString();
    }

    public String getEntityPage() {
        return entityPage;
    }

    public void setEntityPage(String entityPage) {
        this.entityPage = entityPage;
    }

    public String getMapDesc() {
        return mapDesc;
    }

    public void setMapDesc(String mapDesc) {
        this.mapDesc = mapDesc;
    }

    public String getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(String mapFilter) {
        this.mapFilter = mapFilter;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public String getMapValue() {
        return mapValue;
    }

    public void setMapValue(String mapValue) {
        this.mapValue = mapValue;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.entityPage);
        dest.writeString(this.mapDesc);
        dest.writeString(this.mapFilter);
        dest.writeString(this.mapId);
        dest.writeString(this.mapKey);
        dest.writeString(this.mapType);
        dest.writeString(this.mapValue);
        dest.writeString(this.sys);
    }
}
