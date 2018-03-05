package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/5/16.
 */

public interface ITourismMainInteractor<T> {
    Subscription loadTravelData(RequestCallBack<T> callBack);
    Subscription loadTravelTeamType(RequestCallBack<T> callBack);
}
