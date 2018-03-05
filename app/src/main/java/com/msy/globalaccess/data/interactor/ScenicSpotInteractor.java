package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * 景点相关
 * Created by chensh on 2017/3/20 0020.
 */

public interface ScenicSpotInteractor<T> {
    //提交景点相关
    Subscription submitScenic(RequestCallBack<T> callBack, String tripDate, String scenicId);
}
