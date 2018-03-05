package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.RequestCallBackProgress;

import rx.Subscription;

/**
 * Created by pepys on 2017/5/15.
 * description:
 */
public interface ILoadNewVersionInteractor<T> {
    Subscription loadNewVersion(RequestCallBack<T> callBack);
    int downNewApp(RequestCallBackProgress<T> callBack, String url, String path);
}
