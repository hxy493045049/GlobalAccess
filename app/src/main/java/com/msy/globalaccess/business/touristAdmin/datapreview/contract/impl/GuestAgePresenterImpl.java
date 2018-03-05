package com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.GuestAgeContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.GuestAgeInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by shawn on 2017/7/17 0017.
 * <p>
 * description :
 */

public class GuestAgePresenterImpl extends BasePresenterImpl<GuestAgeContract.View> implements GuestAgeContract
        .Presenter {

    private IStatisticsInteractor.IGuestAgeInteractor interactor;

    @Inject
    public GuestAgePresenterImpl(GuestAgeInteractorImpl impl) {
        interactor = impl;
    }

    @Override
    public void onStart() {
        getGuestAgeStatisticsData();
    }

    @Override
    public void getGuestAgeStatisticsData() {
        Subscription subscription = interactor.getStatisticsData(
                new RequestCallBackAdapter<GuestAgeStatisticsBeanWrapper.ResultBean>() {
                    @Override
                    public void success(GuestAgeStatisticsBeanWrapper.ResultBean data) {
                        super.success(data);
                        mView.handlerGuestAgeData(data);
                    }

                    @Override
                    public void onError(@ResultCode int resultCode, String errorMsg) {
                        super.onError(resultCode, errorMsg);
                        mView.onGuestAgeError(resultCode, errorMsg);
                    }
                },
                buildParam());
        cacheSubscription.add(subscription);
    }

    private HashMap<String, String> buildParam() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.GuestAgeStatisticsApi.method)
                .setParam(StatisticsApi.GuestAgeStatisticsApi.searchDateStart, mView.getTimes()[0])
                .setParam(StatisticsApi.GuestAgeStatisticsApi.searchDateEnd, mView.getTimes()[1])
                .setParam(StatisticsApi.GuestAgeStatisticsApi.userId, App.userHelper.getUser().getUserId())
                .build().getParam();
        return param;
    }
}
