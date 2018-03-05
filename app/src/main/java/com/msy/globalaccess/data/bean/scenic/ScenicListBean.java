package com.msy.globalaccess.data.bean.scenic;

import com.msy.globalaccess.widget.dialog.WheelViewDialog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 景区数据
 * Created by chensh on 2017/5/16 0016.
 */
@Entity
public class ScenicListBean implements Serializable,WheelViewDialog.IWheelKey {

    private static final long serialVersionUID = 2L;
    /**
     * scenicId : 121
     * scenicName : 核心景区大门
     */
    @Id(autoincrement = true)
    private Long id;
    private String scenicId;
    private String scenicName;
    private String isOrderTicket;
    private String pscenicId;
    private String isAcc;

    @Transient
    private boolean isChecked;

    @Generated(hash = 507372349)
    public ScenicListBean(Long id, String scenicId, String scenicName,
            String isOrderTicket, String pscenicId, String isAcc) {
        this.id = id;
        this.scenicId = scenicId;
        this.scenicName = scenicName;
        this.isOrderTicket = isOrderTicket;
        this.pscenicId = pscenicId;
        this.isAcc = isAcc;
    }

    @Generated(hash = 532237999)
    public ScenicListBean() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getScenicId() {
        return scenicId;
    }

    public void setScenicId(String scenicId) {
        this.scenicId = scenicId;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getIsOrderTicket() {
        return isOrderTicket;
    }

    public void setIsOrderTicket(String isOrderTicket) {
        this.isOrderTicket = isOrderTicket;
    }

    public String getPscenicId() {
        return pscenicId;
    }

    public void setPscenicId(String pscenicId) {
        this.pscenicId = pscenicId;
    }

    public String getIsAcc() {
        return isAcc;
    }

    public void setIsAcc(String isAcc) {
        this.isAcc = isAcc;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getKey() {
        return scenicName;
    }
}
