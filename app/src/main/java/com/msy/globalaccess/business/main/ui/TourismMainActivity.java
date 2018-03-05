package com.msy.globalaccess.business.main.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.annotation.BindValues;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.TourismViewPagerAdapter;
import com.msy.globalaccess.business.main.contract.TourismMainContract;
import com.msy.globalaccess.business.main.contract.impl.TourismMainPresenterImpl;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TeamStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismAreaStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismAuthenticationStatisticsFragment;
import com.msy.globalaccess.business.touristAdmin.datapreview.ui.TourismDataPreFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.ui.TourismTeamStatisticsFragment;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.business.travelAgency.setting.PersonalCenterFragment;
import com.msy.globalaccess.data.api.TeamServiceApi;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.widget.popupwindow.CustomPopWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pepys on 2017/5/11.
 * description: 旅游局首页
 */
@SuppressWarnings("deprecation")
@BindValues
public class TourismMainActivity extends BaseActivity implements TourismMainContract.View, PopupWindow
        .OnDismissListener {

    /**
     * 团队类型数据
     */
    public ArrayList<TourismDropListBean.DropSelectableBean> teamTypelist = new ArrayList<>();
    @Inject
    TourismMainPresenterImpl mPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ivToolbarRight)
    AppCompatImageView ivToolbarRight;
    @BindView(R.id.admin_appbar)
    AppBarLayout admin_appbar;
    @BindView(R.id.imgBtnBack)
    AppCompatImageButton imgBtnBack;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarTitle;

    @Inject
    TourismTeamStatisticsFragment infoStatisticsFragment;//团队信息统计
    @Inject
    TourismAreaStatisticsFragment addressStatisticsFragment;//团队地区统计
    @Inject
    TourismAuthenticationStatisticsFragment authenticationStatisticsFragment;//团队认证统计
    @Inject
    TourismDataPreFragment dataPreviewFragment;//数据概览页面
    @Inject
    PersonalCenterFragment mPersonalCenterFragment;//系统设置界面
    @Inject
    TeamStatisticsFragment teamStatisticsFragment;//团队管理界面


    @BindView(R.id.vpContent)
    ViewPager vpContent;

    /**
     * 数据概览
     */
    @BindView(R.id.tourismRbDataPre)
    AppCompatRadioButton tourismRbDataPre;

    /**
     * 统计查询
     */
    @BindView(R.id.tourismRbStatistics)
    AppCompatRadioButton tourismRbStatistics;
    /**
     * 个人中心
     */
    @BindView(R.id.tourismRbtUserCenter)
    AppCompatRadioButton tourismRbtUserCenter;

    /**
     * pop
     */
    private CustomPopWindow mStatisticsFilterPop;

    /**
     * 顶部pop 向下箭头
     */
    private Drawable arrow_down;
    /**
     * 顶部pop 向上箭头
     */
    private Drawable arrow_up;
    /**
     * 顶部pop内柔对勾箭头
     */
    private Drawable drawableCheck;
    private BaseFragment mCurrentFragment;
    private List<String> title = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array
            .touristTitleDescription));
    private List<String> staticsTitle = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array
            .touristTitleStatistics));
    private ArrayList<TextView> tbContexts = new ArrayList<>();
    private int textColorNormal = App.getResource().getColor(R.color.text_primary_dark),
            textColorSelected = App.getResource().getColor(R.color.colorPrimary);
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public static void callActivity(Context ctx) {
        Intent intent = new Intent(ctx, TourismMainActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tourism_admin_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        mPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = mPresenter;
    }

    @Override
    protected void init() {
        initToolBar();
        initDrawableResource();
        initListener();
        initViewpager();
        initData();
    }


    @OnClick({R.id.tourismRbDataPre, R.id.tourismRbStatistics, R.id.tourismRbtUserCenter})
    public void radioButtonClick(View view) {
        switch (view.getId()) {
            case R.id.tourismRbDataPre:
                vpContent.setCurrentItem(0, true);
                break;
            case R.id.tourismRbStatistics:
                vpContent.setCurrentItem(1, true);
                break;
            case R.id.tourismRbtUserCenter:
                vpContent.setCurrentItem(5, true);
                break;
        }
    }

    @Override
    public void saveTravelData(SearchTravelAgentBean data) {
    }

    @Override
    public void getTeamType(ArrayList<TourismDropListBean.DropSelectableBean> data) {
        if (data != null) {
            teamTypelist.clear();
            teamTypelist.addAll(data);
            infoStatisticsFragment.setTeamType(teamTypelist);
            addressStatisticsFragment.setTeamType(teamTypelist);
            authenticationStatisticsFragment.setTeamType(teamTypelist);
        }
    }

    @Override
    public void getTouristData(ArrayList<ScenicListBean> data) {

    }

    @Override
    public int currentPageNum() {
        return 1;
    }

    @Override
    public int showNum() {
        return 9999;
    }

    @Override
    public String scenicName() {
        return "";
    }

    @Override
    public String cityName() {
        return "";
    }

    @Override
    public String scenicLevel() {
        return "";
    }

    @Override
    public String isAlaway() {
        return "";
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onDismiss() {
        setToolbarDrawableRight(true, 0);
    }

    //********************************private**************************************

    private void initViewpager() {
        fragments.add(dataPreviewFragment);
        fragments.add(infoStatisticsFragment);
        fragments.add(addressStatisticsFragment);
        fragments.add(authenticationStatisticsFragment);
        fragments.add(teamStatisticsFragment);
        fragments.add(mPersonalCenterFragment);
        vpContent.setAdapter(new TourismViewPagerAdapter(getSupportFragmentManager(), fragments));
        vpContent.setOffscreenPageLimit(1);
        vpContent.setCurrentItem(0);
        mCurrentFragment = dataPreviewFragment;
        vpContent.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switchToolbar(position);
                updateFragment();
                if (position == 0) {
                    tourismRbDataPre.setChecked(true);
                } else if (position >= 1 && position <= 4) {
                    tourismRbStatistics.setChecked(true);
                } else {
                    tourismRbtUserCenter.setChecked(true);
                }
                mCurrentFragment = fragments.get(position);
            }
        });
    }

    private void updateFragment() {
        if (mCurrentFragment instanceof IUpdateable) {
            ((IUpdateable) mCurrentFragment).update();
        }
    }

    private void switchToolbar(int postion) {
        switch (postion) {
            case 0:
                ivToolbarRight.setVisibility(View.GONE);
                tvToolbarTitle.setText(title.get(0));
                setToolbarDrawableRight(false, 0);
                break;
            case 1:
            case 2:
            case 3:
                ivToolbarRight.setVisibility(View.GONE);
                tvToolbarTitle.setText(staticsTitle.get(postion - 1));
                setToolbarDrawableRight(true, 0);
                updateToolbarPopTvState(postion - 1);
                break;
            case 4:
                ivToolbarRight.setVisibility(View.VISIBLE);
                tvToolbarTitle.setText(staticsTitle.get(postion - 1));
                setToolbarDrawableRight(true, 0);
                updateToolbarPopTvState(postion - 1);
                break;
            case 5:
                ivToolbarRight.setVisibility(View.GONE);
                tvToolbarTitle.setText(title.get(2));
                setToolbarDrawableRight(false, 0);
                break;
        }
    }


    /**
     * 变更首页appbar的滑动特性,0表示不能滚动,
     * 其他属性请参考{@link AppBarLayout.LayoutParams.ScrollFlags}
     */
    private void changeAppbarScrollFlag(int... flags) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        if (params != null) {
            int flagResult = 0;
            for (int flag : flags) {
                flagResult = flagResult | flag;
            }
            params.setScrollFlags(flagResult);
            mToolbar.setLayoutParams(params);
        }
    }

    private void initData() {
        mPresenter.loadTravelData();//旅行社数据
        mPresenter.loadTravelTeamType();//团队类型
        mPresenter.loadTouristData();//景点数据
    }

    private void initListener() {
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(TeamServiceApi.TeamListApi.optionSearchType, "0");
                hashMap.put(TeamServiceApi.TeamListApi.optionTeamCode, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTeamStatus, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTravelAgentId, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTravelDepId, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTeamStartDate, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTeamEndDate, "");
                hashMap.put(TeamServiceApi.TeamListApi.optionTeamTypeId, "");
                SearchResultActivity.callActivity(TourismMainActivity.this, SearchResultActivity.TEAM_SEARCH, hashMap);
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });
    }

    private void initToolBar() {
        initToolbarPop();
        changeAppbarScrollFlag(0);
        admin_appbar.setExpanded(true, false);
        mToolbar.setNavigationIcon(R.mipmap.icon_home);
        ivToolbarRight.setVisibility(View.GONE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(title.get(0));

        setToolbarDrawableRight(false, 1);
        ivToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.callActivity(TourismMainActivity.this, SearchResultActivity.TEAM_SEARCH);
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });
        tvToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment == dataPreviewFragment || mCurrentFragment == mPersonalCenterFragment) {
                    return;
                }
                setToolbarDrawableRight(true, 1);
                mStatisticsFilterPop.showAsDropDown(v);
            }
        });
    }

    private void setToolbarDrawableRight(boolean flag, int orientation) {
        if (flag) {
            tvToolbarTitle.setCompoundDrawables(null, null, (orientation == 0) ? arrow_down : arrow_up, null);
            tvToolbarTitle.setCompoundDrawablePadding(15);
        } else {
            tvToolbarTitle.setCompoundDrawables(null, null, null, null);
            tvToolbarTitle.setCompoundDrawablePadding(0);
        }
    }

    private void updateToolbarPopTvState(int position) {
        for (TextView tv : tbContexts) {
            if (position == tbContexts.indexOf(tv)) {
                tv.setTextColor(textColorSelected);
                tv.setCompoundDrawables(null, null, drawableCheck, null);
            } else {
                tv.setTextColor(textColorNormal);
                tv.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    private void initToolbarPop() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatisticsFilterPop != null) {
                    mStatisticsFilterPop.dissmiss();
                }

                switch (v.getId()) {
                    case R.id.statistics_pop_tv_info:
                        vpContent.setCurrentItem(1, true);
                        break;
                    case R.id.statistics_pop_tv_address:
                        vpContent.setCurrentItem(2, true);
                        break;
                    case R.id.statistics_pop_tv_authentication:
                        vpContent.setCurrentItem(3, true);
                        break;
                    case R.id.statistics_pop_tv_teamList:
                        vpContent.setCurrentItem(4, true);
                        break;
                }
            }
        };

        //pop内容
        View popContentView = LayoutInflater.from(this).inflate(R.layout.statistics_popupwindow, null);
        tbContexts.add(0, (TextView) popContentView.findViewById(R.id.statistics_pop_tv_info));
        tbContexts.add(1, (TextView) popContentView.findViewById(R.id.statistics_pop_tv_address));
        tbContexts.add(2, (TextView) popContentView.findViewById(R.id.statistics_pop_tv_authentication));
        tbContexts.add(3, (TextView) popContentView.findViewById(R.id.statistics_pop_tv_teamList));

        tbContexts.get(0).setOnClickListener(listener);
        tbContexts.get(1).setOnClickListener(listener);
        tbContexts.get(2).setOnClickListener(listener);
        tbContexts.get(3).setOnClickListener(listener);

        mStatisticsFilterPop = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(popContentView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(40)
                .setOnDissmissListener(this)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
    }

    private void initDrawableResource() {
        arrow_down = getResources().getDrawable(R.mipmap.icon_arrow_down_statistics);
        arrow_down.setBounds(0, 0, arrow_down.getMinimumWidth(), arrow_down.getMinimumHeight());
        arrow_up = getResources().getDrawable(R.mipmap.icon_arrow_up_statistics);
        arrow_up.setBounds(0, 0, arrow_up.getMinimumWidth(), arrow_up.getMinimumHeight());
        drawableCheck = getResources().getDrawable(R.mipmap.icon_check_theme);
        drawableCheck.setBounds(0, 0, drawableCheck.getMinimumWidth(), drawableCheck.getMinimumHeight());
    }

}
