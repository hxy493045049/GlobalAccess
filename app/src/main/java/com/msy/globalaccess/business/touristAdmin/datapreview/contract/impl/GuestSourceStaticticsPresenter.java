package com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.GuestSourceStaticticsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.GuestSourceInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 客源地
 */

public class GuestSourceStaticticsPresenter extends BasePresenterImpl<GuestSourceStaticticsContract.View> implements
        GuestSourceStaticticsContract.Presenter {
    private IStatisticsInteractor.IGuestSourceStaticticsInteractor mInteractor;

    @Inject
    public GuestSourceStaticticsPresenter(GuestSourceInteractorImpl interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart() {
        loadData();
    }

    @Override
    public void loadData() {
        mInteractor.getStaticticsData(new RequestCallBackAdapter<GuestSourceBeanWrapper.ResultBean>() {
            @Override
            public void success(GuestSourceBeanWrapper.ResultBean data) {
                super.success(data);
                mView.handlerGuestSourceData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onGuestSourceError(errorMsg, resultCode);
            }
        }, buildParam());
    }

    private HashMap<String, String> buildParam() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.GuestSourceStatisticsApi.method)
                .setParam(StatisticsApi.GuestSourceStatisticsApi.userId, App.userHelper.getUser().getUserId())
                .setParam(StatisticsApi.GuestSourceStatisticsApi.sourceType, mView.getSourceType())
                .setParam(StatisticsApi.GuestSourceStatisticsApi.searchDateStart, mView.getStartTime())
                .setParam(StatisticsApi.GuestSourceStatisticsApi.searchDateEnd, mView.getEndTime())
                .build().getParam();
        return param;
    }

}
