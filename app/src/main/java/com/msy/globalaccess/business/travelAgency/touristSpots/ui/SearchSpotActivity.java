package com.msy.globalaccess.business.travelAgency.touristSpots.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.TouristSpotsAdapter;
import com.msy.globalaccess.business.travelAgency.team.modify.CreatScenicSpotActivity;
import com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.SearchSpotContract;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.impl.SearchSpotsPresenterImpl;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.ClearEditText;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.recyclerview.ItemOffsetDecoration;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity.TEAMLINEINFOBEAN;
import static com.msy.globalaccess.business.travelAgency.touristSpots.ui.TouristSpotsActivity.AREAADULTSAMOUNT;
import static com.msy.globalaccess.business.travelAgency.touristSpots.ui.TouristSpotsActivity.AREACHILDRENAMOUT;
import static com.msy.globalaccess.business.travelAgency.touristSpots.ui.TouristSpotsActivity.TRIPSCENICLIST;
import static com.msy.globalaccess.config.DataSetting.SPOT_DATA_SIZE;

public class SearchSpotActivity extends BaseActivity implements SearchSpotContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    SearchSpotsPresenterImpl presenter;

    @BindView(R.id.search_spots_swipeRefre)
    SwipeRefreshLayout search_spots_swipeRefre;

    @BindView(R.id.search_spots_recycle)
    RecyclerView search_spots_recycle;

    @BindView(R.id.auto_search_view)
    ClearEditText auto_search_view;

    /**
     * 当前第几页
     */
    private int pageNumber = 1;
    /**
     * 一页展示多少条
     */
    private int showNumber = SPOT_DATA_SIZE;
    /**
     * 是否常用 ""不常用 1常用
     */
    private String isAlaway = "";
    /**
     * 城市名称
     */
    private String cityName = "";
    /**
     * 景区名称
     */
    private String scenicName = "";
    /**
     * 景区级别
     */
    private String scenicLevel = "";
    /**
     * loading
     */
    private SmallDialog smallDialog;
    /**
     * adapter
     */
    private TouristSpotsAdapter searchSpotsAdapter;
    /**
     * 数据源
     */
    private ArrayList<ScenicListBean> lineListInfo = new ArrayList<>();
    private ScenicListBean scenicBean;
    /**
     * 无数据界面
     */
    private View notDataView;
    /**
     * 是否是刷新
     */
    private boolean isRefresh = true;
    /**
     * 已存在的景点
     */
    private ArrayList<TripScenicBean> tripSceniclist = new ArrayList<>();
    /**
     * 行程信息
     */
    private TeamLineInfoBean teamLineInfoBean;
    /**
     * 儿童数
     */
    private int areaChildrenAmout;
    /**
     * 成人数
     */
    private int areaAdultsAmount;

    public static final String IS_ALAWAY = "isAlaway";
    public static final String CITY_NAME = "cityName";
    public static final String SCENIC_LEVEL= "scenicLevel";

    /**
     *
     * @param context
     * @param teamLineInfoBean
     * @param tripSceniclist
     * @param areaChildrenAmout 儿童数
     * @param areaAdultsAmount  成人数
     * @param isAlaway
     * @param cityName
     * @param scenicLevel
     */
    public static void callActivity(Activity context, TeamLineInfoBean teamLineInfoBean, ArrayList<TripScenicBean> tripSceniclist, int areaChildrenAmout,
                                    int areaAdultsAmount, String isAlaway, String cityName, String scenicLevel) {
        Intent intent = new Intent(context, SearchSpotActivity.class);
        intent.putExtra(TEAMLINEINFOBEAN,teamLineInfoBean);
        intent.putExtra(AREACHILDRENAMOUT,areaChildrenAmout);
        intent.putExtra(AREAADULTSAMOUNT,areaAdultsAmount);
        intent.putExtra(IS_ALAWAY,isAlaway);
        intent.putExtra(CITY_NAME,cityName);
        intent.putExtra(SCENIC_LEVEL,scenicLevel);
        intent.putParcelableArrayListExtra(TRIPSCENICLIST,tripSceniclist);
        context.startActivityForResult(intent, TravelAttractionsActivity.REQUEST_CODE_ADD);
    }

    private void getIntentData(){
        if(getIntent().hasExtra(TEAMLINEINFOBEAN)){
            teamLineInfoBean = getIntent().getParcelableExtra(TEAMLINEINFOBEAN);
        }
        if(getIntent().hasExtra(TRIPSCENICLIST)){
            tripSceniclist = getIntent().getParcelableArrayListExtra(TRIPSCENICLIST);
        }
        if(getIntent().hasExtra(AREACHILDRENAMOUT)){
            areaChildrenAmout = getIntent().getIntExtra(AREACHILDRENAMOUT,0);
        }
        if(getIntent().hasExtra(AREAADULTSAMOUNT)){
            areaAdultsAmount = getIntent().getIntExtra(AREAADULTSAMOUNT,0);
        }
        if(getIntent().hasExtra(IS_ALAWAY)){
            isAlaway = getIntent().getStringExtra(IS_ALAWAY);
        }
        if(getIntent().hasExtra(CITY_NAME)){
            cityName = getIntent().getStringExtra(CITY_NAME);
        }
        if(getIntent().hasExtra(SCENIC_LEVEL)){
            scenicLevel =  getIntent().getStringExtra(SCENIC_LEVEL);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_spot;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        getIntentData();
        initData();
        initListener();
    }

    private void initData(){
        smallDialog = new SmallDialog(this);
        search_spots_swipeRefre.setOnRefreshListener(this);
        search_spots_swipeRefre.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));

        searchSpotsAdapter = new TouristSpotsAdapter(this, R.layout.activity_tourist_spots_item, lineListInfo);
        searchSpotsAdapter.openLoadAnimation();
        searchSpotsAdapter.setOnLoadMoreListener(this, search_spots_recycle);

        search_spots_recycle.setLayoutManager(new GridLayoutManager(this, 3));
        search_spots_recycle.addItemDecoration(new ItemOffsetDecoration(12));
        search_spots_recycle.setAdapter(searchSpotsAdapter);
        notDataView = ViewHelper.getEmptyView(this, search_spots_recycle);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNumber = 1;
        presenter.getSpotsList();
    }

    @Override
    public void onLoadMoreRequested() {
        isRefresh = false;
        pageNumber += 1;
        presenter.getSpotsList();
    }

    @Override
    public void loadSpotsSuccess(ArrayList<ScenicListBean> searchScenicList) {
        if (null == searchScenicList || searchScenicList.size() == 0) {
            if (isRefresh) {
                lineListInfo = new ArrayList<>();
                searchSpotsAdapter.setNewData(lineListInfo);
                search_spots_swipeRefre.setRefreshing(false);
                searchSpotsAdapter.setEmptyView(notDataView);
            } else {
                searchSpotsAdapter.loadMoreEnd();
            }
        } else {
            if (isRefresh) {
                lineListInfo = searchScenicList;
                searchSpotsAdapter.setNewData(searchScenicList);
                search_spots_swipeRefre.setRefreshing(false);
            } else {
                lineListInfo.addAll(searchScenicList);
                searchSpotsAdapter.addData(searchScenicList);
                searchSpotsAdapter.loadMoreComplete();
            }
        }
        if(searchScenicList.size() % SPOT_DATA_SIZE == 0){
            searchSpotsAdapter.setEnableLoadMore(true);
        }else{
            searchSpotsAdapter.setEnableLoadMore(false);
        }
    }

    @Override
    public void loadSpotsFailure() {

    }

    @OnClick({R.id.search_right_btn})
    public void viewOnClick(View view){
        switch (view.getId()){
            case R.id.search_right_btn:
                finish();
                break;
        }
    }

    private void initListener() {
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        auto_search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                scenicName = s.toString();
            }
        });
        search_spots_recycle.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                scenicBean = lineListInfo.get(position);
                if(!checkTouristSpotsRepeat(scenicBean.getScenicId())){
                    TripScenicBean tripScenic = new TripScenicBean();
                    tripScenic.setScenicId(scenicBean.getScenicId());
                    tripScenic.setTripName(scenicBean.getScenicName());
                    tripScenic.setIsOrderTicket(null == scenicBean.getIsOrderTicket()  ? "0" :scenicBean.getIsOrderTicket());
                    tripScenic.setPtripScenicId(scenicBean.getPscenicId());
                    tripScenic.setAuditType(null == scenicBean.getIsAcc() ? "0" :scenicBean.getIsAcc());
                    CreatScenicSpotActivity.callActivity(SearchSpotActivity.this,true,tripScenic,teamLineInfoBean,areaChildrenAmout,areaAdultsAmount);
                }else{
                    ToastUtils.showLongToast("不能选重复的景点..");
                }
            }
        });

        auto_search_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.getSpotsList();
                }
                return false;
            }
        });
    }

    /**
     * 检查选中景点是否和已经添加景点重复
     * @param scenicId
     * @return
     */
    private boolean checkTouristSpotsRepeat(String scenicId){
        for (TripScenicBean tripScenic:tripSceniclist ) {
            if(tripScenic.getScenicId().equals(scenicId)){
                return true;
            }
        }
        return false;
    }
    @Override
    public int currentPageNum() {
        return pageNumber;
    }

    @Override
    public int showNum() {
        return showNumber;
    }

    @Override
    public String scenicName() {
        return scenicName;
    }

    @Override
    public String cityName() {
        return cityName;
    }

    @Override
    public String scenicLevel() {
        return scenicLevel;
    }

    @Override
    public String isAlaway() {
        return isAlaway;
    }

    @Override
    public void showProgress() {
        if(!isRefresh){
            smallDialog.shows();
        }

    }

    @Override
    public void hideProgress() {
        if(!isRefresh){
            smallDialog.dismisss();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

}
