package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.SearchApi;
import com.msy.globalaccess.data.api.TouristSpotsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.TravelDepBean;
import com.msy.globalaccess.data.interactor.SearchInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * 查询实现类
 * Created by chensh on 2017/2/10 0010.
 */

public class SearchLoadDataImpl implements SearchInteractor {
    private Retrofit mRetrofit;

    @Inject
    SearchLoadDataImpl(Retrofit mRetrofit) {
        this.mRetrofit = mRetrofit;
    }

    @Override
    public Subscription loadTeamType(final RequestCallBack callBack) {
        SearchApi service = mRetrofit.create(SearchApi.class);
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SearchApi.SEARCH_TEAM_TYPE)
                .build().getParam();

        return service.getTeamType(param).subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .compose(RxJavaUtils.<BaseBean<SearchTeamTypeBean>>defaultSchedulers())
                .subscribe(new Subscriber<BaseBean<SearchTeamTypeBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        callBack.onError(ResultCode.NET_ERROR, "加载失败");
                        Logger.getLogger(e.toString());
                    }

                    @Override
                    public void onNext(BaseBean<SearchTeamTypeBean> TeamType) {
                        //请求成功
                        if (TeamType != null) {
                            if (ResultCode.SUCCESS == TeamType.getStatus()) {
                                callBack.success(TeamType.getData());
                            } else {
                                ToastUtils.showToast(TeamType.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public Subscription TravelDepApi(final RequestCallBack callBack, String travelDepName) {
        SearchApi.TravelDepApi service = mRetrofit.create(SearchApi.TravelDepApi.class);
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SearchApi.TravelDepApi.Travelmethod)
                .setParam(SearchApi.TravelshowNum, "50")
                .setParam(SearchApi.TravelcurrentPageNum, "1")
                .setParam(SearchApi.TraveluserId, App.userHelper.getUser().getUserId())
                .setParam(SearchApi.TravelDepApi.TraveltravelDepName, travelDepName)
                .build().getParam();
        return service.loadTravelDep(param).subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .compose(RxJavaUtils.<BaseBean<TravelDepBean>>defaultSchedulers())
                .subscribe(new Subscriber<BaseBean<TravelDepBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        callBack.onError(ResultCode.NET_ERROR, "加载失败");
                        Logger.getLogger(e.toString());
                    }

                    @Override
                    public void onNext(BaseBean<TravelDepBean> travelDep) {
                        //请求成功
                        if (travelDep != null) {
                            if (ResultCode.SUCCESS == travelDep.getStatus()) {
                                callBack.success(travelDep.getData());
                            } else {
                                ToastUtils.showToast(travelDep.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public Subscription ScenicSearch(final RequestCallBack callBack) {
        SearchConvertImpl request = new SearchConvertImpl(mRetrofit);
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SearchApi.appScenicSearch)
                .setParam(SearchApi.TravelshowNum, "9999")
                .setParam(SearchApi.TravelcurrentPageNum, "1")
                .setParam(SearchApi.TraveluserId, App.userHelper.getUser().getUserId())
                .setParam(TouristSpotsApi.SpotList.isAcc, 1)
                .setParam(SearchApi.scenicName, "")
                .setParam(SearchApi.cityName, "")
                .setParam(SearchApi.scenicLevel, "")
                .setParam(SearchApi.isAlaway, "")
                .build().getParam();
        return request.request(SearchApi.appScenicSearch, param, callBack);
    }

    @Override
    public Subscription InsuranceSearch(RequestCallBack callBack) {
        SearchConvertImpl request = new SearchConvertImpl(mRetrofit);
        return request.request(SearchApi.appInsuranceSearch, initParam(SearchApi.appInsuranceSearch, SearchApi.insuranceName), callBack);
    }

    @Override
    public Subscription VehicleSearch(RequestCallBack callBack) {
        SearchConvertImpl request = new SearchConvertImpl(mRetrofit);
        return request.request(SearchApi.appVehicleSearch, initParam(SearchApi.appVehicleSearch, SearchApi.vehicleName), callBack);
    }

    @Override
    public Subscription HotelSearch(RequestCallBack callBack) {

        SearchConvertImpl request = new SearchConvertImpl(mRetrofit);
        return request.request(SearchApi.appHotelSearch, initParam(SearchApi.appHotelSearch, SearchApi.hotelName), callBack);
    }

    @Override
    public Subscription TravelAgentSearch(RequestCallBack callBack) {
        SearchConvertImpl request = new SearchConvertImpl(mRetrofit);
        return request.request(SearchApi.appTravelAgentSearch, initParam(SearchApi.appTravelAgentSearch, SearchApi.travelAgentName), callBack);
    }

    /**
     * 填充公用param
     *
     * @param method action
     * @return param
     */
    private HashMap<String, String> initParam(String method, String name) {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(method)
                .setParam(SearchApi.TravelshowNum, "9999")
                .setParam(SearchApi.TravelcurrentPageNum, "1")
                .setParam(SearchApi.TraveluserId, App.userHelper.getUser().getUserId())
                .setParam(name, "")
                .build().getParam();

        return param;
    }
}
