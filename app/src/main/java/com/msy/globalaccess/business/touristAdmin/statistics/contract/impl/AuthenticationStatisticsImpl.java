package com.msy.globalaccess.business.touristAdmin.statistics.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TeamAuthenticationStaticsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.AuthenticstionStatisticsInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * 获取旅游局团队认证统计数据
 * Created by chensh on 2017/5/16 0016.
 */
public class AuthenticationStatisticsImpl extends BasePresenterImpl<TeamAuthenticationStaticsContract.View>
        implements TeamAuthenticationStaticsContract.Presenter {

    private IStatisticsInteractor.IAuthenticstionStatisticsInteractor Impl;

    @Inject
    AuthenticationStatisticsImpl(AuthenticstionStatisticsInteractorImpl Impl) {
        this.Impl = Impl;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void requestTTCS() {
        if (mView == null) {
            return;
        }
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.AuthenticationStatisticsApi.appTravelTeamCheckStatistics)
                .setParam(StatisticsApi.AuthenticationStatisticsApi.searchType, mView.getSearchType())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.travelAgentId, mView.getTravelAgentId())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.scenicId, mView.getScenicId())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.teamCheckStartDate, mView.getTeamCheckStartDate()
                        .trim())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.teamCheckEndDate, mView.getTeamCheckEndDate()
                        .trim())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.teamTypeId, mView.getTeamTypeId())
                .setParam(StatisticsApi.AuthenticationStatisticsApi.userId, UserHelper.getInstance().getUser()
                        .getUserId())
                .build().getParam();
        Subscription subscription = Impl.requestAuthenticsStatist(new RequestCallBackAdapter<TravelTeamStatisticsBean>
                () {
            @Override
            public void beforeRequest() {
                if (mView != null) {
                    mView.showProgress();
                }
            }

            @Override
            public void success(TravelTeamStatisticsBean data) {
                mView.getTravelTeamCheckStatistics(data);
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
}
