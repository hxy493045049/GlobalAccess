package com.msy.globalaccess.data.interactor.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.SearchApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.CurrencyBean;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.search.SearchHotelBean;
import com.msy.globalaccess.data.bean.search.SearchInsuranceBean;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.search.SearchVehicleBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.exception.RxException;
import com.msy.globalaccess.utils.ErrorUtils;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 简化支出/收入单位请求 数据转换类
 * Created by chensh on 2017/2/15 0015.
 */
public class SearchConvertImpl {
    private static String TAG = SearchConvertImpl.class.getSimpleName();
    private SearchApi searchApi;

    @Inject
    public SearchConvertImpl(Retrofit mRetrofit) {
        this.searchApi = mRetrofit.create(SearchApi.class);
    }

    public Subscription request(final String method, HashMap<String, String> param, RequestCallBack callBack) {
        try {
            return searchApi.SearchConvert(param).subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(Schedulers.io())         //请求完成后在io线程中执行
                    .flatMap(new Func1<BaseBean<Object>, Observable<ArrayList<CurrencyBean>>>() {
                        @Override
                        public Observable<ArrayList<CurrencyBean>> call(BaseBean<Object> travelAgent) {
                            if (travelAgent.getStatus() == ResultCode.SUCCESS || travelAgent.getStatus() == ResultCode.SEARCH_NO_DATA) {
                                ArrayList<CurrencyBean> list = null;
                                //类型转换
                                if (travelAgent.getData() != null) {
                                    list = chang(travelAgent.getData().toString(), method);
                                }
                                return Observable.just(list);
                            } else {
                                String errorMessage = ErrorUtils.getErrorMessage(travelAgent, R.string.internet_error);
                                return Observable.error(new RxException(travelAgent.getStatus(), errorMessage));
                            }
                        }
                    }).subscribe(new DefaultSubscriber<>(callBack));
        } catch (Exception e) {
            Log.i("", "" + e.toString());
            return null;
        }

    }

    private ArrayList<CurrencyBean> chang(String travelAgent, String method) {
        ArrayList<CurrencyBean> list = new ArrayList<>();
        try {
            if (method.equals(SearchApi.appTravelAgentSearch)) {
                SearchTravelAgentBean agentBean = new Gson().fromJson(travelAgent, SearchTravelAgentBean.class);
                ArrayList<TravelAgentListBean> scenlist = agentBean.getTravelAgentList();
                for (int i = 0; i < scenlist.size(); i++) {
                    CurrencyBean currencyBean = new CurrencyBean();
                    currencyBean.setId(scenlist.get(i).getTravelAgentId());
                    currencyBean.setName(scenlist.get(i).getTravelAgentName());
                    list.add(currencyBean);
                }
            } else if (method.equals(SearchApi.appHotelSearch)) {
                SearchHotelBean hotel = new Gson().fromJson(travelAgent, SearchHotelBean.class);
                ArrayList<SearchHotelBean.HotelListBean> hotelList = hotel.getHotelList();
                for (int i = 0; i < hotelList.size(); i++) {
                    CurrencyBean currencyBean = new CurrencyBean();
                    currencyBean.setId(hotelList.get(i).getHotelId());
                    currencyBean.setName(hotelList.get(i).getHotelName());
                    list.add(currencyBean);
                }
            } else if (method.equals(SearchApi.appVehicleSearch)) {

                SearchVehicleBean vehicleBean = new Gson().fromJson(travelAgent, SearchVehicleBean.class);
                ArrayList<SearchVehicleBean.VehicleListBean> vehicleList = vehicleBean.getVehicleList();
                for (int i = 0; i < vehicleList.size(); i++) {
                    CurrencyBean currencyBean = new CurrencyBean();
                    currencyBean.setId(vehicleList.get(i).getVehicleId());
                    currencyBean.setName(vehicleList.get(i).getVehicleName());
                    list.add(currencyBean);
                }
            } else if (method.equals(SearchApi.appInsuranceSearch)) {

                SearchInsuranceBean vehicleBean = new Gson().fromJson(travelAgent, SearchInsuranceBean.class);
                ArrayList<SearchInsuranceBean.InsuranceListBean> insuranceList = vehicleBean.getInsuranceList();
                for (int i = 0; i < insuranceList.size(); i++) {
                    CurrencyBean currencyBean = new CurrencyBean();
                    currencyBean.setId(insuranceList.get(i).getInsuranceId());
                    currencyBean.setName(insuranceList.get(i).getInsuranceName());
                    list.add(currencyBean);
                }
            } else if (method.equals(SearchApi.appScenicSearch)) {
                SearchScenicBean insuranceBean = new Gson().fromJson(travelAgent, SearchScenicBean.class);
                ArrayList<ScenicListBean> insuranceList = insuranceBean.getScenicList();
                for (int i = 0; i < insuranceList.size(); i++) {
                    CurrencyBean currencyBean = new CurrencyBean();
                    currencyBean.setId(insuranceList.get(i).getScenicId());
                    currencyBean.setName(insuranceList.get(i).getScenicName());
                    list.add(currencyBean);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "error   " + e.toString());
        }
        return list;
    }
}
