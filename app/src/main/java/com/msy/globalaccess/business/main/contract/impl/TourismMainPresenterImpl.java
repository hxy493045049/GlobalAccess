package com.msy.globalaccess.business.main.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.main.contract.TourismMainContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.interactor.TouristSpotsInteractor;
import com.msy.globalaccess.data.interactor.impl.TourismMainImpl;
import com.msy.globalaccess.data.interactor.impl.TouristSpotsInteractorImpl;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;

/**
 * 旅游局：旅行社/景点数据获取实现类
 * Created by WuDebin on 2017/5/16.
 */

public class TourismMainPresenterImpl extends BasePresenterImpl<TourismMainContract.View> implements TourismMainContract.Presenter {

    private TourismMainImpl impl;
    private TouristSpotsInteractor touristInteractor;

    @Inject
    public TourismMainPresenterImpl(TourismMainImpl impl, TouristSpotsInteractorImpl touristImpl) {
        this.impl = impl;
        this.touristInteractor = touristImpl;
    }

    @Override
    public void loadTravelData() {
        mView.showProgress();
        Subscription subscription = impl.loadTravelData(new RequestCallBackAdapter<SearchTravelAgentBean>
                () {
            @Override
            public void success(SearchTravelAgentBean data) {
                mView.hideProgress();
                mView.saveTravelData(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
                ToastUtils.showToast(errorMsg);
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadTravelTeamType() {
        Subscription subscription = impl.loadTravelTeamType(new RequestCallBackAdapter<ArrayList<TourismDropListBean.DropSelectableBean>>
                () {
            @Override
            public void success(ArrayList<TourismDropListBean.DropSelectableBean> data) {
                mView.getTeamType(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadTouristData() {
        Subscription subscription = touristInteractor.getSpotList(new RequestCallBack<SearchScenicBean>() {
            @Override
            public void beforeRequest() {
                mView.showProgress();
            }

            @Override
            public void success(SearchScenicBean data) {
                mView.getTouristData(data.getScenicList());
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                ToastUtils.showToast(errorMsg);
            }

            @Override
            public void after() {
                mView.hideProgress();
            }
        }, mView.currentPageNum(), mView.showNum(), App.userHelper.getUser().getUserId(), 0, mView.scenicName(), mView
                .cityName(), mView.scenicLevel(), mView.isAlaway(),true);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {

    }
}
