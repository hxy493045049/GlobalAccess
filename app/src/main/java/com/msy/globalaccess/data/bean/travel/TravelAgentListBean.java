package com.msy.globalaccess.data.bean.travel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 旅行社实体类
 * Created by chensh on 2017/5/16 0016.
 */
@Entity
public class TravelAgentListBean implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    /**
     * travelAgentId : ”001”
     * travelAgentName : 中国旅行社
     */

    private String travelAgentId;
    private String travelAgentName;

    @Transient
    private boolean isChecked;
    @Transient
    private String searchText;
    @Transient
    private int searchPosition;

    @Generated(hash = 1699621475)
    public TravelAgentListBean(Long id, String travelAgentId,
            String travelAgentName) {
        this.id = id;
        this.travelAgentId = travelAgentId;
        this.travelAgentName = travelAgentName;
    }

    @Generated(hash = 1944417655)
    public TravelAgentListBean() {
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public int getSearchPosition() {
        return searchPosition;
    }

    public void setSearchPosition(int searchPosition) {
        this.searchPosition = searchPosition;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
