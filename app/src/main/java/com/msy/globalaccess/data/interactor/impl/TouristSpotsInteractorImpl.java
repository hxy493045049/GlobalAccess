package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.TouristSpotsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.holder.TouristHelper;
import com.msy.globalaccess.data.interactor.TouristSpotsInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by pepys on 2017/3/21.
 * description:
 */
public class TouristSpotsInteractorImpl implements TouristSpotsInteractor<SearchScenicBean> {

    private TouristSpotsApi.SpotList touristSpotsApi;

    @Inject
    public TouristSpotsInteractorImpl(Retrofit mRetrofit) {
        touristSpotsApi = mRetrofit.create(TouristSpotsApi.SpotList.class);
    }

    @Override
    public Subscription getSpotList(final RequestCallBack<SearchScenicBean> callBack, int currentPageNum, int showNum, String userID,
                                    int isAcc, String scenicName, String cityName, String scenicLevel, String isAlaway, final boolean isSave) {
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(TouristSpotsApi.SpotList.method)
                .setParam(TouristSpotsApi.SpotList.showNum, showNum)
                .setParam(TouristSpotsApi.SpotList.currentPageNum, currentPageNum)
                .setParam(TouristSpotsApi.SpotList.userId, userID)
                .setParam(TouristSpotsApi.SpotList.isAcc, isAcc)
                .setParam(TouristSpotsApi.SpotList.scenicName, scenicName)
                .setParam(TouristSpotsApi.SpotList.cityName, cityName)
                .setParam(TouristSpotsApi.SpotList.scenicLevel, scenicLevel)
                .setParam(TouristSpotsApi.SpotList.isAlaway, isAlaway)
                .build().getParam();
        callBack.beforeRequest();
        return touristSpotsApi.getSpotList(param)
                .subscribeOn(Schedulers.io())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<BaseBean<SearchScenicBean>>() {
                    @Override
                    public void call(BaseBean<SearchScenicBean> searchScenicBeanBaseBean) {
                        if (searchScenicBeanBaseBean != null && searchScenicBeanBaseBean.getData() != null&&isSave) {
                            TouristHelper.getInstance().insertTravelAgentList(searchScenicBeanBaseBean.getData().getScenicList());
                        }
                    }
                })
                .subscribe(new Subscriber<BaseBean<SearchScenicBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.internet_error);
                        Logger.e(e, "TouristSpotsInteractorImpl");
                        callBack.after();
                    }

                    @Override
                    public void onNext(BaseBean<SearchScenicBean> searchScenicBeanBaseBean) {
                        if (searchScenicBeanBaseBean.getStatus() == ResultCode.SUCCESS || searchScenicBeanBaseBean.getStatus() == ResultCode.SEARCH_NO_DATA) {
                            callBack.success(searchScenicBeanBaseBean.getData());
                        } else {
                            ToastUtils.showToast(searchScenicBeanBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, searchScenicBeanBaseBean.getMessage());
                        }
                        callBack.after();
                    }
                });
    }

}
