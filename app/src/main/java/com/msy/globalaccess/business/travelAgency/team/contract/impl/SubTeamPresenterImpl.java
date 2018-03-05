package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.ISubTeamContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.config.DataSetting;
import com.msy.globalaccess.data.api.TeamServiceApi;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.interactor.ITeamInteractor;
import com.msy.globalaccess.data.interactor.impl.TeamInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description : 团队管理的子fragment对应的presenter
 */

public class SubTeamPresenterImpl extends BasePresenterImpl<ISubTeamContract.View>
        implements ISubTeamContract.Presenter {
    private ITeamInteractor mInteractor;

    @Inject
    SubTeamPresenterImpl(TeamInteractorImpl interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart() {
        loadLatestData();
    }


    @Override
    public void loadLatestData() {
        Subscription subscription = mInteractor.getDataByPage(new MyTeamListCallBack(mView, RequestType.TYPE_LATEST),
                buildParams());
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadMore() {
        Subscription subscription = mInteractor.getDataByPage(
                new MyTeamListCallBack(mView, RequestType.TYPE_LOAD_MORE), buildParams());
        cacheSubscription.add(subscription);
    }

    private HashMap<String, String> buildParams() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(TeamServiceApi.TeamListApi.method)
                .setParam(TeamServiceApi.TeamListApi.currentPageNum, mView.getPage())
                .setParam(TeamServiceApi.TeamListApi.showNum, LIST_DATA_SIZE)
                .setParam(TeamServiceApi.TeamListApi.userId, App.userHelper.getUser().getUserId())
                //可选参数
                .setParam(TeamServiceApi.TeamListApi.optionSearchType, mView.getDataType())
                .setParam(TeamServiceApi.TeamListApi.optionTeamCode, "")
                .setParam(TeamServiceApi.TeamListApi.optionTeamStatus, "")
                .setParam(TeamServiceApi.TeamListApi.optionTravelAgentId, "")
                .setParam(TeamServiceApi.TeamListApi.optionTravelDepId, "")
                .setParam(TeamServiceApi.TeamListApi.optionTeamStartDate, "")
                .setParam(TeamServiceApi.TeamListApi.optionTeamEndDate, "")
                .setParam(TeamServiceApi.TeamListApi.optionTeamTypeId, "")
                .build().getParam();
        return param;
    }

    private class MyTeamListCallBack extends RequestCallBackAdapter<List<TeamListBean>> {

        @RequestType
        private int reqType;

        private MyTeamListCallBack(BaseContract.View view, @RequestType int reqType) {
            super();
            this.reqType = reqType;
        }

        @Override
        public void success(List<TeamListBean> data) {
            if (data.size() == DataSetting.LIST_DATA_SIZE) {
                mView.handlerListData(ResultCode.SUCCESS, reqType, data);
            } else {
                mView.handlerListData(ResultCode.SUCCESS_NO_MORE_DATA, reqType, data);
            }
        }

        @Override
        public void onError(@ResultCode int resultCode, String errorMsg) {
            mView.onError(resultCode, reqType, errorMsg);
        }
    }

}
