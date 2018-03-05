package com.msy.globalaccess.data.interactor.impl;


import com.google.gson.Gson;
import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.TravelAttractionsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsParentBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.TravelAttractionsInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by WuDebin on 2017/3/21.
 */

public class TravelAttractionsInteractorImpl implements TravelAttractionsInteractor {

    private Retrofit mRetrofit;

    @Inject
    TravelAttractionsInteractorImpl(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    @Override
    public Subscription loadTravelAttractionsData(final RequestCallBack callBack, String teamId) {
        TravelAttractionsApi.QueryTravelAttractionsApi queryTravelAttractionsApi = mRetrofit.create
                (TravelAttractionsApi.QueryTravelAttractionsApi.class);
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(TravelAttractionsApi.QueryTravelAttractionsApi.method)
                .setParam(TravelAttractionsApi.QueryTravelAttractionsApi.teamId, teamId)
                .build().getParam();
        return queryTravelAttractionsApi.getTravelAttractionsList(params).compose(RxJavaUtils
                .<BaseBean<TravelAttractionsParentBean>>defaultSchedulers()).subscribe(new Subscriber<BaseBean<TravelAttractionsParentBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(ResultCode.NET_ERROR, "");
                ToastUtils.showToast(R.string.internet_error);
                Logger.e(e,"TravelAttractionsInteractorImpl");
            }

            @Override
            public void onNext(BaseBean<TravelAttractionsParentBean> baseBean) {
                if (baseBean.getStatus() == ResultCode.SUCCESS) {
                    callBack.success(baseBean.getData());
                } else {
                    ToastUtils.showToast(baseBean.getMessage());
                    callBack.onError(ResultCode.NET_ERROR, baseBean.getMessage());
                }
            }
        });
    }

    @Override
    public Subscription commitTravelAttractionsData(final RequestCallBack callBack, ArrayList arrayList, String
            teamId) {
        TravelAttractionsApi.CommitTravelAttractionsApi commitTravelAttractionsApi = mRetrofit.create
                (TravelAttractionsApi.CommitTravelAttractionsApi.class);
        Gson gson = new Gson();

        ArrayList<TripScenicBean> tripScenicBeanArrayList = arrayList;


        String newStr = "";
        try {
            newStr = new String((gson.toJson(tripScenicBeanArrayList).toString()).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(TravelAttractionsApi.CommitTravelAttractionsApi.method)
                .setParam(TravelAttractionsApi.CommitTravelAttractionsApi.teamId, teamId)
                .setParam(TravelAttractionsApi.CommitTravelAttractionsApi.userId, UserHelper.getInstance().getUser()
                        .getUserId())
                .setParam(TravelAttractionsApi.CommitTravelAttractionsApi.tripScenicList, newStr)
                .build().getParam();
        return commitTravelAttractionsApi.commitTravelAttractionsList(params).compose(RxJavaUtils
                .<BaseBean>defaultSchedulers()).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e, "请求失败");
                callBack.onError(ResultCode.NET_ERROR, "");
                ToastUtils.showToast(R.string.internet_error);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getStatus() == ResultCode.SUCCESS) {
                    callBack.success(baseBean);
                } else {
                    ToastUtils.showToast(baseBean.getMessage());
                    callBack.onError(ResultCode.NET_ERROR, baseBean.getMessage());
                }
            }
        });
    }


    @Override
    public Subscription queryTripScenicIsOrder(final RequestCallBack callBack, String tripScenicId, final int
            operationType) {
        TravelAttractionsApi.queryTripScenicIsOrderApi queryTripScenicIsOrderApi = mRetrofit.create
                (TravelAttractionsApi.queryTripScenicIsOrderApi.class);
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(TravelAttractionsApi.queryTripScenicIsOrderApi.method)
                .setParam(TravelAttractionsApi.queryTripScenicIsOrderApi.tripScenicId, tripScenicId)
                .build().getParam();
        return queryTripScenicIsOrderApi.queryTripScenicIsOrder(params).compose(RxJavaUtils
                .<BaseBean>defaultSchedulers()).subscribe(new Subscriber<BaseBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(ResultCode.NET_ERROR, "");
                ToastUtils.showToast(R.string.internet_error);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getStatus() == ResultCode.SUCCESS) {
                    callBack.success(baseBean);
                } else {
                    //                    if(operationType== TravelAttractionsActivity.OPERATION_DELETED) {
                    ToastUtils.showToast(baseBean.getMessage());
                    //                    }
                    callBack.onError(ResultCode.NET_ERROR, baseBean.getMessage());
                }
            }
        });
    }
}
