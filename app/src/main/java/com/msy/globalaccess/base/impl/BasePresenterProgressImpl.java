package com.msy.globalaccess.base.impl;

import android.support.annotation.NonNull;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.utils.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;

import rx.Subscription;

/**
 * Created by hxy on 2016/12/12 0012.
 * <p>
 * description :
 */

public abstract class BasePresenterProgressImpl<T extends BaseContract.View> implements BaseContract.Presenter {
    protected T mView;
    protected List<Subscription> cacheSubscription = new LinkedList<>();

    @Override
    public void attachView(@NonNull BaseContract.View view) {
        mView = (T) view;
    }

    @Override
    public void onDestroy() {
        cancelRequest();
        mView = null;
    }

    @Override
    public void cancelRequest() {
        for (Subscription subscription : cacheSubscription) {
            RxJavaUtils.cancelSubscription(subscription);
        }
        cacheSubscription = null;
    }

    public class RequestCallBackAdapter<K> implements RequestCallBack<K> {

        @Override
        public void beforeRequest() {
            if (mView != null) {
                mView.showProgress();
            }
        }

        public void progress(int soFarBytes, int totalBytes,int speed){

        }

        @Override
        public void success(K data) {
        }

        @Override
        public void onError(@ResultCode int resultCode, String errorMsg) {
            Logger.e("请求失败,ResultCode: " + resultCode + ", errorMsg: " + errorMsg);
        }

        @Override
        public void after() {
            if (mView != null) {
                mView.hideProgress();
            }
        }
    }

}
