package com.msy.globalaccess.business.travelAgency.search.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.DelegateTouristListAdapter;
import com.msy.globalaccess.business.adapter.SettlementListAdapter;
import com.msy.globalaccess.business.adapter.TeamListAdapter;
import com.msy.globalaccess.business.adapter.TouristInfoAdapter;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchResultContract;
import com.msy.globalaccess.business.travelAgency.search.contract.impl.SearchResultPresenterImpl;
import com.msy.globalaccess.business.travelAgency.settlement.ui.SettlementDetailActivity;
import com.msy.globalaccess.business.travelAgency.team.ui.TeamDetailActivity;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.SerializableHashMap;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.bean.team.TeamMemberBean;
import com.msy.globalaccess.event.RefreshDelegateListEvent;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.msy.globalaccess.event.SettlementSuccessEvent;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.recyclerview.WrapContentLinearLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.msy.globalaccess.business.travelAgency.settlement.ui.SettlementFragment.SETTLEMENT_DATA_TYPE_ALL;
import static com.msy.globalaccess.business.travelAgency.team.ui.TeamFragment.TEAM_DATA_TYPE_ALL;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_NO_DATA;
import static com.msy.globalaccess.common.enums.ResultCode.SUCCESS_NO_MORE_DATA;

/**
 * 查询结果界面
 * Created by WuDebin on 2017/2/15.
 */

