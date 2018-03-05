package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.google.gson.Gson;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.IGuiderListContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.config.DataSetting;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.interactor.IGuiderInteractor;
import com.msy.globalaccess.data.interactor.impl.GuiderInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by hxy on 2017/3/15 0015.
 * <p>
 * description :
 */

public class GuiderListPresenterImpl extends BasePresenterImpl<IGuiderListContract.View> implements
        IGuiderListContract.Presenter {

    IGuiderInteractor mInteractor;

    @Inject
    public GuiderListPresenterImpl(GuiderInteractorImpl interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart() {
        loadGuiderList();
    }

    @Override
    public void loadGuiderList() {
        HashMap<String, String> param = new ParamsHelper.Builder().setMethod(GuiderApi.QueryTeamGuiderApi.method)
                .setParam(GuiderApi.QueryTeamGuiderApi.teamInfoId, mView.getTeamInfoId())
                .setParam(GuiderApi.QueryTeamGuiderApi.userId, App.userHelper.getUser().getUserId())
                .setParam(GuiderApi.QueryTeamGuiderApi.delFlag, "0")
                .build().getParam();

        Subscription subscription = mInteractor.getTeamGuiderList(new RequestCallBackAdapter<List<GuiderListBean>>
                () {
            @Override
            public void success(List<GuiderListBean> data) {
                //返回的data必定不为空且大于0
                if (data.size() == DataSetting.LIST_DATA_SIZE) {
                    mView.handleData(ResultCode.SUCCESS, data);
                } else if (data.size() > 0) {
                    mView.handleData(ResultCode.SUCCESS_NO_MORE_DATA, data);
                } else {
                    mView.handleData(ResultCode.SUCCESS_EMPTY, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, errorMsg);
            }
        }, param);

        cacheSubscription.add(subscription);
    }

    @Override
    public void modifyGuider() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(GuiderApi.ModifyGuiderApi.method)
                .setParam(GuiderApi.ModifyGuiderApi.userId, App.userHelper.getUser().getUserId())
                .setParam(GuiderApi.ModifyGuiderApi.teamGuideList, new Gson().toJson(mView
                        .getTeamGuideList()))
                //                .setParam(GuiderApi.ModifyGuiderApi.teamGuideList, "[{\'isOur\': \'1\'}]")
                .build().getParam();
        Subscription subscription = mInteractor.modifyGuider(new RequestCallBackAdapter<NoDataBean>() {
            @Override
            public void beforeRequest() {
                mView.showCommitLoading();
            }

            @Override
            public void success(NoDataBean data) {
                mView.modifyGuiderSuccess();
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, errorMsg);
            }

            @Override
            public void after() {
                mView.hideCommitLoading();
            }
        }, param);
        cacheSubscription.add(subscription);
    }

    @Override
    public void whetherCanDeleteGuider() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(GuiderApi.QueryGuiderAttrApi.method)
                .setParam(GuiderApi.QueryGuiderAttrApi.userId, App.userHelper.getUser().getUserId())
                .setParam(GuiderApi.QueryGuiderAttrApi.teamGuideList, new Gson().toJson(mView.getUncheckGuideList()))
                .build().getParam();
        Subscription subscription = mInteractor.whetherCanDeleteGuider(new RequestCallBackAdapter<NoDataBean>() {
            @Override
            public void beforeRequest() {
                mView.showCommitLoading();
            }

            @Override
            public void success(NoDataBean data) {
                mView.canDelete();
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, errorMsg);
            }

            @Override
            public void after() {
                mView.hideCommitLoading();
            }
        }, param);
        cacheSubscription.add(subscription);

    }
}
