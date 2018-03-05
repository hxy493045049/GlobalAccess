package com.msy.globalaccess.business.travelAgency.touristSpots.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.SearchSpotContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.interactor.TouristSpotsInteractor;
import com.msy.globalaccess.data.interactor.impl.TouristSpotsInteractorImpl;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by pepys on 2017/4/17.
 * description:
 */
public class SearchSpotsPresenterImpl extends BasePresenterImpl<SearchSpotContract.View> implements SearchSpotContract.Presenter {

    /**
     * 景点加载
     */
    private TouristSpotsInteractor<SearchScenicBean> touristSpotsInteractor;


    @Inject
    public SearchSpotsPresenterImpl(TouristSpotsInteractorImpl touristSpotsInteractorimpl) {
        this.touristSpotsInteractor = touristSpotsInteractorimpl;
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
        }, mView.currentPageNum(), mView.showNum(), App.userHelper.getUser().getUserId(),0, mView.scenicName(), mView
                .cityName(), mView.scenicLevel(), mView.isAlaway(),false);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {
    }
}
