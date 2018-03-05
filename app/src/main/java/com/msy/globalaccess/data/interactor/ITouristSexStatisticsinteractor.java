package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import java.util.HashMap;

import rx.Subscription;



/**
 * Created by pepys on 2017/7/10
 * description:游客性别统计
 *
 */
public interface ITouristSexStatisticsinteractor<T> {

    Subscription loadTouristSex(RequestCallBack<T> callBack, HashMap<String,String> params);
}
