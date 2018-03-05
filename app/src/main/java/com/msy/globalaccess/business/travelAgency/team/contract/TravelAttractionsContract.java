package com.msy.globalaccess.business.travelAgency.team.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsParentBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;

import java.util.ArrayList;

/**
 * Created by WuDebin on 2017/3/16.
 */

public interface TravelAttractionsContract {

    interface View extends BaseContract.View {

        void getTravelAttractionsData(TravelAttractionsParentBean travelAttractionsParentBean);

        void getCommitDataStatus(int status);

        void getQueryIsOrderStatus(int status,int operationType);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 查询行程景点列表
         * @param teamId
         */
        void loadTravelAttractionData(String teamId);

        /**
         * 提交景点变更
         * @param tripScenicBeanArrayList
         */
        void commitTravelAttractionData(ArrayList<TripScenicBean> tripScenicBeanArrayList, String teamId);

        /**
         * 查询行程景点是否被预约
         * @param tripScenicId
         */
        void queryTripScenicIsOrder(String tripScenicId,int operationType);

    }

}
