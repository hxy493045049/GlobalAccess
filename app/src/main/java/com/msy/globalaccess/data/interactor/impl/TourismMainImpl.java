package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.SearchApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.holder.TravelHelper;
import com.msy.globalaccess.data.interactor.ITourismMainInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by WuDebin on 2017/5/16.
 */

public class TourismMainImpl implements ITourismMainInteractor {

    private Retrofit mRetrofit;

    @Inject
    TourismMainImpl(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }

    @Override
    public Subscription loadTravelData(final RequestCallBack callBack) {

        SearchApi.TravelAgentSearchApi api = mRetrofit.create(SearchApi.TravelAgentSearchApi.class);

        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(SearchApi.appTravelAgentSearch)
                .setParam(SearchApi.TravelshowNum, "9999")
                .setParam(SearchApi.TravelcurrentPageNum, "1")
                .setParam(SearchApi.TraveluserId, App.userHelper.getUser().getUserId())
                .setParam(SearchApi.travelAgentName, "")
                .build().getParam();

        return api.HotelSearch(params).compose(RxJavaUtils.<BaseBean<SearchTravelAgentBean>>defaultSchedulers())
                .doOnNext(new Action1<BaseBean<SearchTravelAgentBean>>() {
                    @Override
                    public void call(BaseBean<SearchTravelAgentBean> travelAgentBeanBaseBean) {
                        if (travelAgentBeanBaseBean != null && travelAgentBeanBaseBean.getData() != null) {
                            TravelHelper.getInstance().insertTravelAgentList(travelAgentBeanBaseBean.getData().getTravelAgentList());
                        }
                    }
                }).subscribe(new Subscriber<BaseBean<SearchTravelAgentBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.internet_error);
                    }

                    @Override
                    public void onNext(BaseBean<SearchTravelAgentBean> data) {
                        if (data.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(data.getData());
                        } else {
                            ToastUtils.showToast(data.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, data.getMessage());
                        }
                    }
                });
    }

    @Override
    public Subscription loadTravelTeamType(final RequestCallBack callBack) {
        SearchApi searchApi = mRetrofit.create(SearchApi.class);
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(SearchApi.SEARCH_TEAM_TYPE)
                .build().getParam();
        return searchApi.getTeamType(param).subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .compose(RxJavaUtils.<BaseBean<SearchTeamTypeBean>>defaultSchedulers())
                .subscribe(new Subscriber<BaseBean<SearchTeamTypeBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        callBack.onError(ResultCode.NET_ERROR, "加载失败");
                        Logger.e(e, "");
                    }

                    @Override
                    public void onNext(BaseBean<SearchTeamTypeBean> TeamType) {
                        //请求成功
                        if (TeamType != null) {
                            if (ResultCode.SUCCESS == TeamType.getStatus()) {
                                callBack.success(changeType(TeamType.getData()));
                            } else {
                                ToastUtils.showToast(TeamType.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * 转换为通用数据
     *
     * @param bean 从后台获取的数据
     * @return 通用数据
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> changeType(SearchTeamTypeBean bean) {
        ArrayList<TourismDropListBean.DropSelectableBean> currencyList = null;
        ArrayList<SearchTeamTypeBean.TeamTypeListBean> teamTypelist = bean.getTeamTypeList();
        if (teamTypelist != null && teamTypelist.size() > 0) {
            currencyList = new ArrayList<>();
            for (int i = 0; i < bean.getTeamTypeList().size(); i++) {
                currencyList.add(new TourismDropListBean.DropSelectableBean(teamTypelist.get(i).getTeamTypeName(), teamTypelist.get(i).getTeamTypeId(), false));
            }
        }
        return currencyList;
    }
}
