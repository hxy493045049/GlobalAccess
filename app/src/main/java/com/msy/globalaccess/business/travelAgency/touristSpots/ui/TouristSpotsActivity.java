package com.msy.globalaccess.business.travelAgency.touristSpots.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.TouristSpotsAdapter;
import com.msy.globalaccess.business.travelAgency.team.modify.CreatScenicSpotActivity;
import com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.TouristSpotsContract;
import com.msy.globalaccess.business.travelAgency.touristSpots.contract.impl.TouristSpotsPresenterImpl;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;
import com.msy.globalaccess.widget.recyclerview.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity.TEAMLINEINFOBEAN;
import static com.msy.globalaccess.config.DataSetting.SPOT_DATA_SIZE;

/**
 * Created by pepys on 2017/3/17.
 * description:选择景点界面
 */
public class TouristSpotsActivity extends BaseActivity implements TouristSpotsContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tourist_spots_recycle)
    RecyclerView tourist_spots_recycle;

    @BindView(R.id.tourist_spots_swipeRefre)
    SwipeRefreshLayout tourist_spots_swipeRefre;

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;

    @BindView(R.id.ivToolbarRight)
    AppCompatImageView ivToolbarRight;
    /**
     * 是否是常用的
     */
    @BindView(R.id.tourist_spots_tv_isAlaway)
    TextView tourist_spots_tv_isAlaway;
    /**
     * 常用的区域
     */
    @BindView(R.id.tourist_spots_ll_isAlaway)
    LinearLayout tourist_spots_ll_isAlaway;
    /**
     * 选择城市
     */
    @BindView(R.id.tourist_spots_tv_city)
    TextView tourist_spots_tv_city;
    /**
     * 城市的区域
     */
    @BindView(R.id.tourist_spots_ll_city)
    LinearLayout tourist_spots_ll_city;
    /**
     * 景区等级
     */
    @BindView(R.id.tourist_spots_tv_level)
    TextView tourist_spots_tv_level;
    /**
     * 等级的区域
     */
    @BindView(R.id.tourist_spots_ll_level)
    LinearLayout tourist_spots_ll_level;

    @Inject
    TouristSpotsPresenterImpl presenter;
    /**
     * SmallDialog
     */
    private SmallDialog smallDialog;
    /**
     * 数据源
     */
    private ArrayList<ScenicListBean> lineListInfo = new ArrayList<>();
    private ScenicListBean scenicBean;
    /**
     * adapter
     */
    private TouristSpotsAdapter touristSpotsAdapter;
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
     * 景区级别
     */
    private String scenicLevel = "";
    /**
     * 是否常用diaolog
     */
    private WheelViewDialog<String> alawayDiaolog;
    private ArrayList<String> alawayList;
    /**
     * 城市diaolog
     */
    private WheelViewDialog cityDiaolog;
    private ArrayList<CityBean> mCityList = new ArrayList<>();
    /**
     * 等级diaolog
     */
    private WheelViewDialog levelDiaolog;
    private ArrayList<KeyMapBean> mLevelList = new ArrayList<>();
    /**
     * 是否是刷新
     */
    private boolean isRefresh = true;

    /**
     * 无数据界面
     */
    private View notDataView;
    /**
     * 行程信息
     */
    private TeamLineInfoBean teamLineInfoBean;
    /**
     * 已存在的景点
     */
    private ArrayList<TripScenicBean> tripSceniclist = new ArrayList<>();
    /**
     * 儿童数
     */
    private int areaChildrenAmout;
    /**
     * 成人数
     */
    private int areaAdultsAmount;

    public static final String TRIPSCENICLIST ="tripSceniclist";
    public static final String AREACHILDRENAMOUT ="areaChildrenAmout";
    public static final String AREAADULTSAMOUNT ="areaAdultsAmount";

    /**
     *  @param context
     * @param teamLineInfoBean
     * @param tripSceniclist
     * @param areaChildrenAmout 儿童数
     * @param areaAdultsAmount  成人数
     */
    public static void callActivity(Activity context, TeamLineInfoBean teamLineInfoBean, ArrayList<TripScenicBean> tripSceniclist, int areaChildrenAmout, int areaAdultsAmount) {
        Intent intent = new Intent(context, TouristSpotsActivity.class);
        intent.putExtra(TEAMLINEINFOBEAN,teamLineInfoBean);
        intent.putExtra(AREACHILDRENAMOUT,areaChildrenAmout);
        intent.putExtra(AREAADULTSAMOUNT,areaAdultsAmount);
        intent.putParcelableArrayListExtra(TRIPSCENICLIST,tripSceniclist);
        context.startActivityForResult(intent, TravelAttractionsActivity.REQUEST_CODE_ADD);
    }
    private void getIntentData(){
        if(getIntent().hasExtra(TEAMLINEINFOBEAN)){
            teamLineInfoBean = getIntent().getParcelableExtra(TEAMLINEINFOBEAN);
            cityName = teamLineInfoBean.getPlaceInfo();
            CityBean cityBean = new CityBean();
            cityBean.setCityName(cityName);
            mCityList.add(0,cityBean);
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
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_tourist_spots;
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
        initDiaologList();
        initData();
        initListener();

    }

    private void initData() {
        tvToolbarCenter.setText("选择景点");
        tvToolbarCenter.setVisibility(View.VISIBLE);
        ivToolbarRight.setVisibility(View.VISIBLE);
//        tourist_spots_tv_city.setText(cityName);

        smallDialog = new SmallDialog(this);
        tourist_spots_swipeRefre.setOnRefreshListener(this);
        tourist_spots_swipeRefre.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));

        touristSpotsAdapter = new TouristSpotsAdapter(this, R.layout.activity_tourist_spots_item, lineListInfo);
        touristSpotsAdapter.openLoadAnimation();
        touristSpotsAdapter.setOnLoadMoreListener(this, tourist_spots_recycle);

        tourist_spots_recycle.setLayoutManager(new GridLayoutManager(this, 3));