@SuppressWarnings("deprecation")
public class SearchResultActivity extends BaseActivity implements SearchResultContract.View, SwipeRefreshLayout
        .OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    //查询类型
    public final static int TEAM_SEARCH = 1;//团队查询
    public final static int SETTLEMENT_SEARCH = 2;//结算查询
    public final static int TOURIST_SEARCH = 3;//游客查询
    public final static int DELEGATE_GUIDER_SEARCH = 4;//委派导游查询

    @BindView(R.id.swipe_search_result)
    SwipeRefreshLayout swipeSearchResult;
    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;
    @BindView(R.id.fab_search_result)
    FloatingActionButton fabSearchResult;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvTitleBarTitle;
    @Inject
    SearchResultPresenterImpl presenter;

    /**
     * 查询类型
     */
    @SEARCH_TYPE
    private int searchType = TEAM_SEARCH;

    /**
     * 查询条件
     */
    private HashMap<String, Object> searchCondition = new HashMap<>();

    /**
     * 适配器
     */
    private BaseQuickAdapter mAdapter;
    /**
     * 无数据布局、错误布局
     */
    private View notDataView, errorView;
    /**
     * 团队查询结果数据
     */
    private List<TeamListBean> TeamData = new ArrayList<>();
    /**
     * 结算查询结果数据
     */
    private List<SettlementListBean> SettlementData = new ArrayList<>();
    /**
     * 团队游客数据
     */
    private ArrayList<TeamMemberBean> teamMemberList = new ArrayList<>();
    /**
     * 委派导游
     */
    private ArrayList<TouristDelegateBean> mTouristDelegateList = new ArrayList<>();
    /**
     * 当前页数
     */
    private int page = 1;

    /**
     * @param ctx             context对象
     * @param searchType      查询类型   必须是 {@link SEARCH_TYPE} 中的一个
     * @param searchCondition 查询条件
     */
    public static <K, T> void callActivity(Context ctx, @SEARCH_TYPE int searchType, HashMap<K, T> searchCondition) {
        Intent intent = new Intent(ctx, SearchResultActivity.class);
        intent.putExtra("type", searchType);
        SerializableHashMap<K, T> serializableMap = new SerializableHashMap<>();
        serializableMap.setHashMap(searchCondition);
        intent.putExtra("searchCondition", serializableMap);
        ctx.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = presenter;
    }

    @Override
    protected void init() {
        initIntent();
        initData();
        initListen();
        getSearchResult();
        initRxBus();
    }

    /**
     * 获取查询条件
     */
    @SuppressWarnings("WrongConstant")
    public void initIntent() {
        Intent intent = getIntent();
        searchType = intent.getIntExtra("type", TEAM_SEARCH);
        SerializableHashMap serializableHashMap = (SerializableHashMap) intent.getSerializableExtra("searchCondition");
        searchCondition = serializableHashMap.getHashMap();
    }

    /**
     * 初始化
     */
    public void initData() {
        tvTitleBarTitle.setText("查询结果");
        tvTitleBarTitle.setVisibility(View.VISIBLE);
        WrapContentLinearLayoutManager manager = new WrapContentLinearLayoutManager(rvSearchResult.getContext(), LinearLayoutManager
                .VERTICAL, false);
        rvSearchResult.setLayoutManager(manager);
        rvSearchResult.setItemAnimator(new DefaultItemAnimator());
        rvSearchResult.setHasFixedSize(true);
        swipeSearchResult.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        swipeSearchResult.setOnRefreshListener(this);
        notDataView = LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.empty_view, (ViewGroup)
                rvSearchResult.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        errorView = LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.error_view, (ViewGroup)
                rvSearchResult.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        adjustAdapter();

        rvSearchResult.setAdapter(mAdapter);
        mAdapter.openLoadAnimation();
        //        mAdapter.setAutoLoadMoreSize(9);
        mAdapter.setOnLoadMoreListener(this);
        //        mAdapter.setEmptyView(notDataView);
    }

    /**
     * 根据不同的类型来匹配不同的适配器
     */
    private void adjustAdapter() {
        switch (searchType) {
            case TEAM_SEARCH:
                mAdapter = new TeamListAdapter(TeamData, R.layout.item_team_list, TEAM_DATA_TYPE_ALL);
                break;
            case SETTLEMENT_SEARCH:
                mAdapter = new SettlementListAdapter(SettlementData, R.layout.item_settlement_list);
                break;
            case TOURIST_SEARCH:
                mAdapter = new TouristInfoAdapter(R.layout.activity_check_tourist_info_item, teamMemberList);
                break;
            case DELEGATE_GUIDER_SEARCH:
                mAdapter = new DelegateTouristListAdapter(mTouristDelegateList,R.layout.item_delegate_tourist_list);
                break;
        }
    }

    public void getSearchResult() {
        swipeSearchResult.setRefreshing(true);
        presenter.loadSearchResult(searchCondition, searchType, 1);
    }

    @OnClick(R.id.imgBtnBack)
    void back() {
        this.finish();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
        swipeSearchResult.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        swipeSearchResult.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        SettlementData.clear();
        teamMemberList.clear();
        mTouristDelegateList.clear();
        TeamData.clear();
        page = 1;
        mAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvSearchResult.getParent());
        //下拉刷新
        mAdapter.setEnableLoadMore(false);
        presenter.loadSearchResult(searchCondition, searchType, page);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeSearchResult.setEnabled(false);
        presenter.loadMoreData(searchType, page);
    }

    @Override
    public void getTeamSearchResult(@ResultCode int result, @RequestType int code, List<TeamListBean> data) {
        updateData(result, code, data);
        setAdapterStatus(result);
    }

    @Override
    public void getSettlementSearchResult(@ResultCode int result, @RequestType int code, List<SettlementListBean>
            data) {
        updateData(result, code, data);
        setAdapterStatus(result);
    }

    @Override
    public void onError(@ResultCode int resultCode, @RequestType int type, @Nullable String message) {
        toast(message);
        updateData(resultCode, type, null);
        setAdapterStatus(resultCode);
    }

    @Override
    public void loadDelegateListResult(@ResultCode int result, @RequestType int code,List<TouristDelegateBean> data) {
        updateData(result, code, data);
        setAdapterStatus(result);
    }

    @SuppressLint("SwitchIntDef")
    private void setAdapterStatus(@ResultCode int result) {
        switch (result) {
            case ResultCode.SUCCESS:
                mAdapter.loadMoreComplete();
                break;
            case SUCCESS_NO_MORE_DATA:
                mAdapter.loadMoreEnd();
                break;
            case SEARCH_NO_DATA:
                changEmptyView();
                mAdapter.loadMoreEnd();
                break;
            default:
                mAdapter.loadMoreFail();
                mAdapter.setEmptyView(errorView);
                break;
        }
    }

    //判断数据的有效性
    private boolean checkDataValid() {
        switch (searchType) {
            case DELEGATE_GUIDER_SEARCH:
                return mTouristDelegateList.size() > 0;
            case SETTLEMENT_SEARCH:
                return SettlementData.size() > 0;
            case TEAM_SEARCH:
                return TeamData.size() > 0;
            case TOURIST_SEARCH:
                return teamMemberList.size() > 0;
        }
        return false;
    }

    /**
     * 变更EmptyView
     */
    private void changEmptyView() {
        if (!checkDataValid()) {
            mAdapter.setEmptyView(notDataView);
        }
    }

    /**
     * 提示toast
     *
     * @param message message
     */
    private void toast(String message) {
        if (checkDataValid()) {
            judge(message);
        } else {
            ToastUtils.showToast(message);
        }

        //        if (TEAM_SEARCH == searchType && TeamData.size() != 0) {
        //            judge(message);
        //        } else if (SETTLEMENT_SEARCH == searchType && SettlementData.size() != 0) {
        //            judge(message);
        //        } else if (TOURIST_SEARCH == searchType && teamMemberList.size() != 0) {
        //            judge(message);
        //        } else {
        //            ToastUtils.showToast(message);
        //        }
    }

    private void judge(String message) {
        if (!"查询无数据".equals(message)) {
            ToastUtils.showToast(message);
        }
    }

    private void updateData(@ResultCode int result, @RequestType int code, Object data) {
        if (data != null) {
            List<Object> listData = (List<Object>) data;
            if (result == ResultCode.SUCCESS || result == ResultCode.SUCCESS_NO_MORE_DATA) {
                if (code == RequestType.TYPE_LATEST) {//下拉刷新
                    mAdapter.setNewData(listData);
                    page = 2;
                } else if (code == RequestType.TYPE_LOAD_MORE) {//上拉加载
                    mAdapter.addData(listData);
                    page++;
                }
            } else if (result == SEARCH_NO_DATA) {
                if (code == RequestType.TYPE_LATEST) {//下拉刷新
                    mAdapter.setNewData(null);
                }
            }

            switch (searchType) {
                case DELEGATE_GUIDER_SEARCH:
                    mTouristDelegateList = (ArrayList<TouristDelegateBean>) mAdapter.getData();
                    break;
                case SETTLEMENT_SEARCH:
                    TeamData = mAdapter.getData();
                    break;
                case TEAM_SEARCH:
                    TeamData = mAdapter.getData();
                    break;
                case TOURIST_SEARCH:
                    teamMemberList = (ArrayList<TeamMemberBean>) mAdapter.getData();
                    break;
            }

            //            if (TEAM_SEARCH == searchType) {
            //                TeamData.addAll(mAdapter.getData());
            //            } else if (SETTLEMENT_SEARCH == searchType) {
            //                SettlementData.addAll(mAdapter.getData());
            //            } else if (TOURIST_SEARCH == searchType) {
            //                teamMemberList.addAll(mAdapter.getData());
            //            }
        }
    }

    private void initRxBus() {
        Subscription sub1 = RxBus.getInstance().toObservable(RefreshTeamStatusEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshTeamStatusEvent>() {
                    @Override
                    public void call(RefreshTeamStatusEvent refreshTeam) {
                        if (mAdapter instanceof TeamListAdapter) {
                            ((TeamListAdapter) mAdapter).converData(refreshTeam);
                        }

                    }
                });
        rxBusCache.add(sub1);
        Subscription sub2 = RxBus.getInstance().toObservable(SettlementSuccessEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SettlementSuccessEvent>() {
                    @Override
                    public void call(SettlementSuccessEvent refreshTeam) {
                        if (mAdapter instanceof SettlementListAdapter) {
                            ((SettlementListAdapter) mAdapter).updateData(SETTLEMENT_DATA_TYPE_ALL, refreshTeam);
                        }
                    }
                });
        rxBusCache.add(sub2);
        Subscription subscription = RxBus.getInstance().toObservable(RefreshDelegateListEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshDelegateListEvent>() {
                    @Override
                    public void call(RefreshDelegateListEvent refreshdelegate) {
                        ((DelegateTouristListAdapter)mAdapter).updateData(TEAM_DATA_TYPE_ALL,refreshdelegate);
                    }
                });
        rxBusCache.add(subscription);
    }

    /**
     * 监听
     */
    private void initListen() {
        rvSearchResult.addOnItemTouchListener(new OnItemClickListener() {

            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (searchType) {
                    case TEAM_SEARCH: {
                        TeamListBean data = (TeamListBean) adapter.getData().get(position);
                        String fragment_type = "";
                        if (data.getTeamStatus().equals("3")) {
                            fragment_type = "0";
                        } else if (data.getTeamStatus().equals("5")) {
                            fragment_type = "1";
                        } else if (data.getTeamStatus().equals("8")) {
                            fragment_type = "2";
                        }
                        TeamDetailActivity.callActivity(SearchResultActivity.this, data.getTeamId(), data
                                .getAccountStatus(), fragment_type);
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                        break;
                    }
                    case SETTLEMENT_SEARCH: {
                        SettlementListBean data = (SettlementListBean) adapter.getData().get(position);
                        SettlementDetailActivity.callActivity(SearchResultActivity.this, data.getTeamAuditId(),
                                SETTLEMENT_DATA_TYPE_ALL);
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                        break;
                    }
                    case TOURIST_SEARCH:
                        break;
                    case SearchResultActivity.DELEGATE_GUIDER_SEARCH:
                        TeamDetailActivity.callActivity(SearchResultActivity.this,mTouristDelegateList.get(position));
                        break;
                }
            }
        });
    }

    @IntDef({TEAM_SEARCH, SETTLEMENT_SEARCH, TOURIST_SEARCH, DELEGATE_GUIDER_SEARCH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SEARCH_TYPE {

    }
}
