package com.msy.globalaccess.data.bean.guider;


import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;

import java.util.ArrayList;

/**
  * Created by pepys on 2017/7/4
  * description: 导游委派列表
  *
  */
public class DelegateBean {
    /**
     *  总条数
     */
    private String totalNum;
    /**
     * 处理中条数
     */
    private String inProcessNum;
    /**
     *  已通过条数
     */
    private String passNum;
    /**
     *  未通过条数
     */
    private String unPassNum;
    /**
     * 具体导游信息
     */
    private ArrayList<TouristDelegateBean> tourGuideList;

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getInProcessNum() {
        return inProcessNum;
    }

    public void setInProcessNum(String inProcessNum) {
        this.inProcessNum = inProcessNum;
    }

    public String getPassNum() {
        return passNum;
    }

    public void setPassNum(String passNum) {
        this.passNum = passNum;
    }

    public String getUnPassNum() {
        return unPassNum;
    }

    public void setUnPassNum(String unPassNum) {
        this.unPassNum = unPassNum;
    }

    public ArrayList<TouristDelegateBean> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(ArrayList<TouristDelegateBean> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }
}
