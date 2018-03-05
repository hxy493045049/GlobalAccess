package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.ApprovalContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.ApprovalApi;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.ApprovalInteractor;
import com.msy.globalaccess.data.interactor.impl.ApprovalInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by pepys on 2017/2/16.
 * description:审批Presneter
 */
public class ApprovalPresneterImpl extends BasePresenterImpl<ApprovalContract.View> implements ApprovalContract.Presenter {

    private ApprovalInteractor<String> approvalInteractor;

    @Inject
    public ApprovalPresneterImpl(ApprovalInteractorImpl impl){
        approvalInteractor = impl;
    }

    @Override
    public void loadApproval() {
        Subscription subscription = approvalInteractor.loadApproval(new RequestCallBack<String>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(String data) {
                mView.approvalSuccess(data);

            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.approvalFailure();
            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        },UserHelper.getInstance().getUser().getUserId(),mView.getTeamID(),mView.getOpType(),mView.getOpStatus(),mView.getRemark());
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadApprovalDelegateTourist() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(ApprovalApi.ApprovalDelegateTouristApi.METHOD)
                .setParam(ApprovalApi.USERID,UserHelper.getInstance().getUser().getUserId())
                .setParam(ApprovalApi.ApprovalDelegateTouristApi.TEAMGUIDEID,mView.getTeamGuideID())
                .setParam(ApprovalApi.ApprovalDelegateTouristApi.APPROVALTYPE,mView.getApprovalType())
                .setParam(ApprovalApi.ApprovalDelegateTouristApi.REMARK,mView.getRemark())
                .build().getParam();
        Subscription subscription = approvalInteractor.loadApprovalDelegateTourist(new RequestCallBackAdapter<String>(){
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(String data) {
                mView.approvalDelegateTouristSuccess(data);

            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.approvalDelegateTouristFailure();
            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        },param);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {
    }

}
