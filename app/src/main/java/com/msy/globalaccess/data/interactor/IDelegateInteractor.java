package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import java.util.HashMap;

import rx.Subscription;

/**
 * Created by pepys on 2017/7/4.
 * description:37.	导游委派查询
 */
@Deprecated
public interface IDelegateInteractor<T> {

    Subscription loadDelegate(RequestCallBack<T> callBack, HashMap param);
}