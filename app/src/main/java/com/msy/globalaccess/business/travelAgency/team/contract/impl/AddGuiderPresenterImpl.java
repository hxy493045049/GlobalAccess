package com.msy.globalaccess.business.travelAgency.team.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.contract.IAddGuiderContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.api.KeyMapApi;
import com.msy.globalaccess.data.api.TouristSpotsApi;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.interactor.CityListInteractor;
import com.msy.globalaccess.data.interactor.IGuiderInteractor;
import com.msy.globalaccess.data.interactor.IKeyMapInteractor;
import com.msy.globalaccess.data.interactor.impl.CityListInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.GuiderInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.KeyMapInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.msy.globalaccess.data.api.GuiderApi.QueryFreeGuderApi.language;
import static com.msy.globalaccess.data.api.TouristSpotsApi.CityList.xian;


/**
 * Created by shawn on 2017/3/24 0024.
 * <p>
 * description :
 */

public class AddGuiderPresenterImpl extends BasePresenterImpl<IAddGuiderContract.View> implements
        IAddGuiderContract.Presenter {

    private IGuiderInteractor mInteractor;
    private CityListInteractor mCityListInteractor;
    private IKeyMapInteractor mapInteractor;

    @Inject
    public AddGuiderPresenterImpl(GuiderInteractorImpl interactor, CityListInteractorImpl cityListInteractor,
                                  KeyMapInteractorImpl mapInteractor) {
        mInteractor = interactor;
        mCityListInteractor = cityListInteractor;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void onStart() {
        loadCityList();
        loadLanguage();
        loadGuiderList();
    }

    @Override
    public void loadGuiderList() {
        HashMap<String, String> params = new ParamsHelper.Builder().setMethod(GuiderApi.QueryFreeGuderApi.method)
                .setParam(GuiderApi.QueryFreeGuderApi.userId, App.userHelper.getUser().getUserId())
                .setParam(GuiderApi.QueryFreeGuderApi.teamInfoId, mView.getTeamInfoId())
                .setParam(GuiderApi.QueryFreeGuderApi.delTag, "0")
                .setParam(GuiderApi.QueryFreeGuderApi.cityId, "")
                .setParam(GuiderApi.QueryFreeGuderApi.guideCode, mView.getGuiderIdentityNum())
                .setParam(GuiderApi.QueryFreeGuderApi.name, mView.getGuiderName())
                .setParam(GuiderApi.QueryFreeGuderApi.travelAgentName, mView.getGuiderAffiliateUnit())
                .setParam(language, mView.getLanguageType())
                .setParam(GuiderApi.QueryFreeGuderApi.checkTag, "1")
                .setParam(GuiderApi.QueryFreeGuderApi.city, mView.getCity())
                .build().getParam();

        Subscription subscription = mInteractor.getFreeGuiderList(new RequestCallBackAdapter<List<FreeGuiderListBean>>
                () {
            @Override
            public void success(List<FreeGuiderListBean> data) {
                super.success(data);
                if (data != null && data.size() > 0) {
                    mView.onGuiderListCallBack(ResultCode.SUCCESS, data);
                } else {
                    mView.onGuiderListCallBack(ResultCode.SUCCESS_EMPTY, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onGuiderListFailed(resultCode, errorMsg);
            }
        }, params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadCityList() {
        //获取Map集合
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(TouristSpotsApi.CityList.method)
                .setParam(TouristSpotsApi.CityList.cityLevel, mView.getCityLevel())
                .setParam(TouristSpotsApi.CityList.province, mView.getRegisterProvince())
                //---optional
                .setParam(TouristSpotsApi.CityList.cityId, "").setParam(TouristSpotsApi.CityList.cityCode, "").setParam(TouristSpotsApi.CityList.foreigh, "").setParam(TouristSpotsApi.CityList.pcityId, "")
                .setParam(TouristSpotsApi.CityList.cityName, "").setParam(xian, "").setParam(TouristSpotsApi.CityList.city, "").setParam(TouristSpotsApi.CityList.isAlaway, "")
                .build().getParam();

        Subscription subscription = mCityListInteractor.loadCityList(new RequestCallBackAdapter<List<CityBean>>() {
            @Override
            public void success(List<CityBean> data) {
                if (data != null && data.size() > 0) {
                    mView.onCityCallBack(data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast("网络异常,请重试");
            }
        }, params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadLanguage() {
        HashMap<String, String> params = new ParamsHelper.Builder().setMethod(KeyMapApi.method)
                .setParam(KeyMapApi.mapType, KeyMapApi.languageType).build().getParam();

        Subscription subscription = mapInteractor.getDataByType(new RequestCallBackAdapter<List<KeyMapBean>>() {
            @Override
            public void success(List<KeyMapBean> data) {
                if (data != null && data.size() > 0) {
                    mView.onLanguageCallBack(data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast("网络异常,请重试");
            }
        }, params);
        cacheSubscription.add(subscription);
    }
}
