package com.msy.globalaccess.business.travelAgency.team.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.TeamDetailLineAdapter;
import com.msy.globalaccess.business.travelAgency.team.contract.TeamDetailContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.TeamDetailPresenterImpl;
import com.msy.globalaccess.business.travelAgency.team.modify.ChangeTravelMainActivity;
import com.msy.globalaccess.data.bean.RoleBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.team.TeamDetailTimeLineBean;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.data.bean.tourism.TouristsInfoBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.event.RefreshDelegateListEvent;
import com.msy.globalaccess.event.RefreshTeamBadgeEvent;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.customView.CustomViewPager;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.QRCodeDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.StringUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by pepys on 2017/2/6.
 * description: 团队详情
 */
public class TeamDetailActivity extends BaseActivity implements TeamDetailContract.View, SwipeRefreshLayout.OnRefreshListener {
    public static String TEAMID = "teamID";
    public static String ACCOUNT_STATUS = "accountStatus";
    public static String TOURISTDELEGATEBEAN = "touristDelegateBean";
    /**
     * 从哪个Fragment进来的。
     */
    public static String FRAGMENT_TYPE = "fragment_Type";
    /**
     * tablayout
     */
    @BindView(R.id.tab_team_detail)
    TabLayout tab_team_detail;
    /**
     * viewpage
     */
    @BindView(R.id.vp_team_detail)
    CustomViewPager vp_team_detail;
    /**
     * 下拉刷新
     */
    @BindView(R.id.team_detail_swipeRefreshLayout)
    SwipeRefreshLayout team_detail_swipeRefreshLayout;
    /**
     * 时间轴列表
     */
    @BindView(R.id.recycler_team_detail)
    RecyclerView recycler_team_detail;
    @BindView(R.id.team_detail_nestedscrollview)
    NestedScrollView team_detail_nestedscrollview;
    @BindView(R.id.team_detail_fab)
    FloatingActionButton team_detail_fab;
    /**
     * 团队编号
     */
    @BindView(R.id.detail_title_tv_team_number)
    TextView detail_title_tv_team_number;
    /**
     * 团队状态
     */
    @BindView(R.id.detail_tv_team_status)
    TextView detail_tv_team_status;
    /**
     * 团款预付款
     */
    @BindView(R.id.team_detail_tv_prePayMoney)
    TextView team_detail_tv_prePayMoney;
    /**
     * 团款已支付
     */
    @BindView(R.id.team_detail_tv_payMoney)
    TextView team_detail_tv_payMoney;
    /**
     * 团款已退款
     */
    @BindView(R.id.team_detail_tv_backMoney)
    TextView team_detail_tv_backMoney;
    /**
     * 团款余额
     */
    @BindView(R.id.team_detail_tv_remainMoney)
    TextView team_detail_tv_remainMoney;
    /**
     * 底部布局
     */
    @BindView(R.id.team_detail_operation)
    LinearLayout team_detail_operation;
    /**
     * 底部右边按钮
     */
    @BindView(R.id.team_detail_operation_left)
    TextView team_detail_operation_left;
    /**
     * 底部左边按钮
     */
    @BindView(R.id.team_detail_operation_right)
    TextView team_detail_operation_right;

