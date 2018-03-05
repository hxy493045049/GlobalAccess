package com.msy.globalaccess.business.travelAgency.team.contract.impl;


import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.TravelAttractionsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsParentBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.data.interactor.impl.TravelAttractionsInteractorImpl;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/3/16.
 */

public class TravelAttractionsPresenterImpl extends BasePresenterImpl<TravelAttractionsContract.View> implements
        TravelAttractionsContract.Presenter {

    private TravelAttractionsInteractorImpl travelAttractionsInteractor;

    @Inject
    TravelAttractionsPresenterImpl(TravelAttractionsInteractorImpl travelAttractionsInteractor) {
        this.travelAttractionsInteractor = travelAttractionsInteractor;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadTravelAttractionData(String teamId) {
        mView.showProgress();
        Subscription subscription = travelAttractionsInteractor.loadTravelAttractionsData(new RequestCallBackAdapter<TravelAttractionsParentBean>
                () {
            @Override
            public void success(TravelAttractionsParentBean data) {
                mView.hideProgress();
                mView.getTravelAttractionsData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
                mView.getTravelAttractionsData(null);
            }
        }, teamId);
        cacheSubscription.add(subscription);
    }

    @Override
    public void commitTravelAttractionData(ArrayList<TripScenicBean> tripScenicBeanArrayList, String teamId) {
        mView.showProgress();
        Subscription subscription = travelAttractionsInteractor.commitTravelAttractionsData(new RequestCallBackAdapter<BaseBean>
                () {
            @Override
            public void success(BaseBean baseBean) {
                mView.hideProgress();
                mView.getCommitDataStatus(ResultCode.SUCCESS);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
                mView.getCommitDataStatus(resultCode);
            }
        }, tripScenicBeanArrayList,teamId);
        cacheSubscription.add(subscription);
    }

    @Override
    public void queryTripScenicIsOrder(String tripScenicId, final int operationType) {
        mView.showProgress();
        Subscription subscription = travelAttractionsInteractor.queryTripScenicIsOrder(new RequestCallBackAdapter<BaseBean>
                () {
            @Override
            public void success(BaseBean baseBean) {
                mView.hideProgress();
                mView.getQueryIsOrderStatus(ResultCode.SUCCESS,operationType);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
                mView.getQueryIsOrderStatus(resultCode,operationType);
            }
        }, tripScenicId,operationType);
        cacheSubscription.add(subscription);
    }
}
