package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

/**
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description :景区客流分析
 */

public class ScenicOrderCustomCountListBean {
    /**
     * scenicOrderCustomCountBillList : [{"date":"2017-04-01","countNum":"1455"},{"date":"2017-04-02",
     * "countNum":"1889"},{"date":"2017-04-03","countNum":"2251"},{"date":"2017-04-04","countNum":"1045"},
     * {"date":"2017-04-05","countNum":"941"}]
     * name : 武陵源核心景区（森林公园门票站）
     */

    private String name;
    private List<ScenicOrderCustomCountBillListBean> scenicOrderCustomCountBillList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScenicOrderCustomCountBillListBean> getScenicOrderCustomCountBillList() {
        return scenicOrderCustomCountBillList;
    }

    public void setScenicOrderCustomCountBillList(List<ScenicOrderCustomCountBillListBean>
                                                          scenicOrderCustomCountBillList) {
        this.scenicOrderCustomCountBillList = scenicOrderCustomCountBillList;
    }

    public static class ScenicOrderCustomCountBillListBean {
        /**
         * date : 2017-04-01
         * countNum : 1455
         */

        private String date;
        private String countNum;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCountNum() {
            return countNum;
        }

        public void setCountNum(String countNum) {
            this.countNum = countNum;
        }
    }
}
