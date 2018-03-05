package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * 请求接口
 * Created by chensh on 2017/2/8 0008.
 */

public interface LoginInteractor<T> {
    Subscription loadUsrData(RequestCallBack<T> callBack, String loginName, String password);
}
