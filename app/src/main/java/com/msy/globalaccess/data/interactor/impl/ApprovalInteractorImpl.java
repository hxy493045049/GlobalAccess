package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.ApprovalApi;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.interactor.ApprovalInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by pepys on 2017/2/15.
 * description: 审批请求
 */
public class ApprovalInteractorImpl implements ApprovalInteractor {

    private ApprovalApi approvalApi;

    private ApprovalApi.ApprovalDelegateTouristApi approvalDelegateTouristApi;

    @Inject
    public ApprovalInteractorImpl(Retrofit retrofit) {
        this.approvalApi = retrofit.create(ApprovalApi.class);
        this.approvalDelegateTouristApi =  retrofit.create(ApprovalApi.ApprovalDelegateTouristApi.class);
    }

    @Override
    public Subscription loadApproval(final RequestCallBack callBack, String userID, String teamID, int opType, int opStatus, String remark) {
        callBack.beforeRequest();

        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(ApprovalApi.METHOD)
                .setParam(ApprovalApi.USERID, userID)
                .setParam(ApprovalApi.TEAMID, teamID)
                .setParam(ApprovalApi.OPTYPE, opType)
                .setParam(ApprovalApi.OPSTATUS, opStatus)
                .setParam(ApprovalApi.REMARK, remark)
                .build().getParam();

        return approvalApi.Approval(param)
                .compose(RxJavaUtils.<NoDataBean>defaultSchedulers())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {
                        callBack.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.request_error);
                        Logger.e(e, "审批请求失败：");
                        callBack.after();
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(noDataBean.getMessage());
                        } else {
                            ToastUtils.showLongToast(noDataBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, noDataBean.getMessage());
                        }
                    }
                });
    }

    @Override
    public Subscription loadApprovalDelegateTourist(final RequestCallBack callBack, HashMap params) {
        callBack.beforeRequest();
        return approvalDelegateTouristApi.ApprovalDelegateTourist(params)
                .compose(RxJavaUtils.<NoDataBean>defaultSchedulers())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.request_error);
                        Logger.e(throwable, "审批导游委派请求失败：");
                        callBack.after();
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(noDataBean.getMessage());
                        } else {
                            ToastUtils.showLongToast(noDataBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, noDataBean.getMessage());
                        }
                    }
                });
    }
}
