package com.msy.globalaccess.business.travelAgency.settlement.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.msy.globalaccess.business.adapter.SettlementListAdapter;
import com.msy.globalaccess.business.travelAgency.settlement.contract.ISubSettlementContract;
import com.msy.globalaccess.business.travelAgency.settlement.contract.impl.SubSettlementPresenterImpl;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.event.SettlementSuccessEvent;
import com.msy.globalaccess.listener.AppbarInteractor;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.behavior.FABBehavior;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description :
 */
public class SubSettlementFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, ISubSettlementContract.View {
    private static final String KEY_TYPE = "searchType";

    @Inject
    SubSettlementPresenterImpl mPresenter;

    @BindView(R.id.rvOnly)
    RecyclerView rvOnly;

    @BindView(R.id.fabSettlement)
    FloatingActionButton fabSettlement;//置顶按钮

    @BindView(R.id.swipeSettlement)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    @ContextLife("Activity")
    Context mContext;
    private SettlementListAdapter mAdapter;
    private LinearLayoutManager manager;
    private List<SettlementListBean> data = new ArrayList<>();//数据源
    private View notDataView, errorView, loaddingView;
    /**
     * 标志: true 不显示动画,反之显示
     */
    private boolean donotAnim = false;
    /**
     * 用于监听滑动即时变更指定按钮状态
     */
    private FABBehavior behavior = new FABBehavior();
    /*
     * tab类型
     */
    private String searchType = "";
    /**
     * 如果父类实现了该接口,那么fab点击后会回到顶部
     */
    private AppbarInteractor mAppbarInteractor;
    //当前数据的页数
    private int currentPage = 1;
    //目标数据的页数
    private int targetPage = currentPage;

    /**
     * 获取实例
     *
     * @param searchType 查询类型
     * @return 返回对象
     */
    public static SubSettlementFragment newInstance(String searchType) {
        SubSettlementFragment subSettlementFragment = new SubSettlementFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, searchType);
        subSettlementFragment.setArguments(bundle);
        return subSettlementFragment;
    }

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
    public void init(View view) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(KEY_TYPE)) {
            searchType = getArguments().getString(KEY_TYPE);
        } else {
            Logger.e("未获取到具体的结算列表类型");
        }

        if (mAppbarInteractor == null) {
            fabSettlement.setVisibility(View.GONE);
        }
        mSwipeRefreshLayout.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        manager = new LinearLayoutManager(rvOnly.getContext(), LinearLayoutManager.VERTICAL, false);
        rvOnly.setLayoutManager(manager);
        mAdapter = new SettlementListAdapter(data, R.layout.item_settlement_list);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this);
        rvOnly.setItemAnimator(new DefaultItemAnimator());
        rvOnly.setHasFixedSize(true);
        rvOnly.setAdapter(mAdapter);
        mAdapter.addData(data);

        notDataView = ViewHelper.getEmptyView(mContext, rvOnly);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        errorView = ViewHelper.getErrorView(mContext, rvOnly);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        loaddingView = ViewHelper.getLoadingView(mContext, rvOnly);
        mAdapter.setEmptyView(ViewHelper.getLoadingView(mContext, rvOnly));

        initListener();

        if (mPresenter != null) {
            mPresenter.loadLatestData();;
        }

        Subscription subscription = RxBus.getInstance().toObservable(SettlementSuccessEvent.class)
                .subscribe(new Action1<SettlementSuccessEvent>() {
                    @Override
                    public void call(SettlementSuccessEvent settlementSuccessEvent) {
                        mAdapter.updateData(searchType, settlementSuccessEvent);
                    }
                });
        rxBusCache.add(subscription);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_sub_settlement;
    }

    @OnClick(R.id.fabSettlement)
    public void clickFAB() {
        donotAnim = true;
        behavior.animateOuts(fabSettlement);
        if (mAppbarInteractor != null) {
            mAppbarInteractor.changeAppbar(true, false);
        }
        rvOnly.smoothScrollToPosition(0);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            if (mAdapter != null && mAdapter.getData().size() > 0) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            targetPage = 1;
            mAdapter.setEmptyView(loaddingView);
            //下拉刷新
            mAdapter.setEnableLoadMore(false);
            mPresenter.loadLatestData();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        targetPage++;
        mSwipeRefreshLayout.setEnabled(false);
        mPresenter.loadMore();
    }

    @Override
    public void handlerListData(@ResultCode int result, @RequestType int code, List<SettlementListBean> data) {
        updateData(result, code, data);
        setAdapterStatus(result);
        changePage(result);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return mPresenter;
    }

    @Override
    public void onListError(@ResultCode int resultCode, @RequestType int type, @Nullable String message) {
        if (isVisible && mAdapter.getData().size() > 0) {
            ToastUtils.showToast(message);
        }
        if (resultCode == ResultCode.SUCCESS_EMPTY || resultCode == ResultCode.SEARCH_NO_DATA) {
            mAdapter.setNewData(null);
        }
        setAdapterStatus(resultCode);
        changePage(resultCode);
    }

    @Override
    public String getDataType() {
        return searchType;
    }

    @Override
    public int getPage() {
        return targetPage;
    }

    @Override
    public void showProgress() {
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mAdapter != null && mAdapter.getData().size() > 0) {
//                    mSwipeRefreshLayout.setRefreshing(true);
//                }
//            }
//        });
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mSwipeRefreshLayout.setEnabled(true);
    }


    private void setAdapterStatus(@ResultCode int result) {
        switch (result) {
            case ResultCode.SUCCESS:
                mAdapter.loadMoreComplete();
                break;
            case ResultCode.SUCCESS_NO_MORE_DATA:
            case ResultCode.SEARCH_NO_DATA:
            case ResultCode.SUCCESS_EMPTY:
                mAdapter.setEmptyView(notDataView);
                mAdapter.loadMoreEnd();
                break;
            default:
                mAdapter.loadMoreFail();
                mAdapter.setEmptyView(errorView);
                break;
        }
    }

    private void updateData(@ResultCode int result, @RequestType int code, List<SettlementListBean> data) {
        if (result == ResultCode.SUCCESS || result == ResultCode.SUCCESS_NO_MORE_DATA || result == ResultCode
                .SUCCESS_EMPTY) {
            if (code == RequestType.TYPE_LATEST) {//下拉刷新
                mAdapter.setNewData(data);
            } else if (code == RequestType.TYPE_LOAD_MORE) {//上拉加载
                mAdapter.addData(data);
            }
        }
        this.data = mAdapter.getData();
    }

    private void initListener() {
        rvOnly.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SettlementDetailActivity.callActivity(getActivity(), data.get(position).getTeamAuditId(), searchType);
                getActivity().overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });

        rvOnly.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!donotAnim && (dy > 50 || manager.findFirstCompletelyVisibleItemPosition() == 0) && !behavior
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

    private void changePage(@ResultCode int result) {
        switch (result) {
            case ResultCode.SUCCESS:
            case ResultCode.SUCCESS_NO_MORE_DATA:
                currentPage = targetPage;
                break;
            default:
                targetPage = currentPage;
                break;
        }

    }
}
