package com.msy.globalaccess.business.travelAgency.team.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;

import java.util.ArrayList;

/**
 * 景点相关契约类
 * Created by chensh on 2017/3/17 0017.
 */

public interface ScenicSpotContract extends BaseContract {
    interface View extends BaseContract.View {
        /**
         * 新增景点时 获取景点票务信息
         *
         * @param data 数据·
         */
        void getTicketInfo(ArrayList<TripScenicTicketBean> data);
    }


    interface Presenter extends BaseContract.Presenter {
        void submit(String tripDate, String scenicId);
    }
}
