package com.msy.globalaccess.business.navigation.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.navigation.contract.NavigationContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean_Old;
import com.msy.globalaccess.data.interactor.impl.NavigationDataInteractorImpl;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/2/7.
 */

public class NavigationPresenterImpl extends BasePresenterImpl<NavigationContract.View> implements NavigationContract.Presenter {

    private NavigationDataInteractorImpl guideDataInteractor;

    @Inject
    public NavigationPresenterImpl(NavigationDataInteractorImpl guideDataInteractor) {
        this.guideDataInteractor = guideDataInteractor;
    }

    @Override
    public void onStart() {
        loadNavigationData();
    }

    @Override
    public void loadNavigationData() {
        mView.showProgress();
        Subscription subscription = guideDataInteractor.loadNavigationData(new RequestCallBackAdapter<NavigationDataBean>(){
            @Override
            public void success(NavigationDataBean data) {
                mView.hideProgress();
                mView.LoadNavigationSuccess(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
            }
        },mView.getStartDate(),mView.getEndDate());
        cacheSubscription.add(subscription);
    }

    /**
     * 初始版本  首页数据加载
     */
    @Override
    @Deprecated
    public void loadGuideData(String startDate, String endDate) {
        Subscription subscription = guideDataInteractor.loadUsrData(new RequestCallBackAdapter<NavigationDataBean_Old>
                () {
            @Override
            public void success(NavigationDataBean_Old data) {
                mView.hideProgress();
                mView.getGuideData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
            }
        }, startDate, endDate);
        cacheSubscription.add(subscription);
    }



}
