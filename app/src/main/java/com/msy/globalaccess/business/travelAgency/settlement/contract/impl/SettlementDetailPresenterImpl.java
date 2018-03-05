package com.msy.globalaccess.business.travelAgency.settlement.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.settlement.contract.ISettlementDetailContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.SettlementApi;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;
import com.msy.globalaccess.data.interactor.ISettlementInteractor;
import com.msy.globalaccess.data.interactor.impl.SettlementInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

import cn.msy.zc.commonutils.StringToMD5;
import rx.Subscription;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description : 结算详情
 */
public class SettlementDetailPresenterImpl extends BasePresenterImpl<ISettlementDetailContract.View> implements
        ISettlementDetailContract.Presenter {

    private ISettlementInteractor mInteractor;

    @Inject
    SettlementDetailPresenterImpl(SettlementInteractorImpl impl) {
        mInteractor = impl;
    }

    @Override
    public void onStart() {
        mView.showProgress();
        loadSettlementDetail();
    }

    @Override
    public void loadSettlementDetail() {
        if (mView == null) {
            return;
        }

        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SettlementApi.SettlementDetailApi.method)
                .setParam(SettlementApi.SettlementDetailApi.teamAuditId, mView.getTeamAuditId())
                .setParam(SettlementApi.SettlementDetailApi.userId, mView.getUserId())
                .build().getParam();

        Subscription subscription = mInteractor.getSettlementDetail(new RequestCallBackAdapter<SettlementDetailBean>
                () {
            @Override
            public void success(SettlementDetailBean data) {
                mView.handlerDetailData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, errorMsg);
            }
        }, param);
        cacheSubscription.add(subscription);
    }

    @Override
    public void settlement() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SettlementApi.Settlement.method)
                .setParam(SettlementApi.Settlement.teamAuditId, mView.getTeamAuditId())
                .setParam(SettlementApi.Settlement.userId, mView.getUserId())
                .setParam(SettlementApi.Settlement.payPassword, StringToMD5.MD5(mView.getPassWord()))
                .build().getParam();


        Subscription subscription = mInteractor.settlement(new RequestCallBackAdapter<NoDataBean>() {

            @Override
            public void beforeRequest() {
                mView.showLoading();
            }

            @Override
            public void success(NoDataBean data) {
                super.success(data);
                mView.handlerSettlementData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onError(resultCode, errorMsg);
            }

            @Override
            public void after() {
                mView.dismissLoading();
            }
        }, param);

        cacheSubscription.add(subscription);
    }

    @Override
    public void cancelRequest() {
        super.cancelRequest();
    }
}
