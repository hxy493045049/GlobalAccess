package com.msy.globalaccess.business.travelAgency.delegate.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.DelegateTouristListAdapter;
import com.msy.globalaccess.business.travelAgency.delegate.contract.IDelegateContract;
import com.msy.globalaccess.business.travelAgency.delegate.contract.impl.DelegatePresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.ui.TeamDetailActivity;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.event.RefreshDelegateListEvent;
import com.msy.globalaccess.listener.AppbarInteractor;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.behavior.FABBehavior;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.msy.globalaccess.R.id.rvOnly;
import static com.msy.globalaccess.business.travelAgency.delegate.ui.TouristDelegateFragment.TEAM_DATA_TYPE_ALL;
import static com.msy.globalaccess.business.travelAgency.delegate.ui.TouristDelegateFragment.TEAM_DATA_TYPE_PROCRSSING;
import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * Created by pepys on 2017/7/3.
 */

public class SubTouristDelegateFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,IDelegateContract.View,BaseQuickAdapter.RequestLoadMoreListener{

    private static final String KEY_TYPE = "searchType";

    @Inject
    DelegatePresenterImpl mPresenter;

    @BindView(rvOnly)
    RecyclerView recycleView;

    @BindView(R.id.fabSettlement)
    FloatingActionButton fabSettlement;//置顶按钮

    @BindView(R.id.swipeSettlement)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    @ContextLife("Activity")
    Context mContext;
    /**
     * 是否是刷新
     */
    private boolean isRefresh = true;
    /**
     * 标志: true 不显示动画,反之显示
     */
    private boolean donotAnim = false;
    /**
     * 用于监听滑动即时变更指定按钮状态
     */
    private FABBehavior behavior = new FABBehavior();
    /**
     * 如果父类实现了该接口,那么fab点击后会回到顶部
     */
    private AppbarInteractor mAppbarInteractor;
    /**
     * recycle LinearLayoutManager
     */
    private LinearLayoutManager linearLayoutManager;
    /**
     * 常用view
     */
    private View notDataView, errorView, loaddingView;
    /**
     * tab类型
     */
    private String searchType = "";
    /**
     * 当前数据的页数
     */
    private int currentPage = 1;
    /**
     * 数据adapter
     */
    private DelegateTouristListAdapter delegateTouristListAdapter;


    /**
     * 获取实例
     * @param searchType 查询类型
     * @return 返回对象
     */
    public static SubTouristDelegateFragment newInstance(String searchType) {
        SubTouristDelegateFragment subTouristDelegateFragment = new SubTouristDelegateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, searchType);
        subTouristDelegateFragment.setArguments(bundle);
        return subTouristDelegateFragment;
    }
//    item_delegate_tourist_list.xml
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppbarInteractor) {
            mAppbarInteractor = (AppbarInteractor) context;
        }
    }

    @Override
    public void initInjector() {
        getFragmentComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sub_settlement;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    @Override
    public void init(View view) {
        getArgument();
        if (mAppbarInteractor == null) {
            fabSettlement.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        linearLayoutManager = new LinearLayoutManager(recycleView.getContext(), LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(linearLayoutManager);

        delegateTouristListAdapter = new DelegateTouristListAdapter(new ArrayList<TouristDelegateBean>(),R.layout.item_delegate_tourist_list);
        delegateTouristListAdapter.openLoadAnimation();

        delegateTouristListAdapter.setOnLoadMoreListener( this,recycleView);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(delegateTouristListAdapter);

        notDataView = ViewHelper.getEmptyView(mContext, recycleView);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        errorView = ViewHelper.getErrorView(mContext, recycleView);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        loaddingView = ViewHelper.getLoadingView(mContext, recycleView);
        delegateTouristListAdapter.setEmptyView(loaddingView);

        initListener();
        mPresenter.loadDelegate();
    }

    private void getArgument() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_TYPE)) {
            searchType = getArguments().getString(KEY_TYPE);
            if(searchType.equals(TEAM_DATA_TYPE_ALL) || searchType.equals(TEAM_DATA_TYPE_PROCRSSING)){
                initRxBus();
            }
        } else {
            Logger.e("未获取到具体的结算列表类型");
        }
    }
    private void initRxBus() {
        Subscription subscription = RxBus.getInstance().toObservable(RefreshDelegateListEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshDelegateListEvent>() {
                    @Override
                    public void call(RefreshDelegateListEvent refreshdelegate) {
                        delegateTouristListAdapter.updateData(searchType,refreshdelegate);
                    }
                });
        rxBusCache.add(subscription);
    }
    @OnClick({R.id.fabSettlement})
    public void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.fabSettlement:
                donotAnim = true;
                behavior.animateOuts(fabSettlement);
                if (mAppbarInteractor != null) {
                    mAppbarInteractor.changeAppbar(true, false);
                }
                recycleView.smoothScrollToPosition(0);
                break;
        }

    }
    private void initListener() {
        recycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeamDetailActivity.callActivity(mContext,delegateTouristListAdapter.getData().get(position));
            }
        });

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!donotAnim && (dy > 50 || linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) && !behavior
                        .upAnimFlag && fabSettlement.getVisibility() == View.VISIBLE) {
                    behavior.animateOuts(fabSettlement);
                } else if (!donotAnim && dy < -50 && !behavior.downAnimFlag && fabSettlement.getVisibility() == View
                        .INVISIBLE) {
                    behavior.animateIn(fabSettlement);
                }
                donotAnim = false;
            }
        });
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        if (mPresenter != null) {
            if (delegateTouristListAdapter != null && delegateTouristListAdapter.getData().size() > 0) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            currentPage = 1;
            delegateTouristListAdapter.setEmptyView(notDataView);
            //下拉刷新
            delegateTouristListAdapter.setEnableLoadMore(false);
            mPresenter.loadDelegate();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        isRefresh = false;
        currentPage++;
        mSwipeRefreshLayout.setRefreshing(false);
        mPresenter.loadDelegate();
    }

    @Override
    public void delegateSuccess(ArrayList<TouristDelegateBean> touristDelegateList) {
        if (null == touristDelegateList || touristDelegateList.size() == 0) {
            if (isRefresh) {
                delegateTouristListAdapter.setNewData(touristDelegateList);
                mSwipeRefreshLayout.setRefreshing(false);
                delegateTouristListAdapter.setEmptyView(notDataView);
            } else {
                delegateTouristListAdapter.loadMoreEnd();
            }
        } else {
            if (isRefresh) {
                delegateTouristListAdapter.setNewData(touristDelegateList);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                delegateTouristListAdapter.addData(touristDelegateList);
                delegateTouristListAdapter.loadMoreComplete();
            }
        }
        if (touristDelegateList.size() % LIST_DATA_SIZE == 0) {
            delegateTouristListAdapter.setEnableLoadMore(true);
        } else {
            delegateTouristListAdapter.setEnableLoadMore(false);
        }


    }

    @Override
    public void delegateFailure() {

    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    @Override
    public String getOpStatus() {
        return searchType;
    }

    @Override
    public int getPageNum() {
        return currentPage;
    }



}
