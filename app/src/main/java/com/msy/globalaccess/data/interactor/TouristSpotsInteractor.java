package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * Created by pepys on 2017/3/21.
 * description:
 */
public interface TouristSpotsInteractor<T> {
    Subscription getSpotList(RequestCallBack<T> callBack,int currentPageNum,int showNum,String userID,int isAcc,String scenicName,String cityName,String scenicLevel,String isAlaway,boolean isSave);
}
