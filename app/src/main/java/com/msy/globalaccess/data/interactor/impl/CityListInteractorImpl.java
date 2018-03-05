package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.TouristSpotsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.city.CityBeanWrapper;
import com.msy.globalaccess.data.interactor.CityListInteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by pepys on 2017/3/23.
 * description: 获取城市列表
 */
public class CityListInteractorImpl implements CityListInteractor<ArrayList<CityBean>> {

    private TouristSpotsApi.CityList cityListApi;

    @Inject
    public CityListInteractorImpl(Retrofit retrofit) {
        this.cityListApi = retrofit.create(TouristSpotsApi.CityList.class);
    }

    @Override
    public Subscription loadCityList(final RequestCallBack<ArrayList<CityBean>> callBack, HashMap<String, String>
            params) {
        callBack.beforeRequest();
        return cityListApi.getCityList(params)
                .compose(RxJavaUtils.<BaseBean<CityBeanWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<CityBeanWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<CityBeanWrapper>, ArrayList<CityBean>>() {
                    @Override
                    public ArrayList<CityBean> call(BaseBean<CityBeanWrapper> base) {
                        return base.getData().getCityList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));

    }
}
