package com.msy.globalaccess.business.travelAgency.team.ui;

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
import com.msy.globalaccess.business.adapter.TeamListAdapter;
import com.msy.globalaccess.business.travelAgency.team.contract.ISubTeamContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.SubTeamPresenterImpl;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description :
 */

public class SubTeamFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener, ISubTeamContract.View {
    private static final String KEY_TYPE = "searchType";
    @BindView(R.id.swipeTeam)
    SwipeRefreshLayout mSwipeTeam;
    @Inject
    @ContextLife("Activity")
    Context mContext;
    @Inject
    SubTeamPresenterImpl mPresenter;
    @BindView(R.id.rvOnly)
    RecyclerView rvOnly;
    @BindView(R.id.fabTeam)
    FloatingActionButton fabTeam;
    private List<TeamListBean> data = new ArrayList<>();
    private AppbarInteractor mAppbarInteractor;


    private String searchType;

    private FABBehavior behavior = new FABBehavior();
    private TeamListAdapter mAdapter;
    private View notDataView, errorView, loaddingView;
    private boolean donotAnim = false;
    private LinearLayoutManager manager;

    private int currentPage = 1;//当前页面的页面
    private int targetPage = currentPage;//目标数据的页面

    /**
     * 构建一个fragment实例
     *
     * @param searchType 是否表示"全部"
     */
    public static SubTeamFragment newInstance(String searchType) {
        SubTeamFragment fragment = new SubTeamFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, searchType);
        fragment.setArguments(bundle);
        return fragment;
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
            searchType = bundle.getString(KEY_TYPE);
        } else {
            Logger.e("无法获取团队列表数据类型");
        }

        if (mAppbarInteractor == null) {
            fabTeam.setVisibility(View.GONE);
        }
        mSwipeTeam.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        mSwipeTeam.setOnRefreshListener(this);

        manager = new LinearLayoutManager(rvOnly.getContext(), LinearLayoutManager.VERTICAL, false);
        rvOnly.setLayoutManager(manager);
        mAdapter = new TeamListAdapter(data, R.layout.item_team_list, searchType);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this);
        rvOnly.setItemAnimator(new DefaultItemAnimator());
        rvOnly.setHasFixedSize(true);
        rvOnly.setAdapter(mAdapter);
        mAdapter.addData(data);

        initListener();

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
        mAdapter.setAutoLoadMoreSize(0);
        mAdapter.setEmptyView(ViewHelper.getLoadingView(mContext, rvOnly));

        initRxBus();
        if (mPresenter != null) {
            mPresenter.loadLatestData();
        }
    }

    private void initRxBus() {
        Subscription subscription = RxBus.getInstance().toObservable(RefreshTeamStatusEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshTeamStatusEvent>() {
                    @Override
                    public void call(RefreshTeamStatusEvent refreshTeam) {
                        mAdapter.converData(refreshTeam);
                    }
                });
        rxBusCache.add(subscription);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sub_team;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return mPresenter;
    }


    @OnClick(R.id.fabTeam)
    public void clickFAB() {
        donotAnim = true;
        behavior.animateOuts(fabTeam);
        if (mAppbarInteractor != null) {
            mAppbarInteractor.changeAppbar(true, false);
        }
        rvOnly.smoothScrollToPosition(0);
    }

    @Override
    public void onLoadMoreRequested() {
        targetPage++;
        mSwipeTeam.setEnabled(false);
        mPresenter.loadMore();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            if (mAdapter != null && mAdapter.getData().size() > 0) {
                mSwipeTeam.setRefreshing(true);
            }
            targetPage = 1;
            mAdapter.setEmptyView(loaddingView);
            //下拉刷新
            mAdapter.setEnableLoadMore(false);
            mPresenter.loadLatestData();
        }
    }

    @Override
    public void showProgress() {
//        mSwipeTeam.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mAdapter != null && mAdapter.getData().size() > 0) {
//                    mSwipeTeam.setRefreshing(true);
//                }
//            }
//        });
    }

    @Override
    public void onError(@ResultCode int resultCode, @RequestType int type, @Nullable String message) {
        if (isVisible && mAdapter.getData().size() > 0) {
            ToastUtils.showToastBottom(message);
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
    public void hideProgress() {
        mSwipeTeam.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mSwipeTeam.setEnabled(true);
    }

    @Override
    public void handlerListData(@ResultCode int result, @RequestType int code, List<TeamListBean> data) {
        //追踪了adapter源码  如果将updateData和setAdapterStatus调换顺序,会导致首页数据不足一页时,执行两次加载
        updateData(result, code, data);
        setAdapterStatus(result);
        changePage(result);
    }

    private void initListener() {
        rvOnly.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeamListBean data = (TeamListBean) adapter.getData().get(position);
                TeamDetailActivity.callActivity(getActivity(), data.getTeamId(), data.getAccountStatus(), searchType);
                getActivity().overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });

        rvOnly.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!donotAnim && (dy > 50 || manager.findFirstCompletelyVisibleItemPosition() == 0) && !behavior
                        .upAnimFlag && fabTeam.getVisibility() == View.VISIBLE) {
                    behavior.animateOuts(fabTeam);
                } else if (!donotAnim && dy < -50 && !behavior.downAnimFlag && fabTeam.getVisibility() == View
                        .INVISIBLE) {
                    behavior.animateIn(fabTeam);
                }
                donotAnim = false;
            }
        });
    }

    private void setAdapterStatus(@ResultCode int result) {
        switch (result) {
            case ResultCode.SUCCESS:
                mAdapter.loadMoreComplete();
                break;
            case ResultCode.SUCCESS_NO_MORE_DATA:
            case ResultCode.SEARCH_NO_DATA:
            case ResultCode.SUCCESS_EMPTY:
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(notDataView);
                break;
            default:
                mAdapter.loadMoreFail();
                mAdapter.setEmptyView(errorView);
                break;
        }
    }

    private void updateData(@ResultCode int result, @RequestType int code, List<TeamListBean> data) {
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
