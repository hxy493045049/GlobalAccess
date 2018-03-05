package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * Created by chensh on 2017/2/10 0010.
 */

public interface SearchInteractor<T> {
    //获取团队类型
    Subscription loadTeamType(RequestCallBack<T> callBack);

    //获取旅行社部门
    Subscription TravelDepApi(RequestCallBack<T> callBack, String travelDepName);

    //获取景区
    Subscription ScenicSearch(RequestCallBack<T> callBack);

    //保险公司
    Subscription InsuranceSearch(RequestCallBack<T> callBack);

    Subscription VehicleSearch(RequestCallBack<T> callBack);

    Subscription HotelSearch(RequestCallBack<T> callBack);

    Subscription TravelAgentSearch(RequestCallBack<T> callBack);
}
