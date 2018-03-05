package com.msy.globalaccess.data.bean.scenic;

import java.util.List;

/**
 * 新增景点票务
 * Created by chensh on 2017/3/30 0030.
 */

public class TripScenicListBean {
    private List<TripSceniclistBean> tripSceniclist;

    public List<TripSceniclistBean> getTripSceniclist() {
        return tripSceniclist;
    }

    public void setTripSceniclist(List<TripSceniclistBean> tripSceniclist) {
        this.tripSceniclist = tripSceniclist;
    }

    public static class TripSceniclistBean {
        /**
         * price : 95
         * ticketType : 0
         * ticketPriceName : 全票（旺季）
         * scenicTicketTypeId : 1321
         */

        private String price;
        private String ticketType;
        private String ticketPriceName;
        private String scenicTicketTypeId;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTicketType() {
            return ticketType;
        }

        public void setTicketType(String ticketType) {
            this.ticketType = ticketType;
        }

        public String getTicketPriceName() {
            return ticketPriceName;
        }

        public void setTicketPriceName(String ticketPriceName) {
            this.ticketPriceName = ticketPriceName;
        }

        public String getScenicTicketTypeId() {
            return scenicTicketTypeId;
        }

        public void setScenicTicketTypeId(String scenicTicketTypeId) {
            this.scenicTicketTypeId = scenicTicketTypeId;
        }
    }
}
