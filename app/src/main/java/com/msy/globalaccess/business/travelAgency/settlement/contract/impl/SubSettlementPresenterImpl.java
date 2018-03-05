package com.msy.globalaccess.business.travelAgency.settlement.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.settlement.contract.ISubSettlementContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.config.DataSetting;
import com.msy.globalaccess.data.api.SettlementApi;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.interactor.ISettlementInteractor;
import com.msy.globalaccess.data.interactor.impl.SettlementInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description : 结算管理的子fragment对应的presenter
 */
public class SubSettlementPresenterImpl extends BasePresenterImpl<ISubSettlementContract.View>
        implements ISubSettlementContract.Presenter {

    private ISettlementInteractor mInteractor;

    @Inject
    SubSettlementPresenterImpl(SettlementInteractorImpl impl) {
        mInteractor = impl;
    }

    @Override
    public void loadLatestData() {

        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SettlementApi.SettlementListApi.method)
                .setParam(SettlementApi.SettlementListApi.currentPageNum, mView.getPage())
                .setParam(SettlementApi.SettlementListApi.showNum, LIST_DATA_SIZE)
                .setParam(SettlementApi.SettlementListApi.userId, App.userHelper.getUser().getUserId())

                //可选参数
                .setParam(SettlementApi.SettlementListApi.optionSearchType, mView.getDataType())
                .setParam(SettlementApi.SettlementListApi.optionTeamCode, "")
                .setParam(SettlementApi.SettlementListApi.optionPayCode, "")
                .setParam(SettlementApi.SettlementListApi.optionOutComeUnitCoding, "")
                .setParam(SettlementApi.SettlementListApi.optionInComeUnitCoding, "")
                .setParam(SettlementApi.SettlementListApi.optionIncreateTimeStart, "")
                .setParam(SettlementApi.SettlementListApi.optionIncreateTimeEnd, "")
                .setParam(SettlementApi.SettlementListApi.optionSubmitPayTimeStart, "")
                .setParam(SettlementApi.SettlementListApi.optionSubmitPayTimeEnd, "")
                .build().getParam();

        Subscription subscription = mInteractor.getDataByPage(new MyCallBack(mView, RequestType.TYPE_LATEST), param);
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadMore() {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SettlementApi.SettlementListApi.method)
                .setParam(SettlementApi.SettlementListApi.currentPageNum, mView.getPage())
                .setParam(SettlementApi.SettlementListApi.showNum, LIST_DATA_SIZE)
                .setParam(SettlementApi.SettlementListApi.userId, App.userHelper.getUser().getUserId())

                //可选参数
                .setParam(SettlementApi.SettlementListApi.optionSearchType, mView.getDataType())
                .setParam(SettlementApi.SettlementListApi.optionTeamCode, "")
                .setParam(SettlementApi.SettlementListApi.optionPayCode, "")
                .setParam(SettlementApi.SettlementListApi.optionOutComeUnitCoding, "")
                .setParam(SettlementApi.SettlementListApi.optionInComeUnitCoding, "")
                .setParam(SettlementApi.SettlementListApi.optionIncreateTimeStart, "")
                .setParam(SettlementApi.SettlementListApi.optionIncreateTimeEnd, "")
                .setParam(SettlementApi.SettlementListApi.optionSubmitPayTimeStart, "")
                .setParam(SettlementApi.SettlementListApi.optionSubmitPayTimeEnd, "")
                .build().getParam();

        Subscription subscription = mInteractor.getDataByPage(new MyCallBack(mView, RequestType.TYPE_LOAD_MORE), param);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {
        loadLatestData();
    }

    private class MyCallBack extends RequestCallBackAdapter<List<SettlementListBean>> {
        @RequestType
        private int reqType;

        private MyCallBack(BaseContract.View view, @RequestType int reqType) {
            this.reqType = reqType;
        }

        @Override
        public void success(List<SettlementListBean> data) {
            //返回的data必定不为空且大于0
            if (data.size() == DataSetting.LIST_DATA_SIZE) {
                mView.handlerListData(ResultCode.SUCCESS, reqType, data);
            } else if (data.size() > 0) {
                mView.handlerListData(ResultCode.SUCCESS_NO_MORE_DATA, reqType, data);
            } else {
                mView.handlerListData(ResultCode.SUCCESS_EMPTY, reqType, data);
            }
        }

        @Override
        public void onError(@ResultCode int resultCode, String errorMsg) {
            mView.onListError(resultCode, reqType, errorMsg);
        }
    }
}
