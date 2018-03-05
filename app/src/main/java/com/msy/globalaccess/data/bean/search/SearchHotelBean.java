package com.msy.globalaccess.data.bean.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chensh on 2017/2/14 0014.
 */

public class SearchHotelBean {


    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":9,"hotelList":[{"hotelName":"芜湖银湖生态酒店","hotelId":"109"},{"hotelName":"芜湖国际大酒店","hotelId":"110"},{"hotelName":"芜湖商务酒店","hotelId":"142"},{"hotelName":"7天快捷酒店","hotelId":"164"},{"hotelName":"桔子酒店","hotelId":"201"},{"hotelName":"ADS","hotelId":"261"},{"hotelName":"SDFA","hotelId":"262"},{"hotelName":"FDSAQ","hotelId":"263"},{"hotelName":"CXSA","hotelId":"264"}]}
     */
    /**
     * totalNum : 9
     * hotelList : [{"hotelName":"芜湖银湖生态酒店","hotelId":"109"},{"hotelName":"芜湖国际大酒店","hotelId":"110"},{"hotelName":"芜湖商务酒店","hotelId":"142"},{"hotelName":"7天快捷酒店","hotelId":"164"},{"hotelName":"桔子酒店","hotelId":"201"},{"hotelName":"ADS","hotelId":"261"},{"hotelName":"SDFA","hotelId":"262"},{"hotelName":"FDSAQ","hotelId":"263"},{"hotelName":"CXSA","hotelId":"264"}]
     */

    private int totalNum;
    private ArrayList<HotelListBean> hotelList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<HotelListBean> getHotelList() {
        return hotelList;
    }

    public void setHotelList(ArrayList<HotelListBean> hotelList) {
        this.hotelList = hotelList;
    }

    public static class HotelListBean {
        /**
         * hotelName : 芜湖银湖生态酒店
         * hotelId : 109
         */

        private String hotelName;
        private String hotelId;

        public String getHotelName() {
            return hotelName;
        }

        public void setHotelName(String hotelName) {
            this.hotelName = hotelName;
        }

        public String getHotelId() {
            return hotelId;
        }

        public void setHotelId(String hotelId) {
            this.hotelId = hotelId;
        }
    }
}
