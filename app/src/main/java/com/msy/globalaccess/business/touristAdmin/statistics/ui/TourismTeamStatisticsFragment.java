package com.msy.globalaccess.business.touristAdmin.statistics.ui;

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
import com.msy.globalaccess.business.adapter.TourismDropListAdapter;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TourismTeamStatisticsContract;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.TeamStatisticsImpl;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.widget.chartview.CustomColumnChartView;
import com.msy.globalaccess.widget.dialog.DateTimePickDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.popupwindow.SearchTravelPopWindow;
import com.msy.globalaccess.widget.popupwindow.TourismPopManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Viewport;

import static cn.msy.zc.commonutils.TimeFormat.regular7;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TRAVEL_DATA;
import static com.msy.globalaccess.widget.dialog.DateTimePickDialog.TYPE_SIMPLIFY;

/**
 * Created by pepys on 2017/5/11.
 * description:团队信息统计
 */
public class TourismTeamStatisticsFragment extends BaseFragment implements TourismTeamStatisticsContract.View,
        IUpdateable {

    @Inject
    TeamStatisticsImpl presenter;
    @BindView(R.id.activity_tourist_spots_head)
    LinearLayout activity_tourist_spots_head;
    /**
     * 第一个筛选框  旅行社
     */
    @BindView(R.id.tourist_spots_tv_fiter1)
    TextView touristSpotsTvFiter1;
    @BindView(R.id.tourist_spots_img_fiter1)
    ImageView touristSpotsImgFiter1;

    @BindView(R.id.tourist_spots_ll_fiter3)
    RelativeLayout tourist_spots_ll_fiter3;
    @BindView(R.id.tourist_spots_ll_fiter2)
    RelativeLayout tourist_spots_ll_fiter2;
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
     * 行程日期
     */
    @BindView(R.id.statistics_content_ll_tripData)
    LinearLayout statistics_content_ll_tripData;
    /**
     * 行程日期
     */
    @BindView(R.id.statistics_content_tv_tripData)
    TextView statisticsContentTvTripData;
    /**
     * 总团队
     */
    @BindView(R.id.statistics_content_tv_allteam)
    TextView statisticsContentTvAllteam;
    /**
     * 总人数
     */
    @BindView(R.id.statistics_content_tv_allpeople)
    TextView statisticsContentTvAllpeople;
    /**
     * 成人
     */
    @BindView(R.id.statistics_content_tv_adult)
    TextView statisticsContentTvAdult;
    /**
     * 儿童
     */
    @BindView(R.id.statistics_content_tv_child)
    TextView statisticsContentTvChild;
    /**
     * 第一个柱形图标题
     */
    @BindView(R.id.statistics_content_ccv1_tv_title)
    TextView statisticsContentCcv1TvTitle;
    /**
     * 第一个柱形图单位
     */
    @BindView(R.id.statistics_content_ccv1_tv_unit)
    TextView statisticsContentCcv1TvUnit;
    /**
     * 第一个柱形图
     */
    @BindView(R.id.statistics_content_ccv1)
    CustomColumnChartView statisticsContentCcv1;
    /**
     * 第二个柱形图标题
     */
    @BindView(R.id.statistics_content_ccv2_tv_title2)
    TextView statisticsContentCcv2TvTitle2;
    /**
     * 第二个柱形图单位
     */
    @BindView(R.id.statistics_content_ccv2_tv_unit)
    TextView statisticsContentCcv2TvUnit;
    /**
     * 第二个柱形图
     */
    @BindView(R.id.statistics_content_ccv2)
    CustomColumnChartView statisticsContentCcv2;


    private SmallDialog smallDialog;
    /**
     * 旅行社pop
     */
    private SearchTravelPopWindow searchTravelPopWindow;
    /**
     * 行程日期和更多 pop
     */
    private TourismPopManager travelDatePop, morePop;

    /**
     * 行程时间选择器
     */
    private DateTimePickDialog tripStartDateTimeDialog;
    /**
     * 行程时间
     */
    private String tripTime = TimeFormat.getCurrentDate(regular7);
    /**
     * 行程时间  用于用户不点确认保存
     */
    private String tripTimeCache = tripTime;

    /**
     * 创建开始时间选择器
     */
    private DateTimePickDialog createStartDateTimeDialog;
    /**
     * 创建开始时间
     */
    private String createStartTime = "";
    /**
     * 创建开始时间 用于用户不点确认保存
     */
    private String createStartTimeCache = createStartTime;
    /**
     * 创建结束时间选择器
     */
    private DateTimePickDialog createEndDateTimeDialog;
    /**
     * 创建结束时间
     */
    private String createEndTime = "";
    /**
     * 创建结束时间 用于用户不点确认保存
     */
    private String createEndTimeCache = createEndTime;
    /**
     * 保存选择的旅行社
     */
    private TravelAgentListBean mTravelAgentListBean = new TravelAgentListBean();
    /**
     * 团队类型
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList = new ArrayList<>();
    /**
     * 选择的团队类型
     */
    private TourismDropListBean.DropSelectableBean teamType;
    /**
     * 选择的团队类型 用于用户不点确认保存
     */
    private TourismDropListBean.DropSelectableBean teamTypeCache;

    @Inject
    public TourismTeamStatisticsFragment() {

    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
        presenter.attachView(this);
        superPresenter = presenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_team_info;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return presenter;
    }

    @Override
    public void init(View view) {
        smallDialog = new SmallDialog(getActivity());
        presenter.loadTeamInfoData();
        initDrop();
        initDateTime();
        statisticsContentTvTripData.setText(TimeFormat.getCurrentDate(regular7));
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
    }

    @OnClick({R.id.tourist_spots_ll_fiter1, R.id.tourist_spots_ll_fiter2, R.id.tourist_spots_ll_fiter3})
    public void filterOnclick(View view) {
        switch (view.getId()) {
            case R.id.tourist_spots_ll_fiter1://点击旅行社
                searchTravelPopWindow.showAsDropDown(activity_tourist_spots_head);
                touristSpotsImgFiter1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                break;
            case R.id.tourist_spots_ll_fiter2://点击行程日期
                touristSpotsImgFiter2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                travelDatePop.showAsDropDown(activity_tourist_spots_head);
                break;
            case R.id.tourist_spots_ll_fiter3://点击更多
                touristSpotsImgFiter3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_up));
                morePop.showAsDropDown(activity_tourist_spots_head, 0, 1);
                break;
        }
    }

    private void initDateTime() {
        tripStartDateTimeDialog = new DateTimePickDialog(getActivity(), tripTime, TYPE_SIMPLIFY);
        tripStartDateTimeDialog.buildDialog();
        tripStartDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                tripTimeCache = dateTime;
                travelDatePop.updataTravelTime(tripTimeCache);
            }
        });
        createStartDateTimeDialog = new DateTimePickDialog(getActivity(), createStartTime, TYPE_SIMPLIFY);
        createStartDateTimeDialog.buildDialog();
        createStartDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                createStartTimeCache = dateTime;
                morePop.updateCreaterTime(createStartTimeCache, createEndTimeCache);
            }
        });
        createEndDateTimeDialog = new DateTimePickDialog(getActivity(), createEndTime, TYPE_SIMPLIFY);
        createEndDateTimeDialog.buildDialog();
        createEndDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                createEndTimeCache = dateTime;
                morePop.updateCreaterTime(createStartTimeCache, createEndTimeCache);
            }
        });
    }

    //初始化下拉框
    private void initDrop() {
        searchTravelPopWindow = new SearchTravelPopWindow(getActivity(), new SearchTravelPopWindow
                .SelectedTravelCallBack() {
            @Override
            public void confirmSelectedTravel(TravelAgentListBean travelAgentListBean) {
                mTravelAgentListBean = travelAgentListBean;
                presenter.loadTeamInfoData();
                touristSpotsTvFiter1.setText(travelAgentListBean.getTravelAgentName());
            }

            @Override
            public void dismissTravel() {
                touristSpotsImgFiter1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
            }
        });

        travelDatePop = new TourismPopManager.Builder(getActivity())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setTravelDate(tripTime)
                .setEnableBgDark(true)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        touristSpotsImgFiter2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
                        travelDatePop.updataTravelTime(tripTime);
                        tripTimeCache = tripTime;
                    }
                }).addOnItemChildClickListener(R.id.llTimeBegin, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        tripStartDateTimeDialog.show();
                    }
                }).addOnItemChildClickListener(R.id.llTimeEnd, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {

                    }
                }).addOnItemChildClickListener(R.id.btnConfirm, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (travelDatePop != null) {
                            tripTime = tripTimeCache;
                            travelDatePop.dismiss();
                            if (tripTime.equals("")) {
                                statistics_content_ll_tripData.setVisibility(View.GONE);
                            }else{
                                statistics_content_ll_tripData.setVisibility(View.VISIBLE);
                                statisticsContentTvTripData.setText(tripTime);
                            }
                            presenter.loadTeamInfoData();
                        }
                    }
                }).build();
        travelDatePop.setOnTimeDeleteCallback(new TourismDropListAdapter.OnTimeDeleteCallback() {
            @Override
            public void onDelete(@TourismDropListBean.ViewType int type, int timeType, String time) {
                if(TYPE_TRAVEL_DATA == type){
                    tripTimeCache = "";
                }
            }
        });
        morePop = new TourismPopManager.Builder(getActivity())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCreaterTime(createStartTime, createEndTime)
                .setEnableBgDark(true)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        touristSpotsImgFiter3.setImageDrawable(getResources().getDrawable(R.mipmap
                                .icon_solid_arrow_down));
                        morePop.updateCreaterTime(createStartTime, createEndTime);
                        morePop.setSetectedItem(TourismDropListBean.ViewType.TYPE_TEAM, teamType);

                        teamTypeCache = teamType;
                        createStartTimeCache = createStartTime;
                        createEndTimeCache = createEndTime;
                    }
                }).addOnItemChildClickListener(R.id.llTimeBegin, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        createStartDateTimeDialog.show();
                    }
                }).addOnItemChildClickListener(R.id.llTimeEnd, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        createEndDateTimeDialog.show();
                    }
                }).setTeamType(teamTypeList)
                .addOnItemChildClickListener(R.id.tvAll, new TourismPopManager
                        .OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        teamTypeCache = value;
                    }
                }).addOnItemChildClickListener(R.id.tvCell, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        teamTypeCache = value;
                    }
                }).addOnItemChildClickListener(R.id.btnConfirm, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (morePop != null) {
                            createStartTime = createStartTimeCache;
                            createEndTime = createEndTimeCache;
                            teamType = teamTypeCache;
                            morePop.dismiss();
                            presenter.loadTeamInfoData();
                        }
                    }
                }).build();
        morePop.setOnTimeDeleteCallback(new TourismDropListAdapter.OnTimeDeleteCallback() {
            @Override
            public void onDelete(@TourismDropListBean.ViewType int type, int timeType, String time) {
                if(timeType == TourismDropListAdapter.OnTimeDeleteCallback.START_TIME){
                    createStartTimeCache = "";
                }else{
                    createEndTimeCache = "";
                }
            }
        });
    }

    public void update() {
        if (super.mFragmentView == null) {
            return;
        }
        clearPopDate();
        presenter.loadTeamInfoData();
    }

    /**
     * 清除旅行社数据
     */
    private void clearPopDate() {

        if (morePop != null) {
            morePop.resetPop();
        }
        if (travelDatePop != null) {
            travelDatePop.resetPop();
        }
        mTravelAgentListBean = new TravelAgentListBean();
        if (searchTravelPopWindow != null) {
            searchTravelPopWindow.clearCurrentList();
            if (touristSpotsTvFiter1 != null) {
                touristSpotsTvFiter1.setText(getString(R.string.guider_affiliate));
            }
        }

        tripTime = TimeFormat.getCurrentDate(regular7);
        createStartTime = "" ;
        createEndTime = "" ;
        teamType = null;

        //需要把缓存的值全部还原
        tripTimeCache = tripTime;
        createStartTimeCache = createStartTime;
        createEndTimeCache = createEndTime;
        teamTypeCache = teamType;
        statistics_content_ll_tripData.setVisibility(View.VISIBLE);
        statisticsContentTvTripData.setText(tripTime);

        travelDatePop.resetPop();
        morePop.resetPop();

        travelDatePop.updataTravelTime(tripTime);
        morePop.updateCreaterTime(createStartTime, createEndTime);

        initDateTime();
    }

    @Override
    public void showTeamInfoStatistics(TeamStatisticsImpl.ResultBean resultBean) {
        statisticsContentTvAllteam.setText(resultBean.getTeamCount() + "个");
        statisticsContentTvAllpeople.setText(resultBean.getPeopleCount() + "人");
        statisticsContentTvAdult.setText(resultBean.getAdultCount() + "人");
        statisticsContentTvChild.setText(resultBean.getChildCount() + "人");
        showTravelTeamCountChart(resultBean);
        showTravelTeamPeopleCountChart(resultBean.getTravelTeamPeopleCount());
    }

    /**
     * 显示旅行社接待团队统计
     */
    public void showTravelTeamCountChart(TeamStatisticsImpl.ResultBean resultBean) {
        if(resultBean.getTravelTeamCount() != null){
            int size = resultBean.getTravelTeamCount().getColumns().size();
            statisticsContentCcv1.setColumnChartData(resultBean.getTravelTeamCount());
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv1.getMaximumViewport().height() * 1.25f, size,0);
            statisticsContentCcv1.setMaximumViewport(viewportMax);
            Viewport viewport = new Viewport(0, statisticsContentCcv1.getMaximumViewport().height() * 1.25f, size > 5 ? 5 : size, 0);
            statisticsContentCcv1.setCurrentViewport(viewport);
            statisticsContentCcv1.moveTo(0, 0);
            statisticsContentCcv1.setVisibility(View.VISIBLE);
        }else{
            statisticsContentCcv1.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 显示旅行社接待团队人数统计
     */
    public void showTravelTeamPeopleCountChart(ColumnChartData data) {
        if(data != null){
            int size = data.getColumns().size();
            statisticsContentCcv2.setColumnChartData(data);
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv2.getMaximumViewport().height() * 1.25f, size,0);
            statisticsContentCcv2.setMaximumViewport(viewportMax);
            Viewport viewport = new Viewport(0, statisticsContentCcv2.getMaximumViewport().height() * 1.25f, size > 5 ? 5  : size, 0);
            statisticsContentCcv2.setCurrentViewport(viewport);
            statisticsContentCcv2.moveTo(0, 0);
            statisticsContentCcv2.setVisibility(View.VISIBLE);
        }else{
            statisticsContentCcv2.setVisibility(View.INVISIBLE);
        }

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

    @Override
    public String getTravelAgentId() {
        return mTravelAgentListBean.getTravelAgentId() == null ? "" : mTravelAgentListBean.getTravelAgentId();
    }

    @Override
    public String getTeamDate() {
        return tripTime;
    }

    @Override
    public String getTeamCreateStartDate() {
        return createStartTime;
    }

    @Override
    public String getTeamCreateEndDate() {
        return createEndTime;
    }

    public String getTeamType() {
        return (teamType == null || teamType.getValue().equals("-1")) ? "" : teamType.getValue();
    }

    public void setTeamType(ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList) {
        this.teamTypeList = teamTypeList;
        if (morePop != null) {
            morePop.updateTeamType(teamTypeList);
        }
    }
}
