package com.msy.globalaccess.business.travelAgency.delegate.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.delegate.contract.IDelegateContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.data.interactor.IGuiderInteractor;
import com.msy.globalaccess.data.interactor.impl.GuiderInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;


/**
 * Created by pepys on 2017/7/4
 * description: 请求导游委派列表
 */
public class DelegatePresenterImpl extends BasePresenterImpl<IDelegateContract.View> implements IDelegateContract
        .Presenter {

    private IGuiderInteractor delegateInteractor;

    @Inject
    public DelegatePresenterImpl(GuiderInteractorImpl delegateInteractor) {
        this.delegateInteractor = delegateInteractor;
    }

    @Override
    public void onStart() {
        loadDelegate();
    }


    @Override
    public void loadDelegate() {

        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(GuiderApi.DelegateApi.METHOD)
                .setParam(GuiderApi.DelegateApi.USERID, App.userHelper.getUser().getUserId())
                .setParam(GuiderApi.DelegateApi.CURRENTPAGENUM, mView.getPageNum())
                .setParam(GuiderApi.DelegateApi.SHOWNUM, 10)
                .setParam(GuiderApi.DelegateApi.OPTYPE, "")
                .setParam(GuiderApi.DelegateApi.OPSTATUS, mView.getOpStatus())
                .setParam(GuiderApi.DelegateApi.TEAMCODE, "")
                .setParam(GuiderApi.DelegateApi.CREATETIMEEND, "")
                .setParam(GuiderApi.DelegateApi.CREATETIMESTART, "")
                .build().getParam();

        Subscription subscription = delegateInteractor.loadDelegate(new RequestCallBackAdapter<List
                <TouristDelegateBean>>() {
            @Override
            public void success(List<TouristDelegateBean> data) {
                super.success(data);
                mView.delegateSuccess(new ArrayList<>(data));
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.delegateFailure();
            }
        }, param);
        cacheSubscription.add(subscription);
    }
}