//        tourist_spots_recycle.addItemDecoration(new SpacesItemDecoration(24));
        tourist_spots_recycle.addItemDecoration(new ItemOffsetDecoration(12));
        tourist_spots_recycle.setAdapter(touristSpotsAdapter);
        notDataView = ViewHelper.getEmptyView(this, tourist_spots_recycle);

        alawayDiaolog = new WheelViewDialog<>(this, alawayList, tourist_spots_tv_isAlaway, new WheelViewDialog.onWheelViewPickedListener() {
            @Override
            public void onPicked(Object pickedItem, View view) {
                String isAlawayString = pickedItem.toString();
                tourist_spots_tv_isAlaway.setText(isAlawayString);
                if (isAlawayString.equals("常用")) {
                    isAlaway = "1";
                } else {
                    isAlaway = "";
                }
                onRefresh();
            }
        });

    }

    private void initListener() {
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });
        tourist_spots_recycle.addOnItemTouchListener(new OnItemClickListener() {
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
                    CreatScenicSpotActivity.callActivity(TouristSpotsActivity.this,true,tripScenic,teamLineInfoBean,areaChildrenAmout,areaAdultsAmount);
                }else{
                    ToastUtils.showLongToast("不能选重复的景点..");
                }
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

    @OnClick({R.id.tourist_spots_ll_isAlaway, R.id.tourist_spots_ll_city, R.id.tourist_spots_ll_level,R.id.ivToolbarRight})
    public void onTvClick(View view) {
        switch (view.getId()) {
            case R.id.tourist_spots_ll_isAlaway:
                alawayDiaolog.show();
                break;
            case R.id.tourist_spots_ll_city:
                if (cityDiaolog != null) {
                    cityDiaolog.show();
                }else{
                    presenter.getCityList();
                }
                break;
            case R.id.tourist_spots_ll_level:
                if (levelDiaolog != null) {
                    levelDiaolog.show();
                }else{
                    presenter.getLevel();
                }
                break;
            case R.id.ivToolbarRight:
                SearchSpotActivity.callActivity(TouristSpotsActivity.this,teamLineInfoBean,tripSceniclist,areaChildrenAmout,areaAdultsAmount,isAlaway,cityName,scenicLevel);
                break;
        }
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
                touristSpotsAdapter.setNewData(lineListInfo);
                tourist_spots_swipeRefre.setRefreshing(false);
                touristSpotsAdapter.setEmptyView(notDataView);
            } else {
                touristSpotsAdapter.loadMoreEnd();
            }
        } else {
            if (isRefresh) {
                lineListInfo = searchScenicList;
                touristSpotsAdapter.setNewData(searchScenicList);
                tourist_spots_swipeRefre.setRefreshing(false);
            } else {
                lineListInfo.addAll(searchScenicList);
                touristSpotsAdapter.addData(searchScenicList);
                touristSpotsAdapter.loadMoreComplete();
            }
        }
        if(searchScenicList.size()% SPOT_DATA_SIZE == 0){
            touristSpotsAdapter.setEnableLoadMore(true);
        }else{
            touristSpotsAdapter.setEnableLoadMore(false);
        }

    }

    @Override
    public void loadSpotsFailure() {
    }

    @Override
    public void loadCitySuccess(ArrayList<CityBean> cityList) {
        mCityList = cityList;
        if (cityDiaolog == null) {
            CityBean cityBean = new CityBean();
            cityBean.setCityName("全部");
            mCityList.add(0,cityBean);
            cityDiaolog = new WheelViewDialog<>(this, mCityList, tourist_spots_tv_city, new WheelViewDialog.onWheelViewPickedListener() {
                @Override
                public void onPicked(Object pickedItem, View view) {
                    cityName = ((CityBean) pickedItem).getCityName();
                    tourist_spots_tv_city.setText(cityName);
                    if(cityName.equals("全部")){
                        cityName = "";
                    }
                    onRefresh();
                }
            });
        }
    }

    @Override
    public void loadCityFailure(String msg) {
        ToastUtils.showLongToast(msg);
    }

    @Override
    public void loadLevelSuccess(List<KeyMapBean> keyMapList) {
        mLevelList = (ArrayList<KeyMapBean>) keyMapList;
        if (levelDiaolog == null) {
            KeyMapBean keyMap = new KeyMapBean();
            keyMap.setMapDesc("景点级别");
            keyMap.setMapId("0");
            keyMap.setMapKey("0");
            keyMap.setMapType("SCENLEVEL");
            keyMap.setMapValue("全部");
            mLevelList.add(0, keyMap);
            levelDiaolog = new WheelViewDialog<>(this, mLevelList, tourist_spots_tv_level, new WheelViewDialog.onWheelViewPickedListener() {
                @Override
                public void onPicked(Object pickedItem, View view) {
                    KeyMapBean keyMapBean = ((KeyMapBean) pickedItem);
                    tourist_spots_tv_level.setText(keyMapBean.getMapValue());
                    if (keyMapBean.getMapValue().equals("全部")) {
                        scenicLevel = "";
                    }else{
                        scenicLevel = keyMapBean.getMapKey();
                    }
                    onRefresh();
                }
            });
        }
    }
    @Override
    public void loadLevelFailure(String msg) {
        ToastUtils.showLongToast(msg);
    }
    private void initDiaologList() {
        alawayList = new ArrayList<>();
        alawayList.add("全部");
        alawayList.add("常用");
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
    public int currentPageNum() {
        return pageNumber;
    }

    @Override
    public int showNum() {
        return showNumber;
    }

    @Override
    public String scenicName() {
        return "";
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


}
