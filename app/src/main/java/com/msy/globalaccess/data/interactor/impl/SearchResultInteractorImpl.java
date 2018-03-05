package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.SearchApi;
import com.msy.globalaccess.data.api.SettlementApi;
import com.msy.globalaccess.data.api.TeamServiceApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListWrapper;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.bean.team.TeamListWrapper;
import com.msy.globalaccess.data.bean.team.TeamMemberBean;
import com.msy.globalaccess.data.bean.team.TeamMemberWrapper;
import com.msy.globalaccess.data.interactor.SearchResultInteractor;
import com.msy.globalaccess.exception.RxException;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ErrorUtils;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * Created by WuDebin on 2017/2/15.
 */

public class SearchResultInteractorImpl implements SearchResultInteractor {

    private Retrofit mRetrofit;

    private TeamServiceApi.TeamListApi teamListApi;
    private SettlementApi.SettlementListApi settlementApi;
    private SearchApi.TeamMemberSearch searchApi;

    @Inject
    SearchResultInteractorImpl(Retrofit mRetrofit) {
        this.mRetrofit = mRetrofit;
        this.teamListApi = mRetrofit.create(TeamServiceApi.TeamListApi.class);
        this.settlementApi = mRetrofit.create(SettlementApi.SettlementListApi.class);
        searchApi = mRetrofit.create(SearchApi.TeamMemberSearch.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Subscription loadTeamSearchResult(final RequestCallBack callBack, HashMap searchCondition, int page) {
        HashMap<String, String> param = new ParamsHelper.Builder().setMethod(TeamServiceApi.TeamListApi.method)
                .setParam(TeamServiceApi.TeamListApi.currentPageNum, page)
                .setParam(TeamServiceApi.TeamListApi.showNum, LIST_DATA_SIZE)
                .setParam(TeamServiceApi.TeamListApi.userId, App.userHelper.getUser().getUserId())
                //可选参数
                .setParams(searchCondition)
                .build().getParam();
        return teamListApi.getTeamList(NetUtil.getCacheControl(),param).compose(RxJavaUtils
                .<BaseBean<TeamListWrapper>>defaultSchedulers())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callBack.beforeRequest();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseBean<TeamListWrapper>, Observable<List<TeamListBean>>>() {
                    @Override
                    public Observable<List<TeamListBean>> call(BaseBean<TeamListWrapper> wrapper) {
                        if (wrapper.getStatus() == ResultCode.SUCCESS) {
                            return Observable.just(wrapper.getData().getTeamList());
                        } else {
                            String errorMessage = ErrorUtils.getErrorMessage(wrapper, R.string.internet_error);
                            return Observable.error(new RxException(wrapper.getStatus(), errorMessage));
                        }
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Subscription loadSettlementSearchResult(final RequestCallBack callBack, HashMap searchCondition, int page) {
        HashMap<String, String> param = new ParamsHelper.Builder().setMethod(SettlementApi.SettlementListApi.method)
                .setParam(SettlementApi.SettlementListApi.currentPageNum, page)
                .setParam(SettlementApi.SettlementListApi.showNum, LIST_DATA_SIZE)
                .setParam(SettlementApi.SettlementListApi.userId, App.userHelper.getUser().getUserId())
                //可选参数
                .setParams(searchCondition)
                .build().getParam();
        return settlementApi.getSettlementList(param).compose(RxJavaUtils
                .<BaseBean<SettlementListWrapper>>defaultSchedulers())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callBack.beforeRequest();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseBean<SettlementListWrapper>, Observable<List<SettlementListBean>>>() {
                    @Override
                    public Observable<List<SettlementListBean>> call(BaseBean<SettlementListWrapper> wrapper) {
                        if (wrapper.getStatus() == ResultCode.SUCCESS) {
                            return Observable.just(wrapper.getData().getTeamAuditList());
                        } else {
                            String errorMessage = ErrorUtils.getErrorMessage(wrapper, R.string.internet_error);
                            return Observable.error(new RxException(wrapper.getStatus(), errorMessage));
                        }
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }


    @Override
    public Subscription loadMemberSearchResult(final RequestCallBack callBack, HashMap searchCondition, int page) {
        HashMap<String, String> param = new ParamsHelper.Builder().setMethod(SearchApi.appTeamMemberSearch)
                .setParam(SearchApi.TravelcurrentPageNum, page)
                .setParam(SearchApi.TravelshowNum, LIST_DATA_SIZE)
                //可选参数
                .setParams(searchCondition)
                .build().getParam();
        return searchApi.TeamMember(param).compose(RxJavaUtils
                .<BaseBean<TeamMemberWrapper>>defaultSchedulers())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        callBack.beforeRequest();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseBean<TeamMemberWrapper>, Observable<List<TeamMemberBean>>>() {
                    @Override
                    public Observable<List<TeamMemberBean>> call(BaseBean<TeamMemberWrapper> wrapper) {
                        if (wrapper.getStatus() == ResultCode.SUCCESS) {
                            return Observable.just(wrapper.getData().getMemberList());
                        } else {
                            String errorMessage = ErrorUtils.getErrorMessage(wrapper, R.string.internet_error);
                            return Observable.error(new RxException(wrapper.getStatus(), errorMessage));
                        }
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }
}
