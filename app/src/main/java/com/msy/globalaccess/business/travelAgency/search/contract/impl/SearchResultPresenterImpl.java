package com.msy.globalaccess.business.travelAgency.search.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchResultContract;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.data.interactor.impl.GuiderInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.SearchResultInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * 查询结果Impl
 * Created by WuDebin on 2017/2/15.
 */

public class SearchResultPresenterImpl extends BasePresenterImpl<SearchResultContract.View> implements
        SearchResultContract.Presenter {

    /**
     * 请求实体类
     */
    private SearchResultInteractorImpl searchResultInteractor;

    private GuiderInteractorImpl delegateInteractor;

    private HashMap<String, Object> searchCondition;

    @Inject
    SearchResultPresenterImpl(SearchResultInteractorImpl searchResultInteractor,GuiderInteractorImpl delegateInteractor) {
        this.searchResultInteractor = searchResultInteractor;
        this.delegateInteractor = delegateInteractor;
    }


    @Override
    public void onStart() {

    }


    @Override
    public void loadSearchResult(HashMap<String, Object> searchCondition, int type, int page) {
        this.searchCondition = searchCondition;
        switch (type) {
            case SearchResultActivity.TEAM_SEARCH:
                loadTeamSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.SETTLEMENT_SEARCH:
                loadSettelementSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.TOURIST_SEARCH:
                loadMemberSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.DELEGATE_GUIDER_SEARCH:
                loadApprovalDelegateTourist(searchCondition,page,RequestType.TYPE_LATEST);
                break;
        }
    }

    @Override
    public void loadMoreData(int type, int page) {
        switch (type) {
            case SearchResultActivity.TEAM_SEARCH:
                MoreTeamSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.SETTLEMENT_SEARCH:
                MoreSettelementSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.TOURIST_SEARCH:
                MoreMemberSearchResult(searchCondition, page);
                break;
            case SearchResultActivity.DELEGATE_GUIDER_SEARCH:
                loadApprovalDelegateTourist(searchCondition,page, RequestType.TYPE_LOAD_MORE);
                break;
        }
    }

    /**
     * 团队首次查询
     *
     * @param searchCondition
     */
    public void loadTeamSearchResult(HashMap<String, Object> searchCondition, int page) {
        this.searchCondition = searchCondition;
        Subscription subscription = searchResultInteractor.loadTeamSearchResult(new RequestCallBackAdapter<List<TeamListBean>>() {

            @Override
            public void success(List<TeamListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getTeamSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LATEST, data);
                } else {
                    mView.getTeamSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LATEST, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LATEST, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }

    /**
     * 团队加载更多查询
     *
     * @param searchCondition
     */
    public void MoreTeamSearchResult(HashMap<String, Object> searchCondition, int page) {
        Subscription subscription = searchResultInteractor.loadTeamSearchResult(new RequestCallBackAdapter<List<TeamListBean>>() {

            @Override
            public void success(List<TeamListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getTeamSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LOAD_MORE, data);
                } else {
                    mView.getTeamSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LOAD_MORE, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LOAD_MORE, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }

    /**
     * 结算首次查询
     *
     * @param searchCondition
     */
    public void loadSettelementSearchResult(HashMap<String, Object> searchCondition, int page) {
        Subscription subscription = searchResultInteractor.loadSettlementSearchResult(new RequestCallBackAdapter<List<SettlementListBean>>() {

            @Override
            public void success(List<SettlementListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getSettlementSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LATEST, data);
                } else {
                    mView.getSettlementSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LATEST, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LATEST, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }

    /**
     * 结算加载更多查询
     *
     * @param searchCondition
     */
    public void MoreSettelementSearchResult(HashMap<String, Object> searchCondition, int page) {
        Subscription subscription = searchResultInteractor.loadSettlementSearchResult(new RequestCallBackAdapter<List<SettlementListBean>>() {

            @Override
            public void success(List<SettlementListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getSettlementSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LOAD_MORE, data);
                } else {
                    mView.getSettlementSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LOAD_MORE, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LOAD_MORE, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }


    /**
     * 游客首次查询
     *
     * @param searchCondition
     */
    public void loadMemberSearchResult(HashMap<String, Object> searchCondition, int page) {
        Subscription subscription = searchResultInteractor.loadMemberSearchResult(new RequestCallBackAdapter<List<TeamListBean>>() {

            @Override
            public void success(List<TeamListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getTeamSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LATEST, data);
                } else {
                    mView.getTeamSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LATEST, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LATEST, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }

    /**
     * 游客更多次查询
     *
     * @param searchCondition
     */
    public void MoreMemberSearchResult(HashMap<String, Object> searchCondition, int page) {
        Subscription subscription = searchResultInteractor.loadMemberSearchResult(new RequestCallBackAdapter<List<TeamListBean>>() {

            @Override
            public void success(List<TeamListBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.getTeamSearchResult(ResultCode.SUCCESS, RequestType.TYPE_LOAD_MORE, data);
                } else {
                    mView.getTeamSearchResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LOAD_MORE, data);
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode, RequestType.TYPE_LOAD_MORE, errorMsg);
            }
        }, searchCondition, page);
        cacheSubscription.add(subscription);
    }

    /**
     * 查询导游委派
     */
    public void loadApprovalDelegateTourist(HashMap<String, Object> searchCondition,int page ,@RequestType final int type ) {
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(GuiderApi.DelegateApi.METHOD)
                .setParams(searchCondition)
                .setParam(GuiderApi.DelegateApi.CURRENTPAGENUM, page)
                .build().getParam();
        Subscription subscription = delegateInteractor.loadDelegate(new RequestCallBackAdapter<List
                <TouristDelegateBean>>() {
            @Override
            public void success(List<TouristDelegateBean> data) {
                if (data.size() == LIST_DATA_SIZE) {
                    mView.loadDelegateListResult(ResultCode.SUCCESS, RequestType.TYPE_LOAD_MORE, new ArrayList<>(data));
                } else if (data.size() ==0 ){
                    mView.loadDelegateListResult(ResultCode.SEARCH_NO_DATA, RequestType.TYPE_LOAD_MORE, new ArrayList<>(data));
                }else{
                    mView.loadDelegateListResult(ResultCode.SUCCESS_NO_MORE_DATA, RequestType.TYPE_LOAD_MORE, new ArrayList<>(data));
                }
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.onError(resultCode,type,errorMsg);
            }
        }, param);
        cacheSubscription.add(subscription);
    }
}
