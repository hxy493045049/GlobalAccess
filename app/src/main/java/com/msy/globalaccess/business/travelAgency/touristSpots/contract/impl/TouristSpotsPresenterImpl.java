package com.msy.globalaccess.business.travelAgency.touristSpots.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.TouristSpotsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.KeyMapApi;
import com.msy.globalaccess.data.api.TouristSpotsApi;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.interactor.CityListInteractor;
import com.msy.globalaccess.data.interactor.TouristSpotsInteractor;
import com.msy.globalaccess.data.interactor.impl.CityListInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.KeyMapInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.TouristSpotsInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by pepys on 2017/3/21.
 * description:选择景点界面
 */
public class TouristSpotsPresenterImpl extends BasePresenterImpl<TouristSpotsContract.View> implements
        TouristSpotsContract.Presenter {

    /**
     * 景点加载
     */
    private TouristSpotsInteractor<SearchScenicBean> touristSpotsInteractor;
    /**
     * 城市加载
     */
    private CityListInteractor<ArrayList<CityBean>> cityListInteractor;

    private KeyMapInteractorImpl  keyMapInteractor;
    @Inject
    public TouristSpotsPresenterImpl(TouristSpotsInteractorImpl touristSpotsInteractorimpl, CityListInteractorImpl cityListInteractor, KeyMapInteractorImpl keyMapInteractor) {
        this.touristSpotsInteractor = touristSpotsInteractorimpl;
        this.cityListInteractor = cityListInteractor;
        this.keyMapInteractor = keyMapInteractor;
    }

    @Override
    public void getSpotsList() {
        Subscription subscription = touristSpotsInteractor.getSpotList(new RequestCallBack<SearchScenicBean>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(SearchScenicBean data) {
                mView.loadSpotsSuccess(data.getScenicList());
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.loadSpotsFailure();
            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        }, mView.currentPageNum(), mView.showNum(), App.userHelper.getUser().getUserId(), 0,mView.scenicName(), mView
                .cityName(), mView.scenicLevel(), mView.isAlaway(),false);
        cacheSubscription.add(subscription);
    }

    @Override
    public void getCityList() {
        //获取Map集合
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(TouristSpotsApi.CityList.method)
                .setParam(TouristSpotsApi.CityList.cityId, "")
                .setParam(TouristSpotsApi.CityList.cityLevel, "2")
                .setParam(TouristSpotsApi.CityList.cityCode, "")
                .setParam(TouristSpotsApi.CityList.foreigh, "")
                .setParam(TouristSpotsApi.CityList.pcityId, "")
                .setParam(TouristSpotsApi.CityList.cityName, "")
                .setParam(TouristSpotsApi.CityList.province, App.userHelper.getUser().getProvince())
                .setParam(TouristSpotsApi.CityList.xian, "")
                .setParam(TouristSpotsApi.CityList.city, "")
                .setParam(TouristSpotsApi.CityList.isAlaway, "")
                .build().getParam();
        Subscription subscription = cityListInteractor.loadCityList(new RequestCallBack<ArrayList<CityBean>>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(ArrayList<CityBean> data) {
                mView.loadCitySuccess(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.loadCityFailure(errorMsg);
            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        }, params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void getLevel() {
        //获取Map集合
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(KeyMapApi.method)
                .setParam(KeyMapApi.mapType, KeyMapApi.levelType)
                .build().getParam();
        Subscription subscription = keyMapInteractor.getDataByType(new RequestCallBackAdapter<List<KeyMapBean>>() {
            @Override
            public void success(List<KeyMapBean> data) {
                if (data != null && data.size() > 0) {
                    mView.loadLevelSuccess(data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.loadLevelFailure(errorMsg);
            }
        },params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {
        getSpotsList();
        getCityList();
        getLevel();
    }

}
