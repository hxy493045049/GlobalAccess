package com.msy.globalaccess.data.bean.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 保险公司实体类
 * Created by chensh on 2017/2/14 0014.
 */

public class SearchInsuranceBean {


    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":3,"insuranceList":[{"insuranceId":"3","insuranceName":"中国人保"},{"insuranceId":"4","insuranceName":"中国人寿"},{"insuranceId":"41","insuranceName":"太平洋保险"}]}
     */
    /**
     * totalNum : 3
     * insuranceList : [{"insuranceId":"3","insuranceName":"中国人保"},{"insuranceId":"4","insuranceName":"中国人寿"},{"insuranceId":"41","insuranceName":"太平洋保险"}]
     */

    private int totalNum;
    private ArrayList<InsuranceListBean> insuranceList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<InsuranceListBean> getInsuranceList() {
        return insuranceList;
    }

    public void setInsuranceList(ArrayList<InsuranceListBean> insuranceList) {
        this.insuranceList = insuranceList;
    }

    public static class InsuranceListBean {
        /**
         * insuranceId : 3
         * insuranceName : 中国人保
         */

        private String insuranceId;
        private String insuranceName;

        public String getInsuranceId() {
            return insuranceId;
        }

        public void setInsuranceId(String insuranceId) {
            this.insuranceId = insuranceId;
        }

        public String getInsuranceName() {
            return insuranceName;
        }

        public void setInsuranceName(String insuranceName) {
            this.insuranceName = insuranceName;
        }
    }
}
