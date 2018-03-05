package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.ScenicSpotContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;
import com.msy.globalaccess.data.interactor.impl.ScenicSpotInteractorImpl;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;

/**
 * 景点相关Impl
 * Created by chensh on 2017/3/17 0017.
 */

public class ScenicSpotPrsenterImpl extends BasePresenterImpl<ScenicSpotContract.View> implements ScenicSpotContract
        .Presenter {

    private ScenicSpotInteractorImpl scenicSpotInteractor;

    @Inject
    ScenicSpotPrsenterImpl(ScenicSpotInteractorImpl scenicSpotInteractor) {
        this.scenicSpotInteractor = scenicSpotInteractor;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void submit(String tripDate, String scenicId) {
        Subscription subscription = scenicSpotInteractor.submitScenic(new RequestCallBackAdapter<ArrayList<TripScenicTicketBean>>
                () {
            @Override
            public void beforeRequest() {
                if (mView != null) {
                    mView.showProgress();
                }
            }

            @Override
            public void success(ArrayList<TripScenicTicketBean> data) {
                mView.getTicketInfo(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                mView.getTicketInfo(null);
            }

            @Override
            public void after() {
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, tripDate, scenicId);
        cacheSubscription.add(subscription);
    }
}
