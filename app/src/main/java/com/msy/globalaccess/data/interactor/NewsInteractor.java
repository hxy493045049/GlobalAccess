package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import rx.Subscription;

/**
 * Created by hxy on 2016/12/13 0013.
 * <p>
 * description :it's just a demo for request data
 */
@Deprecated
public interface NewsInteractor<T> {
    Subscription loadLocalNews(RequestCallBack<T> callBack);
}
