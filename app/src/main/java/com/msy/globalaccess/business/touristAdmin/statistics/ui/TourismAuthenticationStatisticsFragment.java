package com.msy.globalaccess.business.touristAdmin.statistics.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TeamAuthenticationStaticsContract;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.AuthenticationStatisticsImpl;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.widget.chartview.CustomColumnChartView;
import com.msy.globalaccess.widget.dialog.DateTimePickDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.popupwindow.CustomPopWindow;
import com.msy.globalaccess.widget.popupwindow.SearchTouristPopWindow;
import com.msy.globalaccess.widget.popupwindow.SearchTravelPopWindow;
import com.msy.globalaccess.widget.popupwindow.TourismPopManager;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Viewport;

import static cn.msy.zc.commonutils.TimeFormat.regular7;
import static com.msy.globalaccess.widget.dialog.DateTimePickDialog.TYPE_SIMPLIFY;
import static com.msy.globalaccess.widget.dialog.DateTimePickDialog.TYPE_TIME;

/**
 * Created by pepys on 2017/5/11.
 * description:团队认证统计
 */
public class TourismAuthenticationStatisticsFragment extends BaseFragment implements TeamAuthenticationStaticsContract
        .View, IUpdateable {
    /**
     * 按景点
     */
    public static int TouristAdthentication = 1;
    /**
     * 按旅行社
     */
    public static int TravelAdthentication = 0;

    @BindView(R.id.statistics_content_ll_tripData)
    LinearLayout statistics_content_ll_tripData;

    @BindView(R.id.activity_tourist_spots_head)
    LinearLayout activity_tourist_spots_head;
    /**
     * 第一个筛选框  旅行社
     */
    @BindView(R.id.tourist_spots_tv_fiter1)
    TextView touristSpotsTvFiter1;
    @BindView(R.id.tourist_spots_img_fiter1)
    ImageView touristSpotsImgFiter1;

    /**
     * 第二个筛选框  行程日期
     */
    @BindView(R.id.tourist_spots_tv_fiter2)
    TextView touristSpotsTvFiter2;
    @BindView(R.id.tourist_spots_img_fiter2)
    ImageView touristSpotsImgFiter2;
    /**
     * 第三个筛选框  更多
     */
    @BindView(R.id.tourist_spots_tv_fiter3)
    TextView touristSpotsTvFiter3;
    @BindView(R.id.tourist_spots_img_fiter3)
    ImageView touristSpotsImgFiter3;

    /**
     * 第四个筛选框  更多
     */
    @BindView(R.id.tourist_spots_tv_fiter4)
    TextView touristSpotsTvFiter4;
    @BindView(R.id.tourist_spots_img_fiter4)
    ImageView touristSpotsImgFiter4;
    @BindView(R.id.tourist_spots_ll_fiter4)
    RelativeLayout touristSpotsllFiter4;
    /**
     * 行程日期
     */
    @BindView(R.id.statistics_content_tv_tripData)
    TextView statisticsContentTvTripData;
    /**
     * 景区认证总团数
     */
    @BindView(R.id.statistics_content_tv_allteam)
    TextView statisticsContentTvAllteam;
    /**
     * 景区认证总团数
     */
    @BindView(R.id.statistics_content_tv_allteam1)
    TextView statisticsContentTvAllteam1;
    /**
     * 景区认证总人数
     */
    @BindView(R.id.statistics_content_tv_allpeople)
    TextView statisticsContentTvAllpeople;
    /**
     * 景区认证总人数
     */
    @BindView(R.id.statistics_content_tv_allpeople1)
    TextView statisticsContentTvAllpeople1;
    /**
     * 需要隐藏
     */
    @BindView(R.id.statistics_content_rl_info2)
    RelativeLayout statisticsContentRlInfo2;
    /**
     * 第一个柱形图标题
     */
    @BindView(R.id.statistics_content_ccv1_tv_title)
    TextView statisticsContentCcv1TvTitle;
    /**
     * 第一个柱形图
     */
    @BindView(R.id.statistics_content_ccv1)
    CustomColumnChartView statisticsContentCcv1;
    /**
     * 第二个柱形图标题
     */
    @BindView(R.id.statistics_content_ccv2_tv_title2)
    TextView statisticsContentCcv2TvTitle;
    /**
     * 第二个柱形图
     */
    @BindView(R.id.statistics_content_ccv2)
    CustomColumnChartView statisticsContentCcv2;

    @Inject
    AuthenticationStatisticsImpl presenter;

    private SmallDialog smallDialog;
    /**
     * 更多pop
     */
    private TourismPopManager morePop;
    /**
     * 按照什么区分
     */
    private int adthenticationType = TravelAdthentication;
    /**
     * 团队类型
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList = new ArrayList<>();

    /**
     * 按景点、按旅行社 pop
     */
    private CustomPopWindow popType;
    private View popTypeView;
    /**
     * pop选择对钩
     */
    private Drawable drawableCheck;
    /**
     * 选择旅行社和景点
     */
    private TextView tvTourism, tvScenicSpot;
    /**
     * 搜索旅行社
     */
    private SearchTravelPopWindow searchTravelPopWindow;
    /**
     * 选择的景点
     */
    private TravelAgentListBean travelAgentBean;
    /**
     * 搜索景点
     */
    private SearchTouristPopWindow searchTouristPopWindow;
    /**
     * 选择的景点
     */
    private ScenicListBean touristBean;
    //-----------------------------------------------------------
    /**
     * 选择的团队类型
     */
    private TourismDropListBean.DropSelectableBean teamType;
    /**
     * 选择的团队类型 用于用户不点确认保存
     */
    private TourismDropListBean.DropSelectableBean teamTypeCache;
    //-------------------------认证开始时间选择器 年月日----------------------------------
    /**
     * 认证开始时间选择器 年月日
     */
    private DateTimePickDialog authStartDateTimeDialog;
    /**
     * 认证开始时间
     */
    private String authStartTime = TimeFormat.getCurrentDate(regular7);
    /**
     * 认证开始时间  用于用户不点确认保存
     */
    private String authStartTimeCache = authStartTime;
    //-------------------------认证开始时间选择器 时分  HH：mm----------------------------------
    /**
     * 认证开始时间选择器 时分  HH：mm
     */
    private DateTimePickDialog authStartHourTimeDialog;
    /**
     * 认证开始时间   HH：mm
     */
    private String authStartHourTime = "00:00";

    /**
     * 认证开始时间  用于用户不点确认保存   HH：mm
     */
    private String authStartHourTimeCache = authStartHourTime;
    //------------------------认证结束时间选择器 年月日-----------------------------------
    /**
     * 认证结束时间选择器 年月日
     */
    private DateTimePickDialog authenticationEndDateTimeDialog;
    /**
     * 认证结束时间
     */
    private String authEndTime = TimeFormat.getCurrentDate(regular7);
    /**
     * 认证结束时间  用于用户不点确认保存
     */
    private String authEndTimeCache = authEndTime;
    //-----------------------认证结束时间选择器 时分  HH：mm------------------------------------
    /**
     * 认证结束时间选择器 时分  HH：mm
     */
    private DateTimePickDialog authEndHourTimeDialog;
    /**
     * 认证结束时间    HH：mm
     */
    private String authEndHourTime = "23:59";
    /**
     * 认证结束时间  用于用户不点确认保存    HH：mm
     */
    private String authEndHourTimeCache = authEndHourTime;

    //-----------------------------------------------------------

    /**
     * 统计数据
     */
    private TravelTeamStatisticsBean mTravelTeamStatistics;

    @Inject
    public TourismAuthenticationStatisticsFragment() {

    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_team_authentication;
    }

    @Override
    public void init(View view) {
        smallDialog = new SmallDialog(getActivity());
        statisticsContentCcv1.setValueTouchEnabled(false);
        statisticsContentCcv1.setZoomEnabled(false);
        statisticsContentCcv1.setScrollEnabled(true);
        statisticsContentCcv1.setHorizontalScrollBarEnabled(true);
        statisticsContentCcv1.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        statisticsContentCcv2.setValueTouchEnabled(false);
        statisticsContentCcv2.setZoomEnabled(false);
        statisticsContentCcv2.setScrollEnabled(true);
        statisticsContentCcv2.setHorizontalScrollBarEnabled(true);
        statisticsContentCcv2.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        touristSpotsTvFiter1.setText("按旅行社");
        touristSpotsTvFiter2.setText("景点");
        touristSpotsTvFiter3.setText("旅行社");
        touristSpotsTvFiter4.setText("更多");
        initType();
        touristSpotsllFiter4.setVisibility(View.VISIBLE);
        statisticsContentRlInfo2.setVisibility(View.GONE);
        statistics_content_ll_tripData.setVisibility(View.GONE);

        initDrop();
        initDateTime();
        presenter.requestTTCS();
    }

    public void setTeamType(ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList) {
        this.teamTypeList = teamTypeList;
        if (morePop != null) {
            morePop.updateTeamType(teamTypeList);
        }
    }

    private void initType() {
        if (adthenticationType == TouristAdthentication) {
            statisticsContentTvAllteam1.setText("景区认证总团数：");
            statisticsContentTvAllpeople1.setText("景区认证总人数：");
            statisticsContentCcv1TvTitle.setText("核心景区团队认证统计");
            statisticsContentCcv2TvTitle.setText("核心景区团队认证人数统计");
        } else {
            statisticsContentTvAllteam1.setText("旅行社认证总团数：");
            statisticsContentTvAllpeople1.setText("旅行社认证总人数：");
            statisticsContentCcv1TvTitle.setText("旅行社团队认证统计");
            statisticsContentCcv2TvTitle.setText("旅行社团队认证人数统计");
        }

    }

    //初始化下拉框
    private void initDrop() {
        searchTouristPopWindow = new SearchTouristPopWindow(getActivity(), new SearchTouristPopWindow
                .SelectedTouristCallBack() {
            @Override
            public void confirmSelectedTourist(ScenicListBean scenicBean) {
                touristBean = scenicBean;
                presenter.requestTTCS();
                touristSpotsTvFiter2.setText(scenicBean.getScenicName());
            }

            @Override
            public void dismissTravel() {
                touristSpotsImgFiter2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
            }
        });

        searchTravelPopWindow = new SearchTravelPopWindow(getActivity(), new SearchTravelPopWindow
                .SelectedTravelCallBack() {
            @Override
            public void confirmSelectedTravel(TravelAgentListBean travelAgentListBean) {
                travelAgentBean = travelAgentListBean;
                presenter.requestTTCS();
                touristSpotsTvFiter3.setText(travelAgentListBean.getTravelAgentName());
            }

            @Override
            public void dismissTravel() {
                touristSpotsImgFiter3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
            }
        });
        morePop = new TourismPopManager.Builder(getActivity())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAuthenticationTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +
                        authEndHourTimeCache)
                .setTeamType(teamTypeList)
                .setEnableBgDark(true)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        morePop.setSetectedItem(TourismDropListBean.ViewType.TYPE_TEAM, teamType);
                        morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache +
                                " " + authEndHourTimeCache);
                        teamTypeCache = teamType;
                        touristSpotsImgFiter4.setImageDrawable(getResources().getDrawable(R.mipmap
                                .icon_solid_arrow_down));
                    }
                })
                .addOnItemChildClickListener(R.id.tvTimeBegin, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, @TourismDropListBean
                            .ViewType int ViewType) {
                        authStartDateTimeDialog.show();
                    }
                })
                .addOnItemChildClickListener(R.id.tvTimeBeginDetail, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, @TourismDropListBean
                            .ViewType int ViewType) {
                        authStartHourTimeDialog.show();
                    }
                })
                .addOnItemChildClickListener(R.id.tvTimeEnd, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, @TourismDropListBean
                            .ViewType int ViewType) {
                        authenticationEndDateTimeDialog.show();
                    }
                })
                .addOnItemChildClickListener(R.id.tvTimeEndDetail, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, @TourismDropListBean
                            .ViewType int ViewType) {
                        authEndHourTimeDialog.show();
                    }
                })
                .addOnItemChildClickListener(R.id.tvAll, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        teamTypeCache = value;
                    }
                })
                .addOnItemChildClickListener(R.id.tvCell, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        teamTypeCache = value;
                    }
                })
                .addOnItemChildClickListener(R.id.btnConfirm, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (morePop != null) {
                            teamType = teamTypeCache;
                            authStartTime = authStartTimeCache;
                            authStartHourTime = authStartHourTimeCache;
                            authEndTime = authEndTimeCache;
                            authEndHourTime = authEndHourTimeCache;
                            morePop.dismiss();
                            presenter.requestTTCS();
                        }
                    }
                })
                .build();


        drawableCheck = getResources().getDrawable(R.mipmap.icon_check_theme);
        drawableCheck.setBounds(0, 0, drawableCheck.getMinimumWidth(), drawableCheck.getMinimumHeight());
        popTypeView = LayoutInflater.from(getActivity()).inflate(R.layout.view_pop_auth_type, null);
        tvTourism = (TextView) popTypeView.findViewById(R.id.tvTourism);
        tvScenicSpot = (TextView) popTypeView.findViewById(R.id.tvScenicSpot);
        tvTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedState((TextView) v);
                adthenticationType = TravelAdthentication;
                popType.dissmiss();
                touristSpotsTvFiter1.setText("按旅行社");
                initType();
                presenter.requestTTCS();
            }
        });
        tvScenicSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedState((TextView) v);
                adthenticationType = TouristAdthentication;
                popType.dissmiss();
                touristSpotsTvFiter1.setText("按景点");
                initType();
                presenter.requestTTCS();
            }
        });
        popType = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(popTypeView)
                .enableBackgroundDark(true)
                .setBgDarkAlpha(40)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
    }

    private void initDateTime() {
        authStartDateTimeDialog = new DateTimePickDialog(getActivity(), authStartTime, TYPE_SIMPLIFY);
        authStartDateTimeDialog.buildDialog();
        authStartDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                authStartTimeCache = dateTime;
                if (authStartHourTimeCache.equals("")) {
                    authStartHourTimeCache = "00:00";
                }
                morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +
                        authEndHourTimeCache);
            }
        });
        authenticationEndDateTimeDialog = new DateTimePickDialog(getActivity(), authEndTime, TYPE_SIMPLIFY);
        authenticationEndDateTimeDialog.buildDialog();
        authenticationEndDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                authEndTimeCache = dateTime;
                if (authEndHourTimeCache.equals("")) {
                    authEndHourTimeCache = "00:00";
                }
                morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +
                        authEndHourTimeCache);
            }
        });

        authStartHourTimeDialog = new DateTimePickDialog(getActivity(), authStartHourTime, TYPE_TIME);
        authStartHourTimeDialog.buildDialog();
        authStartHourTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                authStartHourTimeCache = dateTime;
                if (authStartTimeCache.equals("")) {
                    authStartTimeCache = TimeFormat.formatData(regular7, new Date());
                }
                morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +
                        authEndHourTimeCache);
            }
        });
        authEndHourTimeDialog = new DateTimePickDialog(getActivity(), authEndHourTime, TYPE_TIME);
        authEndHourTimeDialog.buildDialog();
        authEndHourTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                authEndHourTimeCache = dateTime;
                if (authEndTimeCache.equals("")) {
                    authEndTimeCache = TimeFormat.formatData(regular7, new Date());
                }
                morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +
                        authEndHourTimeCache);
            }
        });
    }

    private void changeSelectedState(TextView v) {
        tvScenicSpot.setCompoundDrawables(null, null, null, null);
        tvTourism.setCompoundDrawables(null, null, null, null);
        tvScenicSpot.setTextColor(getResources().getColor(R.color.text_primary_dark));
        tvTourism.setTextColor(getResources().getColor(R.color.text_primary_dark));

        v.setTextColor(getResources().getColor(R.color.colorPrimary));
        v.setCompoundDrawables(null, null, drawableCheck, null);
        touristSpotsImgFiter1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
    }


    @OnClick({R.id.tourist_spots_ll_fiter1, R.id.tourist_spots_ll_fiter2, R.id.tourist_spots_ll_fiter3, R.id
            .tourist_spots_ll_fiter4})
    public void filterOnclick(View view) {
        switch (view.getId()) {
            case R.id.tourist_spots_ll_fiter1://点击按景点
                touristSpotsImgFiter1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                popType.showAsDropDown(activity_tourist_spots_head, 0, 1);
                break;
            case R.id.tourist_spots_ll_fiter2://点击景点
                touristSpotsImgFiter2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                searchTouristPopWindow.showAsDropDown(activity_tourist_spots_head);
                break;
            case R.id.tourist_spots_ll_fiter3://点击旅行社
                touristSpotsImgFiter3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                searchTravelPopWindow.showAsDropDown(activity_tourist_spots_head);
                break;
            case R.id.tourist_spots_ll_fiter4://点击更多
                touristSpotsImgFiter4.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                morePop.showAsDropDown(activity_tourist_spots_head, 0, 1);
                break;
        }
    }


    public void update() {
        clearPopDate();
        presenter.requestTTCS();
    }

    /**
     * 清除Pop数据
     */
    private void clearPopDate() {
        //景点
        touristBean = null;
        if (searchTouristPopWindow != null) {
            searchTouristPopWindow.clearCurrentList();
            if (touristSpotsTvFiter2 != null) {
                touristSpotsTvFiter2.setText(getString(R.string.scenic));
            }
        }
        //更多
        if (morePop != null) {
            morePop.resetPop();
        }
        //旅行社
        travelAgentBean = null;
        if (searchTravelPopWindow != null) {
            searchTravelPopWindow.clearCurrentList();
            if (touristSpotsTvFiter3 != null) {
                touristSpotsTvFiter3.setText(getString(R.string.guider_affiliate));
            }
        }

        adthenticationType = TravelAdthentication;
        teamType = null;
        authStartTime = TimeFormat.getCurrentDate(regular7);
        authStartHourTime = "00:00";
        authEndTime = authStartTime;
        authEndHourTime = "23:59";

        //需要把缓存的值全部还原
        authStartTimeCache = authStartTime;
        authStartHourTimeCache = authStartHourTime;
        authEndTimeCache = authEndTime;
        authEndHourTimeCache = authEndHourTime;
        morePop.updateAuthTime(authStartTimeCache + " " + authStartHourTimeCache, authEndTimeCache + " " +authEndHourTimeCache);
        changeSelectedState(tvTourism);
        touristSpotsTvFiter1.setText("按旅行社");
        initType();
        initDateTime();

    }

    @Override
    public void getTravelTeamCheckStatistics(TravelTeamStatisticsBean travelTeamStatistics) {
        if(travelTeamStatistics == null){
            return;
        }
        mTravelTeamStatistics = travelTeamStatistics;
        statisticsContentTvAllteam.setText(travelTeamStatistics.getTeamCount() + "个");
        statisticsContentTvAllpeople.setText(travelTeamStatistics.getCheckPeopleCount() + "人");
        showAuthTeamCountChart();
        showAuthPeoleCountChart();
    }

    /**
     * 显示认证团队统计
     */
    public void showAuthTeamCountChart() {
        if(mTravelTeamStatistics.getTeamColumnChartData() != null){
            statisticsContentCcv1.setColumnChartData(mTravelTeamStatistics.getTeamColumnChartData());
            int size = mTravelTeamStatistics.getTeamCheckCountList().size();
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv1.getMaximumViewport().height() * 1.25f, size, 0);
            statisticsContentCcv1.setMaximumViewport(viewportMax);
            statisticsContentCcv1.setCurrentViewport(new Viewport(0, statisticsContentCcv1.getMaximumViewport().height(), size > 5 ? 5 : size, 0));
            statisticsContentCcv1.moveTo(0, 0);
            statisticsContentCcv1.setVisibility(View.VISIBLE);
        }else{
            statisticsContentCcv1.setVisibility(View.INVISIBLE);
        }

    }
    /**
     * 显示认证人数统计
     */
    public void showAuthPeoleCountChart() {
        if(mTravelTeamStatistics.getPeopleColumnChartData() != null){
            statisticsContentCcv2.setColumnChartData(mTravelTeamStatistics.getPeopleColumnChartData());
            int size = mTravelTeamStatistics.getTeamCheckCountList().size();
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv2.getMaximumViewport().height() * 1.25f, size, 0);
            statisticsContentCcv2.setMaximumViewport(viewportMax);
            statisticsContentCcv2.setCurrentViewport(new Viewport(0, statisticsContentCcv2.getMaximumViewport().height(), size > 5 ? 5 : size, 0));
            statisticsContentCcv2.moveTo(0, 0);
            statisticsContentCcv2.setVisibility(View.VISIBLE);
        }else{
            statisticsContentCcv2.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public String getSearchType() {
        return adthenticationType + "";
    }

    @Override
    public String getTravelAgentId() {
        return travelAgentBean == null ? "" : travelAgentBean.getTravelAgentId();
    }

    @Override
    public String getScenicId() {
        return touristBean == null ? "" : touristBean.getScenicId();
    }

    @Override
    public String getTeamCheckStartDate() {
        return authStartTime + " " + authStartHourTime;
    }

    @Override
    public String getTeamCheckEndDate() {
        return authEndTime + " " + authEndHourTime;
    }

    @Override
    public String getTeamTypeId() {
        return (teamType == null || teamType.getValue().equals("-1")) ? "" : teamType.getValue();
    }

    @Override
    public void showProgress() {
        if (isVisible && smallDialog != null) {
            smallDialog.shows();
        }
    }

    @Override
    public void hideProgress() {
        smallDialog.dismisss();
    }
}
