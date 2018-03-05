package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/2/21.
 */

public interface INavigationDataInteractor<T> {
    Subscription loadUsrData(RequestCallBack<T> callBack, String startDate, String endDate);
    Subscription loadNavigationData(RequestCallBack<T> callBack, String startDate, String endDate);
}
