package com.msy.globalaccess.business.travelAgency.search.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.SearchApi;
import com.msy.globalaccess.data.bean.base.CurrencyBean;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.search.SearchValueListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.bean.TravelDepBean;
import com.msy.globalaccess.data.holder.TouristHelper;
import com.msy.globalaccess.data.holder.TravelHelper;
import com.msy.globalaccess.data.interactor.impl.SearchLoadDataImpl;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Search--module
 * Created by chensh on 2017/1/24 0024.
 */

public class SearchPresenterImpl extends BasePresenterImpl<SearchContract.View> implements SearchContract.Presenter {
    /**
     * 请求实体类
     */
    private SearchLoadDataImpl searchLoadData;

    @Inject
    SearchPresenterImpl(SearchLoadDataImpl searchLoadData) {
        this.searchLoadData = searchLoadData;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadTeamType() {
        //团队类型
        Subscription subscription = searchLoadData.loadTeamType(new RequestCallBackAdapter<SearchTeamTypeBean>
                () {
            @Override
            public void beforeRequest() {
            }

            @Override
            public void success(SearchTeamTypeBean data) {
                if (mView != null) {
                    mView.getTeamType(data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadTravelDep(String travelDepName) {
        //旅行社部门
        mView.showProgress();
        Subscription subscription = searchLoadData.TravelDepApi(new RequestCallBackAdapter<TravelDepBean>
                () {
            @Override
            public void success(TravelDepBean data) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.getTravelDep(data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, travelDepName);
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadScenicSearch() {
        if ("2".equals(App.userHelper.getUser().getUserRoleType())) {
            chang(SearchApi.appScenicSearch);
        } else {
            Subscription subscription = searchLoadData.ScenicSearch(new RequestCallBackAdapter<ArrayList<CurrencyBean>>() {
                @Override
                public void success(ArrayList<CurrencyBean> data) {
                    mView.hideProgress();
                    mView.getScenicSearch(new SearchValueListBean(0, "景区", data));
                }

                @Override
                public void onError(@ResultCode int resultCode, String errorMsg) {
                    super.onError(resultCode, errorMsg);
                    ToastUtils.showToast(errorMsg);
                    if (mView != null) {
                        mView.hideProgress();
                    }
                }
            });
            cacheSubscription.add(subscription);
        }
    }

    @Override
    public void loadInsuranceSearch() {
        Subscription subscription = searchLoadData.InsuranceSearch(new RequestCallBackAdapter<ArrayList<CurrencyBean>>
                () {
            @Override
            public void success(ArrayList<CurrencyBean> data) {
                mView.hideProgress();
                mView.getScenicSearch(new SearchValueListBean(1, "保险公司", data));
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadVehicleSearch() {
        Subscription subscription = searchLoadData.VehicleSearch(new RequestCallBackAdapter<ArrayList<CurrencyBean>>
                () {
            @Override
            public void success(ArrayList<CurrencyBean> data) {
                mView.hideProgress();
                mView.getScenicSearch(new SearchValueListBean(2, "客运公司", data));
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadHotelSearch() {
        Subscription subscription = searchLoadData.HotelSearch(new RequestCallBackAdapter<ArrayList<CurrencyBean>>
                () {
            @Override
            public void success(ArrayList<CurrencyBean> data) {
                if (mView != null) {
                    mView.hideProgress();
                    mView.getScenicSearch(new SearchValueListBean(3, "酒店", data));
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadTravelAgentSearch() {
        if("2".equals(App.userHelper.getUser().getUserRoleType())){
            chang(SearchApi.appTravelAgentSearch);
        }else{
            Subscription subscription = searchLoadData.TravelAgentSearch(new RequestCallBackAdapter<ArrayList<CurrencyBean>>
                    () {
                @Override
                public void success(ArrayList<CurrencyBean> data) {
                    if (mView != null) {
                        mView.getScenicSearch(new SearchValueListBean(4, "旅行社", data));
                    }
                }

                @Override
                public void onError(@ResultCode int resultCode, String errorMsg) {
                    super.onError(resultCode, errorMsg);
                    ToastUtils.showToast(errorMsg);
                    if (mView != null) {
                        mView.hideProgress();
                    }
                }
            });
            cacheSubscription.add(subscription);
        }

    }

    private void chang(final String method) {
        Observable.just(method)
                .map(new Func1<String, ArrayList<CurrencyBean>>() {
                    @Override
                    public ArrayList<CurrencyBean> call(String method) {
                        ArrayList<CurrencyBean> list = new ArrayList<>();
                        if (method.equals(SearchApi.appTravelAgentSearch)) {
                            List<TravelAgentListBean> scenlist = TravelHelper.getInstance().getTravelAll();
                            for (int i = 0; i < scenlist.size(); i++) {
                                CurrencyBean currencyBean = new CurrencyBean();
                                currencyBean.setId(scenlist.get(i).getTravelAgentId());
                                currencyBean.setName(scenlist.get(i).getTravelAgentName());
                                list.add(currencyBean);
                            }
                        }else if (method.equals(SearchApi.appScenicSearch)) {
                            List<ScenicListBean> insuranceList = TouristHelper.getInstance().getTravelAll();
                            for (int i = 0; i < insuranceList.size(); i++) {
                                CurrencyBean currencyBean = new CurrencyBean();
                                currencyBean.setId(insuranceList.get(i).getScenicId());
                                currencyBean.setName(insuranceList.get(i).getScenicName());
                                list.add(currencyBean);
                            }
                        }
                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<CurrencyBean>>() {
                    @Override
                    public void call(ArrayList<CurrencyBean> currencyBeen) {
                        mView.hideProgress();
                        if(method.equals(SearchApi.appTravelAgentSearch)){
                            mView.getScenicSearch(new SearchValueListBean(4, "旅行社",currencyBeen));
                        }else if(method.equals(SearchApi.appScenicSearch)){
                            mView.getScenicSearch(new SearchValueListBean(0, "景区",currencyBeen));
                        }
                    }
                });
    }
}
