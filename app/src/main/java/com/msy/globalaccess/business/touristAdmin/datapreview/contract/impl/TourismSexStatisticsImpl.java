package com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.TourismSexStatisticsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsListBean;
import com.msy.globalaccess.data.interactor.impl.TouristSexInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.HashMap;

import javax.inject.Inject;


/**
 * Created by pepys on 2017/7/11
 * description: 游客性别统计
 *
 */
public class TourismSexStatisticsImpl extends BasePresenterImpl<TourismSexStatisticsContract.View> implements TourismSexStatisticsContract.Presenter {


    private TouristSexInteractorImpl touristSexInteractor;

    @Inject
    public TourismSexStatisticsImpl(TouristSexInteractorImpl touristSexInteractor){
            this.touristSexInteractor = touristSexInteractor;
    }

    @Override
    public void onStart() {
        loadSexStatistics();
    }

    @Override
    public void loadSexStatistics() {
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.TouristSexStatisticsApi.method)
                .setParam(StatisticsApi.TouristSexStatisticsApi.userId, App.userHelper.getUser().getUserId())
                .setParam(StatisticsApi.TouristSexStatisticsApi.searchDateStart, mView.getTimes()[0])
                .setParam(StatisticsApi.TouristSexStatisticsApi.searchDateEnd, mView.getTimes()[1])
                .build().getParam();
        touristSexInteractor.loadTouristSex(new RequestCallBackAdapter<TouristSexStatisticsListBean>(){
            @Override
            public void beforeRequest() {
                if (mView != null) {
                    mView.showProgress();
                }
            }

            @Override
            public void success(TouristSexStatisticsListBean data) {
                mView.loadSexStatisticsSuccess(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                mView.loadFailer();
            }

            @Override
            public void after() {
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        },params);
    }

}