    @Inject
    TeamDetailPresenterImpl mTeamDetailPresenter;
    /**
     * tablayout 名称
     */
    private List<String> module;
    /**
     * 团队详情
     */
    private TeamDetailBean mTeamDetail;
    /**
     * 团队基本信息Fragment
     */
    private TeamEssentialInfoFragment essentialInInfoFragment;
    /**
     * 导游信息Fragment
     */
    private TeamCiceroniInfoFragment ciceroniInfoFragment;
    /**
     * 团队客源地Fragment
     */
    private TeamTouristSourceFragment teamTouristSourceFragment;
    /**
     * 车辆信息Fragment
     */
    private TeamTransportVehicleFragment teamTransportVehicleFragment;
    /**
     * 时间线adapter
     */
    private TeamDetailLineAdapter timeLineAdapter;
    /**
     * 认证条码
     */
    private QRCodeDialog qrDialog;
    /**
     * 时间线数据源
     */
    private ArrayList<TeamDetailTimeLineBean> timeLineList = new ArrayList<>();
    /**
     * 时间线数据源  第一次不展示全部 点击查看更多展示全部
     */
    private ArrayList<TeamDetailTimeLineBean> timeLineFooterList = new ArrayList<>();
    /**
     * fragment集合
     */
    private ArrayList<Fragment> fragments = new ArrayList<>();
    /**
     * loading图
     */
    private SmallDialog smallDialog;
    /**
     * 审批界面头部
     */
    private String approvalTitle = "";
    /**
     * 审批界面团队状态
     */
    private String approvalTeamStatus = "";
    /**
     * 审批某申请   出团申请。变更申请。作废申请。作废审批。出团。
     */
    private int approvalRequestStatus = 0;
    /**
     * 团队详情
     */
    private String teamID = "";
    /**
     * 结算状态
     */
    private String accountStatus = "";
    /**
     * 从哪个Fragment进来的。
     */
    private String fragment_Type;

    private String holderMoney = App.getResourceString(R.string.holder_money_double_no_money);
    /**
     * 权限列表
     */
    private ArrayList<RoleBean> roleBeanList = null;
    /**
     * 导游信息
     */
    private TouristDelegateBean mTouristDelegateBean = new TouristDelegateBean();
    /**
     * 是否默认显示导游Fragment   显示的话是从导游审批进来的
     */
    private boolean isShowGuide = false;

