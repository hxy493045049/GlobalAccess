package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.TeamDetailContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.TeamServiceApi;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.ApprovalInteractor;
import com.msy.globalaccess.data.interactor.ITeamInteractor;
import com.msy.globalaccess.data.interactor.impl.ApprovalInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.TeamInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by pepys on 2017/2/10.
 * description:
 */
public class TeamDetailPresenterImpl extends BasePresenterImpl<TeamDetailContract.View> implements TeamDetailContract.Presenter {
    /**
     * 持有model的引用
     */
    private ITeamInteractor teamInteractor;

    private ApprovalInteractor<String> approvalInteractor;

    @Inject
    TeamDetailPresenterImpl(TeamInteractorImpl impl, ApprovalInteractorImpl approvalInteractor) {
        teamInteractor = impl;
        this.approvalInteractor = approvalInteractor;
    }

    @Override
    public void loadTeamDetail() {
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(TeamServiceApi.TeamDetailApi.TEAM_DETAIL)
                .setParam(TeamServiceApi.TeamDetailApi.USER_ID, UserHelper.getInstance().getUser().getUserId())
                .setParam(TeamServiceApi.TeamDetailApi.TEAM_ID, mView.getTeamID())
                .build().getParam();

        Subscription subscription = teamInteractor.getTeamDetail(new RequestCallBack<TeamDetailBean>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(TeamDetailBean data) {
                mView.hideProgress();
                mView.showTeamDetail(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.hideProgress();
                mView.LoadFailure();
            }

            @Override
            public void after() {

            }
        }, param);
        cacheSubscription.add(subscription);
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

            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        }, UserHelper.getInstance().getUser().getUserId(), mView.getTeamID(), mView.getOpType(), mView.getOpStatus(), mView.getRemark());
        cacheSubscription.add(subscription);
    }

    @Override
    public void checkIsChange() {
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(TeamServiceApi.TeamDetailApi.method_appTeamIsChange)
                .setParam(TeamServiceApi.TeamDetailApi.USER_ID, UserHelper.getInstance().getUser().getUserId())
                .setParam(TeamServiceApi.TeamDetailApi.TEAM_ID, mView.getTeamID())
                .build().getParam();

        Subscription subscription = teamInteractor.checkIsChange(new RequestCallBack<NoDataBean>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(NoDataBean data) {
                if(data.getStatus() == ResultCode.SUCCESS){
                    mView.canChange();
                }else{
                    mView.noCanChange(data.getMessage());
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {

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
        //2242
        loadTeamDetail();
    }
}
