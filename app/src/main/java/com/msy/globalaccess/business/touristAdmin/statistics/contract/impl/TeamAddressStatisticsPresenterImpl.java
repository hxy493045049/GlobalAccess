package com.msy.globalaccess.business.touristAdmin.statistics.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TeamAddressStatisticsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.AddressStatisticsBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.TeamAddressStatisticsInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;


/**
 * 旅游局团队地区Presenter类
 * Created by chensh on 2017/5/17 0017.
 */
public class TeamAddressStatisticsPresenterImpl extends BasePresenterImpl<TeamAddressStatisticsContract.View> implements
        TeamAddressStatisticsContract.Presenter {
    private IStatisticsInteractor.ITeamAddressStatisticsInteractor interactor;

    @Inject
    TeamAddressStatisticsPresenterImpl(TeamAddressStatisticsInteractorImpl Impl) {
        this.interactor = Impl;
    }

    @Override
    public void requestTCSS() {
        if (mView == null) {
            return;
        }
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.AddressSourceStatisticsApi.appTravelCustomSourceStatistics)
                .setParam(StatisticsApi.AddressSourceStatisticsApi.travelAgentId, mView.getTravelAgentId())
                .setParam(StatisticsApi.AddressSourceStatisticsApi.teamDate, mView.getTeamDate())
                .setParam(StatisticsApi.AddressSourceStatisticsApi.teamStauts, mView.getTeamStauts())
                .setParam(StatisticsApi.AddressSourceStatisticsApi.teamTypeId, mView.getTeamTypeID())
                .setParam(StatisticsApi.AddressSourceStatisticsApi.customSourceType, mView.getCustomSourceType())
                .setParam(StatisticsApi.AddressSourceStatisticsApi.userId, UserHelper.getInstance().getUser()
                        .getUserId())
                .build().getParam();
        Subscription subscription = interactor.requestAddressStatist(new RequestCallBackAdapter<AddressStatisticsBean>
                () {
            @Override
            public void beforeRequest() {
                if (mView != null) {
                    mView.showProgress();
                }
            }

            @Override
            public void success(AddressStatisticsBean data) {
                mView.getTravelCustomSourceStatistics(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
            }

            @Override
            public void after() {
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {

    }
}