    public TeamDetailActivity() {
        module = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.teamDetailModule));
    }

    /**
     * 从团队列表进入
     *
     * @param context
     * @param teamID
     * @param accountStatus
     * @param fragmentType
     */
    public static void callActivity(Activity context, String teamID, String accountStatus, String fragmentType) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        intent.putExtra(TEAMID, teamID);
        intent.putExtra(ACCOUNT_STATUS, accountStatus);
        intent.putExtra(FRAGMENT_TYPE, fragmentType);
        context.startActivity(intent);
    }

    public static void callActivity(Context context, TouristDelegateBean touristDelegateBean) {
        Intent intent = new Intent(context, TeamDetailActivity.class);
        intent.putExtra(TOURISTDELEGATEBEAN, touristDelegateBean);
        context.startActivity(intent);
    }


    private void getIntentData() {
        if (getIntent().hasExtra(TEAMID)) {
            teamID = getIntent().getStringExtra(TEAMID);
        }
        if (getIntent().hasExtra(ACCOUNT_STATUS)) {
            accountStatus = getIntent().getStringExtra(ACCOUNT_STATUS);
        }
        if (getIntent().hasExtra(FRAGMENT_TYPE)) {
            fragment_Type = getIntent().getStringExtra(FRAGMENT_TYPE);
        }
        if (getIntent().hasExtra(TOURISTDELEGATEBEAN)) {
            mTouristDelegateBean = getIntent().getParcelableExtra(TOURISTDELEGATEBEAN);
            teamID = mTouristDelegateBean.getTeamId();
            isShowGuide = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_detail_base;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        mTeamDetailPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = mTeamDetailPresenter;
    }

    @Override
    protected void init() {
        initFragment();
        initListener();
        getIntentData();
        initRxBus();
        smallDialog = new SmallDialog(this, getResources().getString(R.string.small_dialog));

        team_detail_swipeRefreshLayout.setOnRefreshListener(this);
        team_detail_swipeRefreshLayout.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));

        vp_team_detail.setAdapter(new TeamDetailActivity.MyViewPagerAdapter(getSupportFragmentManager()));
        vp_team_detail.setOffscreenPageLimit(3);
        tab_team_detail.setupWithViewPager(vp_team_detail);
        tab_team_detail.setTabMode(TabLayout.MODE_FIXED);
        if (isShowGuide) {
            tab_team_detail.getTabAt(3).select();
        }

        timeLineAdapter = new TeamDetailLineAdapter(this, R.layout.team_detail_timeline_item, timeLineList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recycler_team_detail.setLayoutManager(linearLayoutManager);
        recycler_team_detail.setHasFixedSize(true);
        recycler_team_detail.setNestedScrollingEnabled(false);
        recycler_team_detail.setAdapter(timeLineAdapter);
        team_detail_nestedscrollview.scrollTo(0, 0);
    }

    private void initFragment() {
        essentialInInfoFragment = TeamEssentialInfoFragment.newInstance();
        essentialInInfoFragment.setVp(vp_team_detail);
        teamTouristSourceFragment = TeamTouristSourceFragment.newInstance();
        teamTouristSourceFragment.setVp(vp_team_detail);
        teamTransportVehicleFragment = TeamTransportVehicleFragment.newInstance();
        teamTransportVehicleFragment.setVp(vp_team_detail);
        ciceroniInfoFragment = TeamCiceroniInfoFragment.newInstance();
        ciceroniInfoFragment.setVp(vp_team_detail);
        fragments.add(essentialInInfoFragment);
        fragments.add(teamTouristSourceFragment);
        fragments.add(teamTransportVehicleFragment);
        fragments.add(ciceroniInfoFragment);
    }

    private void initRxBus() {
        Subscription subscription = RxBus.getInstance().toObservable(RefreshTeamStatusEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RefreshTeamStatusEvent>() {
                    @Override
                    public void call(RefreshTeamStatusEvent refreshTeam) {
                        mTeamDetail.getTeamBaseInfo().setTeamStatus(Integer.parseInt(refreshTeam.getStatus()));
                        setPeration();
                    }
                });
        rxBusCache.add(subscription);
        Subscription RefreshDelegateSubscription = RxBus.getInstance().toObservable(RefreshDelegateListEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<RefreshDelegateListEvent>() {
                    @Override
                    public void call(RefreshDelegateListEvent refreshdelegate) {
                        isShowGuide = false;
                        setPeration();
                    }
                });
        rxBusCache.add(RefreshDelegateSubscription);
    }

    private void initListener() {
        vp_team_detail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp_team_detail.resetHeight(position);
                team_detail_nestedscrollview.smoothScrollTo(0, 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick({R.id.team_detail_fab, R.id.team_detail_qr, R.id.team_detail_operation_left, R.id.team_detail_operation_right, R.id.team_detail_button_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.team_detail_fab:
                team_detail_nestedscrollview.smoothScrollTo(0, 0);
                break;
            case R.id.team_detail_qr:
                if (mTeamDetail != null || mTeamDetail.getTeamBaseInfo().getTeamQrCode().equals("")) {
                    ToastUtils.showToast("条形码为空");
                } else {
                    if (qrDialog == null) {
                        qrDialog = new QRCodeDialog(this, mTeamDetail.getTeamBaseInfo().getTeamQrCode());
                    }
                    qrDialog.show();
                }
                break;
            case R.id.team_detail_operation_right:
                int status = mTeamDetail.getTeamBaseInfo().getTeamStatus();
                if (isShowGuide) {
                    ApprovalActivity.callActivity(this, mTouristDelegateBean);
                } else if (status == 1 || status == 5 || status == 8) {
                    ApprovalActivity.callActivity(this, mTeamDetail, approvalTitle, approvalTeamStatus, approvalRequestStatus, fragment_Type);
                } else if (status == 2) {
                    //出团
                    approvalRequestStatus = 4;
                    showAlertDialog(0);
                } else if (status == 4 || status == 6 || status == 7 || status == 10) {
                    mTeamDetailPresenter.checkIsChange();
                }
                break;
            case R.id.team_detail_operation_left:
                //作废申请
                approvalRequestStatus = 3;
                showAlertDialog(1);
                break;
            case R.id.team_detail_button_back:
                finish();
                break;
        }
    }

    @Override
    public void showTeamDetail(TeamDetailBean teamDetail) {
        team_detail_swipeRefreshLayout.setRefreshing(false);
        mTeamDetail = teamDetail;
        setPeration();
        detail_title_tv_team_number.setText(teamDetail.getTeamBaseInfo().getTeamCode());
        //设置团款信息
        team_detail_tv_prePayMoney.setText(String.format(holderMoney, Double.parseDouble(teamDetail.getTeamMoneyInfo().getPrePayMoney()) / 100.0));
        team_detail_tv_payMoney.setText(String.format(holderMoney, Double.parseDouble(teamDetail.getTeamMoneyInfo().getPayMoney()) / 100.0));
        team_detail_tv_backMoney.setText(String.format(holderMoney, Double.parseDouble(teamDetail.getTeamMoneyInfo().getBackMoney()) / 100.0));
        team_detail_tv_remainMoney.setText(String.format(holderMoney, Double.parseDouble(teamDetail.getTeamMoneyInfo().getRemainMoney()) / 100.0));
        //设置基本信息
        essentialInInfoFragment.setTeamDetailBaseInfo(teamDetail.getTeamBaseInfo());
        //设置客源地信息
        TouristsInfoBean touristsInfo = new TouristsInfoBean();
        touristsInfo.setTravelAgentName(teamDetail.getTeamBaseInfo().getTravelAgentName());
        touristsInfo.setTeamCode(teamDetail.getTeamBaseInfo().getTeamCode());
        touristsInfo.setDriverPhone(getDriverPhones());
        touristsInfo.setVehicleInfos(getDriverNums());
        touristsInfo.setGuideInfos(getGuideInfos());
        touristsInfo.setTripDates(getTripData());
        touristsInfo.setPeopleCount(getPeopleCount());
        teamTouristSourceFragment.setTeamDetailSourceListener(teamDetail.getTeamMemberSourceList(), teamDetail.getTeamMemberList(), touristsInfo, teamID);
        //设置运输车辆信息
        teamTransportVehicleFragment.setTeamDetailVehicleInfoListener(teamDetail.getVehicleInfoList());
        //设置导游信息
        ciceroniInfoFragment.seteamDetailGuideInfo(teamDetail.getGuideInfoList());
        //转换线路信息
        convertTeamLineInfoBean(teamDetail);


    }

    @Override
    public void onRefresh() {
        mTeamDetailPresenter.loadTeamDetail();
    }


    private boolean isRole(String roleTag) {
        if (roleBeanList == null) {
            roleBeanList = new Gson().fromJson(UserHelper.getInstance().getUser().getRoleList(), new TypeToken<ArrayList<RoleBean>>() {
            }.getType());
        }
        for (RoleBean roleBean : roleBeanList) {
            if (roleTag.equals(roleBean.getRoleTag())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置团队状态和底部按钮状态
     */
    private void setPeration() {
        switch (mTeamDetail.getTeamBaseInfo().getTeamStatus()) {
            case 0://编辑中
                approvalTeamStatus = "编辑中";
                detail_tv_team_status.setText(approvalTeamStatus);
                team_detail_operation.setVisibility(View.GONE);
                break;
            case 1://已提交
                approvalTeamStatus = "已提交";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 2://已提交（通过审核）
                approvalTeamStatus = "已提交（通过审核）";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 3://已提交（审核失败）
                approvalTeamStatus = "已提交（审核失败）";
                detail_tv_team_status.setText(approvalTeamStatus);
                team_detail_operation.setVisibility(View.GONE);
                break;
            case 4://生效
                approvalTeamStatus = "生效";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 5://变更
                approvalTeamStatus = "变更";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 6://变更（通过审核）
                approvalTeamStatus = "变更（通过审核）";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 7://变更（审核失败）
                approvalTeamStatus = "变更（审核失败）";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 8://作废
                approvalTeamStatus = "作废";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
            case 9://作废（通过审核）
                approvalTeamStatus = "作废（通过审核）";
                detail_tv_team_status.setText(approvalTeamStatus);
                team_detail_operation.setVisibility(View.GONE);
                break;
            case 10://作废（审核失败）
                approvalTeamStatus = "作废（审核失败）";
                detail_tv_team_status.setText(approvalTeamStatus);
                break;
        }
        if (isShowGuide) {
            if (isRole(RoleBean.HANDLEGUIDEAPP) && mTouristDelegateBean.getOperStatus() != null && mTouristDelegateBean.getOperStatus().equals("0")) {
                team_detail_operation.setVisibility(View.VISIBLE);
                team_detail_operation_left.setVisibility(View.GONE);
                if(mTouristDelegateBean.getOpType().equals("1")){
                    approvalTitle = "取消导游委派申请";
                }else{
                    approvalTitle = "审批导游委派申请";
                }
                team_detail_operation_right.setVisibility(View.VISIBLE);
                team_detail_operation_right.setText(approvalTitle);
            }
        } else {
            switch (mTeamDetail.getTeamBaseInfo().getTeamStatus()) {
                case 0://编辑中
                    break;
                case 1://已提交
                    if (isRole(RoleBean.CHECKOUTTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setVisibility(View.GONE);
                        approvalTitle = "审批出团申请";
                        approvalRequestStatus = 0;
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText(approvalTitle);
                    }
                    break;
                case 2://已提交（通过审核）
                    if (isRole(RoleBean.OUTTEAM) && accountStatus.equals("1")) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        approvalTitle = "出团";
                        approvalRequestStatus = 4;
                        team_detail_operation_right.setText(approvalTitle);
                    } else {
                        team_detail_operation_right.setVisibility(View.GONE);
                    }
                    if (isRole(RoleBean.DISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setText("作废");
                    }
                    break;
                case 3://已提交（审核失败）
                    break;
                case 4://生效
                    if (isRole(RoleBean.DISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setVisibility(View.VISIBLE);
                        approvalTitle = "作废";
                        approvalRequestStatus = 3;
                        team_detail_operation_left.setText(approvalTitle);
                    }
                    if (isRole(RoleBean.CHANGETEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText("变更");
                    }
                    break;
                case 5://变更
                    if (isRole(RoleBean.CHECKCHANGETEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        approvalTitle = "审批变更申请";
                        approvalRequestStatus = 2;
                        team_detail_operation_left.setVisibility(View.GONE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText(approvalTitle);
                    }
                    break;
                case 6://变更（通过审核）
                    if (isRole(RoleBean.DISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        approvalTitle = "作废";
                        approvalRequestStatus = 3;
                        team_detail_operation_left.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setText(approvalTitle);
                    }
                    if (isRole(RoleBean.CHANGETEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText("变更");
                    }
                    break;
                case 7://变更（审核失败）
                    if (isRole(RoleBean.DISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        approvalTitle = "作废";
                        approvalRequestStatus = 3;
                        team_detail_operation_left.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setText(approvalTitle);
                    }
                    if (isRole(RoleBean.CHANGETEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText("变更");
                    }
                    break;
                case 8://作废
                    if (isRole(RoleBean.CHECKDISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        approvalTitle = "审批作废申请";
                        approvalRequestStatus = 1;
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText(approvalTitle);
                    }
                    break;
                case 9://作废（通过审核）
                    break;
                case 10://作废（审核失败）
                    if (isRole(RoleBean.DISTEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        approvalTitle = "作废";
                        approvalRequestStatus = 3;
                        team_detail_operation_left.setVisibility(View.VISIBLE);
                        team_detail_operation_left.setText(approvalTitle);
                    }
                    if (isRole(RoleBean.CHANGETEAM)) {
                        team_detail_operation.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setVisibility(View.VISIBLE);
                        team_detail_operation_right.setText("变更");
                    }
                    break;
            }
        }

    }

    /**
     * 得到人数信息
     *
     * @return
     */
    private String getPeopleCount() {
        int size = mTeamDetail.getTeamMemberSourceList().size();
        int childCount = 0;
        int adultCount = 0;
        for (int i = 0; i < size; i++) {
            childCount += StringUtils.stringConvertInt(mTeamDetail.getTeamMemberSourceList().get(i).getChildAmount());
            adultCount += StringUtils.stringConvertInt(mTeamDetail.getTeamMemberSourceList().get(i).getAdultAmount());
        }
        return "总人数:" + (childCount + adultCount) + "(成人：" + adultCount + "人  儿童：" + childCount + "人)";
    }

    /**
     * 得到行程日期
     *
     * @return
     */
    private String getTripData() {
        int lastsize = mTeamDetail.getLineListInfo().size() == 0 ? 0 : (mTeamDetail.getLineListInfo().size() - 1);
        return mTeamDetail.getLineListInfo().get(0).getTripDate() + "至" + mTeamDetail.getLineListInfo().get(lastsize).getTripDate();
    }

    /**
     * 得到所有司机的手机号码
     *
     * @return
     */
    private String getDriverPhones() {
        int size = mTeamDetail.getVehicleInfoList().size();
        String driverPhones = "";
        for (int i = 0; i < size; i++) {
            driverPhones += mTeamDetail.getVehicleInfoList().get(i).getDriverPhone() + ",";
        }
        if (driverPhones.length() > 0) {
            return driverPhones.substring(0, driverPhones.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 得到所有的车牌号
     *
     * @return
     */
    private String getDriverNums() {
        int size = mTeamDetail.getVehicleInfoList().size();
        String driverNums = "";
        for (int i = 0; i < size; i++) {
            driverNums += mTeamDetail.getVehicleInfoList().get(i).getVehicleNum() + ",";
        }
        if (driverNums.length() > 0) {
            return driverNums.substring(0, driverNums.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 得到所有的导游号码和名字
     *
     * @return
     */
    private String getGuideInfos() {
        int size = mTeamDetail.getGuideInfoList().size();
        String guideInfos = "";
        for (int i = 0; i < size; i++) {
            guideInfos += mTeamDetail.getGuideInfoList().get(i).getName() + "(" + mTeamDetail.getGuideInfoList().get(i).getPhoneNum() + "),";
        }
        if (guideInfos.length() > 0) {
            return guideInfos.substring(0, guideInfos.length() - 1);
        } else {
            return "";
        }
    }

    @Override
    public void LoadFailure() {
        team_detail_swipeRefreshLayout.setRefreshing(false);
        showAlertDialog("详情加载失败，请重试..", 0);
    }

    @Override
    public String getTeamID() {
        return teamID;
    }

    @Override
    public int getOpType() {
        return approvalRequestStatus;
    }

    @Override
    public int getOpStatus() {
        return 0;
    }

    @Override
    public String getRemark() {
        return "";
    }

    @Override
    public void approvalSuccess(String message) {
        String teamStatus = "";
        String operType = "";
        int operStatus = 0;
        int badgeChange = 0;
        switch (getOpType()) {
            case 3:  //这里是提交作废申请
                teamStatus = "8";
                operStatus = 0;
                operType = "1";
                badgeChange = 1;
                break;
            case 4:  //这里是出团按钮
                teamStatus = "4";
                operStatus = 1;
                operType = "0";
                break;
        }
        RxBus.getInstance().post(new RefreshTeamStatusEvent(getTeamID(), teamStatus, operStatus, operType));
        RxBus.getInstance().post(new RefreshTeamBadgeEvent(fragment_Type, operType, badgeChange));
        ToastUtils.showToast(message);
        finish();
    }

    @Override
    public void canChange() {
        showAlertDialog(getResources().getString(R.string.approval_change_travel), 1);
    }

    @Override
    public void noCanChange(String message) {
        ToastUtils.showToast(message);
    }

    /**
     * 展示Dialog
     */
    /*private void showAlertDialog(String title) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }*/
    @Override
    public void showProgress() {
        team_detail_swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        team_detail_swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 转换线路信息
     *
     * @param teamDetail
     */
    private void convertTeamLineInfoBean(TeamDetailBean teamDetail) {
        timeLineList.clear();
        timeLineFooterList.clear();
        int size = teamDetail.getLineListInfo().size();
        TeamLineInfoBean teamLineInfo;
        TeamDetailTimeLineBean line;
        for (int i = 0; i < size; i++) {
            teamLineInfo = teamDetail.getLineListInfo().get(i);
            line = new TeamDetailTimeLineBean();
            line.setItemType(0);
            line.setTitle(teamLineInfo.getTripDate());
            if (i > 1) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(1);
            line.setTitle("行程：" + teamLineInfo.getPlaceInfo());
            if (i > 1) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(2);
            String scenicInfo = StringUtils.delHTMLTag(teamLineInfo.getScenicInfo());
            scenicInfo = scenicInfo.replace("),", ")\n");
            scenicInfo = scenicInfo.replace("],", "]\n");
            line.setTitle(scenicInfo);
            if (i > 0) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(3);
            line.setTitle("餐饮");
            line.setContent("早餐：" + teamLineInfo.getBreakFastInfo() + "\n午餐：" + teamLineInfo.getLunchInfo() + "\n晚餐：" + teamLineInfo.getDinnerInfo());
            if (i > 0) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(4);
            line.setTitle("住宿");
            line.setContent(teamLineInfo.getHotelInfo());
            if (i > 0) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(5);
            line.setTitle("文化演艺");
            line.setContent(teamLineInfo.getCultureInfo());
            if (i > 0) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

            line = new TeamDetailTimeLineBean();
            line.setItemType(6);
            line.setTitle("旅游购物");
            line.setContent(teamLineInfo.getShoppingInfo());
            if (i > 0) {
                timeLineFooterList.add(line);
            } else {
                timeLineList.add(line);
            }

        }
        timeLineAdapter.setNewData(timeLineList);
        if (timeLineList.size() > 7 && timeLineAdapter.getFooterLayoutCount() < 1) {
            timeLineAdapter.addFooterView(getFooterView(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeLineAdapter.addData(timeLineFooterList);
                    timeLineAdapter.removeAllFooterView();
                }
            }), 0);
        }

    }

    private View getFooterView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.recycle_line_footer, (ViewGroup) recycler_team_detail.getParent(), false);
        TextView textView = (TextView) view.findViewById(R.id.linr_footer_more);
        textView.setOnClickListener(listener);
        return view;
    }

    /**
     * 展示Dialog
     *
     * @param approvalStatus 0:出团   1：作废申请
     */
    private void showAlertDialog(final int approvalStatus) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        String title = approvalStatus == 0 ? "确认同意出团？" : getResources().getString(R.string.approval_invalid_application);
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTeamDetailPresenter.loadApproval();

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    /**
     * 展示Dialog
     *
     * @param title        弹框标题
     * @param positiveType 处理确定按钮事件   0:详情请求失败，重试。
     */
    private void showAlertDialog(String title, final int positiveType) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (positiveType) {
                    case 0:
                        mTeamDetailPresenter.loadTeamDetail();
                        break;
                    case 1:
                        ChangeTravelMainActivity.callActivity(TeamDetailActivity.this, mTeamDetail, accountStatus);
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                        break;
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
        }
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {


        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return module.get(position);//页卡标题
        }
    }
}
