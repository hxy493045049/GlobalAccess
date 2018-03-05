package com.msy.globalaccess.data.bean.search;

import java.util.ArrayList;
import java.util.List;

/**
 * 车队查询实体类
 * Created by chensh on 2017/2/14 0014.
 */

public class SearchVehicleBean {


    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":11,"vehicleList":[{"vehicleId":"72","vehicleName":"安徽旅游公交有限公司"},{"vehicleId":"73","vehicleName":"张家界旅游客运有限公司"},{"vehicleId":"147","vehicleName":"运输公司bj"},{"vehicleId":"245","vehicleName":"安庆大巴一公司"},{"vehicleId":"265","vehicleName":"441qwwqe"},{"vehicleId":"266","vehicleName":"SWQ"},{"vehicleId":"268","vehicleName":"DEYP"},{"vehicleId":"269","vehicleName":"uio"},{"vehicleId":"270","vehicleName":"TREQ"},{"vehicleId":"271","vehicleName":"ACD"},{"vehicleId":"272","vehicleName":"SLO"}]}
     */
    /**
     * totalNum : 11
     * vehicleList : [{"vehicleId":"72","vehicleName":"安徽旅游公交有限公司"},{"vehicleId":"73","vehicleName":"张家界旅游客运有限公司"},{"vehicleId":"147","vehicleName":"运输公司bj"},{"vehicleId":"245","vehicleName":"安庆大巴一公司"},{"vehicleId":"265","vehicleName":"441qwwqe"},{"vehicleId":"266","vehicleName":"SWQ"},{"vehicleId":"268","vehicleName":"DEYP"},{"vehicleId":"269","vehicleName":"uio"},{"vehicleId":"270","vehicleName":"TREQ"},{"vehicleId":"271","vehicleName":"ACD"},{"vehicleId":"272","vehicleName":"SLO"}]
     */

    private int totalNum;
    private ArrayList<VehicleListBean> vehicleList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<VehicleListBean> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<VehicleListBean> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public static class VehicleListBean {
        /**
         * vehicleId : 72
         * vehicleName : 安徽旅游公交有限公司
         */

        private String vehicleId;
        private String vehicleName;

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getVehicleName() {
            return vehicleName;
        }

        public void setVehicleName(String vehicleName) {
            this.vehicleName = vehicleName;
        }
    }
}
