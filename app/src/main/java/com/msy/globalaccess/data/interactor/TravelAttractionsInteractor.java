package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;

import java.util.ArrayList;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/3/21.
 */

public interface TravelAttractionsInteractor<T> {

    /**
     * 查询行程景点
     * @param callBack
     * @param teamTripId
     * @return
     */
    Subscription loadTravelAttractionsData(RequestCallBack<T> callBack, String teamTripId);


    /**
     * 提交行程景点变更
     * @param callBack
     * @param tripScenicBeanArrayList
     * @return
     */
    Subscription commitTravelAttractionsData(RequestCallBack<T> callBack, ArrayList<TripScenicBean> tripScenicBeanArrayList, String teamId);


    /**
     *
     * 查询行程景点是否被预约
     * @param callBack
     * @param tripScenicId
     * @return
     */
    Subscription queryTripScenicIsOrder(RequestCallBack<T> callBack,String tripScenicId,int operationType);
}
