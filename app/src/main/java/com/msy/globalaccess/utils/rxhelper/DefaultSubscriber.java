package com.msy.globalaccess.utils.rxhelper;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.exception.RxException;
import com.msy.globalaccess.utils.ErrorUtils;
import com.orhanobut.logger.Logger;

import rx.Subscriber;

/**
 * Created by hxy on 2017/2/13.
 * class description: 默认的订阅者
 */
public class DefaultSubscriber<T> extends Subscriber<T> {
    private RequestCallBack<T> mCallBack;

    private String errorMsg = App.getResourceString(R.string.internet_error);

    public DefaultSubscriber(RequestCallBack<T> callBack) {
        mCallBack = callBack;
    }

    public DefaultSubscriber(RequestCallBack<T> callBack, int resId) {
        this(callBack, App.getResourceString(resId));
    }

    public DefaultSubscriber(RequestCallBack<T> callBack, String errorMessage) {
        mCallBack = callBack;
        errorMsg = errorMessage;
    }

    @Override
    public void onCompleted() {
        try {
            Logger.i("rx completed");
            mCallBack.after();
        } catch (Exception ex) {
            Logger.e(ex, "");
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            Logger.e(e, "rx error");
            if (e instanceof RxException) {
                RxException rxException = (RxException) e;
                mCallBack.onError(rxException.getResultCode(), rxException.getMessage());
            } else {
                mCallBack.onError(ErrorUtils.getExceptionCode(e), ErrorUtils.getExceptionMessage(e, errorMsg));
            }
            mCallBack.after();
        } catch (Exception ex) {
            Logger.e(ex, "");
        }
    }

    @Override
    public void onNext(T t) {
        try {
            Logger.i("rx onNext");
            mCallBack.success(t);
        } catch (Exception e) {
            Logger.e(e, "");
        }
    }
}
