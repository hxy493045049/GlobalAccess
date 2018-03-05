package com.msy.globalaccess.business.touristAdmin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.TourismDropListAdapter;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TeamAddressStatisticsContract;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.TeamAddressStatisticsPresenterImpl;
import com.msy.globalaccess.data.bean.statistics.AddressStatisticsBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.holder.TravelHelper;
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
import static com.msy.globalaccess.R.id.statistics_content_tv_child;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TRAVEL_DATA;
import static com.msy.globalaccess.widget.dialog.DateTimePickDialog.TYPE_SIMPLIFY;

/**
 * Created by pepys on 2017/5/11.
 * description:团队地区统计
 */
public class TourismAreaStatisticsFragment extends BaseFragment implements TeamAddressStatisticsContract.View,
        SearchTravelPopWindow.SelectedTravelCallBack, IUpdateable {

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
     * 行程日期区域
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
    @BindView(R.id.statistics_content_tv_adult)
    TextView statisticsContentTvAdult;
    @BindView(R.id.statistics_content_tv_adult1)
    TextView statisticsContentTvAdult1;
    /**
     * 需要隐藏
     */
    @BindView(R.id.statistics_content_tv_allpeople)
    TextView statisticsContentTvAllpeople;
    @BindView(R.id.statistics_content_tv_allpeople1)
    TextView statisticsContentTvAllpeople1;
    @BindView(statistics_content_tv_child)
    TextView statisticsContentTvChild;
    @BindView(R.id.statistics_content_tv_child1)
    TextView statisticsContentTvChild1;
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

    @Inject
    TeamAddressStatisticsPresenterImpl presenter;

    private SmallDialog smallDialog;
    /**
     * 行程时间选择器
     */
    private DateTimePickDialog tripStartDateTimeDialog;
    /**
     * 行程时间
     */
    private String tripTime = TimeFormat.getCurrentDate(regular7);
    /**
     * 行程时间  用于用户不点取消保存
     */
    private String tripTimeCache = tripTime;
    /**
     * 团队类型
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList = new ArrayList<>();
    /**
     * 选择的团队类型
     */
    private TourismDropListBean.DropSelectableBean teamType = null;
    /**
     * 选择的团队类型 用于用户不点确认保存
     */
    private TourismDropListBean.DropSelectableBean teamTypeCache;
    /**
     * 旅行社pop
     */
    private SearchTravelPopWindow searchTravelPopWindow;
    /**
     * 行程日期和更多 pop
     */
    private TourismPopManager travelDatePop, morePop;
    /**
     * 保存选择的旅行社
     */
    private TravelAgentListBean travelAgentListBean = new TravelAgentListBean();
    /**
     * 选择的团队状态
     */
    private TourismDropListBean.DropSelectableBean teamStauts = null;
    /**
     * 选择的团队状态
     */
    private TourismDropListBean.DropSelectableBean teamStautsCache = null;
    /**
     * 选择的客源地
     */
    private TourismDropListBean.DropSelectableBean customSourceType = null;
    /**
     * 选择的客源地
     */
    private TourismDropListBean.DropSelectableBean customSourceTypeCache = null;
    /**
     * 页面数据
     */
    private AddressStatisticsBean mAddressStatistics;

    @Inject
    public TourismAreaStatisticsFragment() {
    }


    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_team_address;
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
        searchTravelPopWindow = new SearchTravelPopWindow(getActivity(), this);
        statisticsContentTvAllpeople.setVisibility(View.GONE);
        statisticsContentTvAllpeople1.setVisibility(View.GONE);
        statisticsContentTvChild.setVisibility(View.GONE);
        statisticsContentTvChild1.setVisibility(View.GONE);

        statisticsContentTvAdult1.setText("总人数：");

        statisticsContentCcv1TvTitle.setText("地区团队统计");
        statisticsContentCcv2TvTitle2.setText("地区人数统计");
        presenter.requestTCSS();
        initDrop();
        initDateTimePop();

        statisticsContentTvTripData.setText(TimeFormat.getCurrentDate(regular7));
    }

    public void setTeamType(ArrayList<TourismDropListBean.DropSelectableBean> teamTypeList) {
        this.teamTypeList = teamTypeList;
        if (morePop != null) {
            morePop.updateTeamType(teamTypeList);
        }
    }

    private void initDateTimePop() {
        tripStartDateTimeDialog = new DateTimePickDialog(getActivity(), tripTime, TYPE_SIMPLIFY);
        tripStartDateTimeDialog.buildDialog();
        tripStartDateTimeDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                tripTimeCache = dateTime;
                travelDatePop.updataTravelTime(tripTimeCache);
            }
        });
    }

    //初始化下拉框
    private void initDrop() {
        searchTravelPopWindow = new SearchTravelPopWindow(getActivity(), this);
        travelDatePop = new TourismPopManager.Builder(getActivity())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setTravelDate(tripTimeCache)
                .setEnableBgDark(true)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        touristSpotsImgFiter2.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
                        travelDatePop.updataTravelTime(tripTime);
                        tripTime = tripTimeCache;
                    }
                })
                .addOnItemChildClickListener(R.id.llTimeBegin, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        tripStartDateTimeDialog.show();
                    }
                })
                .addOnItemChildClickListener(R.id.llTimeEnd, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                    }
                })
                .addOnItemChildClickListener(R.id.btnConfirm, new TourismPopManager.OnItemChildClickListener() {
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
                            statisticsContentTvTripData.setText(tripTime);
                            presenter.requestTCSS();
                        }
                    }
                })
                .build();
        travelDatePop.setOnTimeDeleteCallback(new TourismDropListAdapter.OnTimeDeleteCallback() {
            @Override
            public void onDelete(@TourismDropListBean.ViewType int type, int timeType, String time) {
                if(TYPE_TRAVEL_DATA == type){
                    tripTimeCache = "";
                }
            }
        });

        morePop = new TourismPopManager.Builder(getActivity())
                .setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setTeamState(TravelHelper.getInstance().getStatusList())
                .setTeamType(teamTypeList)
                .setGuestSource(TravelHelper.getInstance().getTouristsList())
                .setEnableBgDark(true)
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        touristSpotsImgFiter3.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
                        morePop.setSetectedItem(TourismDropListBean.ViewType.TYPE_TEAM_STATE, teamStauts);
                        morePop.setSetectedItem(TourismDropListBean.ViewType.TYPE_TEAM, teamType);
                        morePop.setSetectedItem(TourismDropListBean.ViewType.TYPE_GUEST_SOURCE, customSourceType);
                        teamStautsCache = teamStauts;
                        teamTypeCache = teamType;
                        customSourceTypeCache = customSourceType;
                    }
                }).addOnItemChildClickListener(R.id.tvAll, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (viewType == TourismDropListBean.ViewType.TYPE_TEAM_STATE) {
                            //团队状态
                            teamStautsCache = value;
                        } else if (viewType == TourismDropListBean.ViewType.TYPE_TEAM) {
                            //团队类型
                            teamTypeCache = value;
                        } else {
                            //客源地
                            customSourceTypeCache = value;
                        }
                    }
                })
                .addOnItemChildClickListener(R.id.tvCell, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (viewType == TourismDropListBean.ViewType.TYPE_TEAM_STATE) {
                            //团队状态
                            teamStautsCache = value;
                        } else if (viewType == TourismDropListBean.ViewType.TYPE_TEAM) {
                            //团队类型
                            teamTypeCache = value;
                        } else {
                            //客源地
                            customSourceTypeCache = value;
                        }
                    }
                })
                .addOnItemChildClickListener(R.id.btnConfirm, new TourismPopManager.OnItemChildClickListener() {
                    @Override
                    public void onCallBack(TourismDropListBean.DropSelectableBean value, int viewType) {
                        if (morePop != null) {
                            teamStauts = teamStautsCache;
                            teamType = teamTypeCache;
                            customSourceType = customSourceTypeCache;
                            presenter.requestTCSS();
                            morePop.dismiss();
                        }
                    }
                })
                .build();
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


    public void update() {
        clearPopDate();
        presenter.requestTCSS();
    }

    /**
     * 清除旅行社数据
     */
    private void clearPopDate() {
        travelAgentListBean = new TravelAgentListBean();
        if (searchTravelPopWindow != null) {
            searchTravelPopWindow.clearCurrentList();
            if (touristSpotsTvFiter1 != null) {
                touristSpotsTvFiter1.setText(getString(R.string.guider_affiliate));
            }
        }

        tripTime = TimeFormat.getCurrentDate(regular7);
        teamType = null;
        teamStauts = null;
        customSourceType = null;
        //需要把缓存的值全部还原
        tripTimeCache = tripTime;
        teamTypeCache = teamType;
        teamStautsCache = teamStauts;
        customSourceTypeCache = customSourceType;

        statistics_content_ll_tripData.setVisibility(View.VISIBLE);
        statisticsContentTvTripData.setText(tripTime);

        travelDatePop.resetPop();
        morePop.resetPop();

        travelDatePop.updataTravelTime(tripTime);

        initDateTimePop();
    }

    @Override
    public void getTravelCustomSourceStatistics(AddressStatisticsBean addressStatistics) {
        if (addressStatistics == null) {
            return;
        }
        mAddressStatistics = addressStatistics;
        statisticsContentTvAllteam.setText(addressStatistics.getTeamCount() + "个");
        statisticsContentTvAdult.setText(addressStatistics.getPeopleCount() + "人");
        showAddressTeamCountChart(addressStatistics.getStatisticsBean().getTravelTeamCount());
        showAddressPeopleCountChart(addressStatistics.getStatisticsBean().getTravelTeamPeopleCount());
    }

    @Override
    public String getTravelAgentId() {
        return travelAgentListBean.getTravelAgentId() == null ? "" : travelAgentListBean.getTravelAgentId();
    }

    @Override
    public String getTeamDate() {
        return tripTime;
    }

    @Override
    public String getTeamStauts() {
        return (teamStauts == null || teamStauts.getValue().equals("-1")) ? "" : teamStauts.getValue();
    }

    public String getTeamTypeID() {
        return (teamType == null || teamType.getValue().equals("-1")) ? "" : teamType.getValue();
    }

    @Override
    public String getCustomSourceType() {
        return (customSourceType == null || customSourceType.getValue().equals("-1")) ? "" : customSourceType
                .getValue();
    }

    /**
     * 显示地区团队统计
     * @param data
     */
    public void showAddressTeamCountChart(ColumnChartData data) {
        if(data != null){
            int size = mAddressStatistics.getCustomSourceCountList().size();
            statisticsContentCcv1.setColumnChartData(data);
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv1.getMaximumViewport().height() * 1.25f, size,0);
            statisticsContentCcv1.setMaximumViewport(viewportMax);
            statisticsContentCcv1.setCurrentViewport(new Viewport(0, statisticsContentCcv1.getMaximumViewport().height(), size > 5 ? 5 : size, 0));
            statisticsContentCcv1.moveTo(0, 0);
            statisticsContentCcv1.setVisibility(View.VISIBLE);
        }else{
            statisticsContentCcv1.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 显示地区人数统计
     * @param data
     */
    public void showAddressPeopleCountChart(ColumnChartData data) {
        if(data != null){
            int size = mAddressStatistics.getCustomSourceCountList().size();
            statisticsContentCcv2.setColumnChartData(data);
            Viewport viewportMax = new Viewport(-0.5f, statisticsContentCcv2.getMaximumViewport().height() * 1.25f, size,  0);
            statisticsContentCcv2.setMaximumViewport(viewportMax);
            statisticsContentCcv2.setCurrentViewport(new Viewport(0, statisticsContentCcv2.getMaximumViewport() .height(), size > 5 ? 5 : size, 0));
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
    public void confirmSelectedTravel(TravelAgentListBean travelAgentListBean) {
        this.travelAgentListBean = travelAgentListBean;
        presenter.requestTCSS();
        touristSpotsTvFiter1.setText(travelAgentListBean.getTravelAgentName());
    }

    @Override
    public void dismissTravel() {
        touristSpotsImgFiter1.setImageDrawable(getResources().getDrawable(R.mipmap.icon_solid_arrow_down));
    }
}
