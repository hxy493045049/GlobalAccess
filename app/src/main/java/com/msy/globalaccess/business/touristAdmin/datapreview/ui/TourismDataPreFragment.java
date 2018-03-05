package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.TourismDataPreviewContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.DataPreviewPresenterImpl;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTeamCountListBean;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTimeCountListWrapper;
import com.msy.globalaccess.data.bean.statistics.ScenicOrderCustomCountListBean;
import com.msy.globalaccess.data.bean.statistics.TendencyBean;
import com.msy.globalaccess.data.bean.statistics.TouristAbroadStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsBean;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.StatisticsHelper;
import com.msy.globalaccess.widget.chartview.CustomColumnChartView;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * 数据概览
 * Created by shawn on 2017/5/22 0022.
 * <p>
 * description :
 */

@SuppressWarnings("deprecation")
public class TourismDataPreFragment extends BaseFragment implements IUpdateable, TourismDataPreviewContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int ERROR_STATE = 0;
    private static final int LOADING_STATE = 1;
    private static final int CONTENT_STATE = 2;
    //手动计算出来的布局宽度,别问怎么算出来的,德斌算的
    private static final int LAYOUT_WIDTH = DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(207);
    @Inject
    DataPreviewPresenterImpl mPresenter;
    @BindView(R.id.loadView)
    LinearLayout loadView;
    @BindView(R.id.vsErrorView)
    LinearLayout errorView;
    @BindView(R.id.lineChart)
    LineChartView lineChart;
    @BindView(R.id.tvCreateTeamSize)
    TextView tvCreateTeamSize;//今日创建团队数
    @BindView(R.id.tvCisborderTeamSize)
    TextView tvCisborderTeamSize;//今日在途团队数
    @BindView(R.id.tvCisborderGuestsNum)
    TextView tvCisborderGuestsNum;//今日在途游客数
    @BindView(R.id.tvScenicName1)
    TextView tvScenicName1;
    @BindView(R.id.tvScenicName2)
    TextView tvScenicName2;
    @BindView(R.id.tvScenicName3)
    TextView tvScenicName3;
    @BindView(R.id.tvScenicName4)
    TextView tvScenicName4;
    @BindView(R.id.ivColorBlue)
    ImageView ivColorBlue;
    @BindView(R.id.ivColorRed)
    ImageView ivColorRed;
    @BindView(R.id.ivColorPurple)
    ImageView ivColorPurple;
    @BindView(R.id.ivColorLightBlue)
    ImageView ivColorLightBlue;
    @BindView(R.id.llColorHint)
    LinearLayout llColorHint;
    @BindView(R.id.swpDatePreview)
    SwipeRefreshLayout swpDatePreview;
    @BindView(R.id.statistics_content_rl1)
    RelativeLayout statistics_content_rl1;
    @BindView(R.id.statistics_content_rl2)
    RelativeLayout statistics_content_rl2;
    @BindView(R.id.statistics_content_rl3)
    RelativeLayout statistics_content_rl3;
    @BindView(R.id.statistics_content_rl4)
    RelativeLayout statistics_content_rl4;
    @BindView(R.id.statistics_content_rl5)
    RelativeLayout statistics_content_rl5;
    @BindView(R.id.statistics_content_rl6)
    RelativeLayout statistics_content_rl6;
    @BindView(R.id.statistics_content_ll1)
    LinearLayout statistics_content_ll1;
    @BindView(R.id.statistics_content_ll2)
    LinearLayout statistics_content_ll2;
    @BindView(R.id.statistics_content_ll3)
    LinearLayout statistics_content_ll3;
    @BindView(R.id.statistics_content_ll4)
    LinearLayout statistics_content_ll4;
    @BindView(R.id.statistics_content_ll5)
    LinearLayout statistics_content_ll5;
    @BindView(R.id.statistics_content_ll6)
    LinearLayout statistics_content_ll6;
    @BindView(R.id.scnicAuthTimeStatistics)
    FrameLayout scnicAuthTimeStatistics;
    @BindView(R.id.flPeopleAndDayView)
    FrameLayout flPeopleAndDayView;
    @BindView(R.id.framelayoutAge)
    FrameLayout framelayoutAge;
    private SmallDialog dialog;
    private View parentView;

    //   -------------------- 旅游人天趋势分析--------------------
    private ColumnChartView peopleAndDayView;
    private List<TextView> sourceContentRatio;
    private List<TextView> sourceContent;
    //--------------------旅游人天趋势分析 end--------------------

    //-----------------景区饼形图-------------
    private PieChartView scenic_statistics;
    private List<TextView> scenicContent;
    private List<TextView> scenicContentRatio;
    private ArrayList<RelativeLayout> scenicRlViewsList = new ArrayList<>();//景点统计view缓存
    //---------------------景区饼形图 end-------------------

    //------------------------性别统计------------------------
    private ArrayList<LinearLayout> sexllViewsList = new ArrayList<>();
    private List<TextView> sexContentRatio;
    private List<TextView> sexContent;
    private PieChartView sex_statistics;
    //---------------------性别统计 end-------------------

    //---------------------景区客流趋势 ---------------------
    private ArrayList<TextView> linearTextHint = new ArrayList<>();
    private ArrayList<ImageView> linearColorHint = new ArrayList<>();
    //---------------------景区客流趋势 end---------------------

    //---------------------客源地总览饼形图---------------------
    private PieChartView pcvOriginStatistics;
    //---------------------客源地总览饼形图 end---------------------

    private ArrayList<LinearLayout> sourcellViewsList = new ArrayList<>();
    //景点认证时间分布
    private ColumnChartView authTimeView;
    //团队停留天数统计
    private LineChartView stayDayLineChart;
    //游客年龄统计
    private CustomColumnChartView customColumnChartView;
    //境内外游客对比
    private LineChartView abroadLineChart;
    //趋势分析柱形图
    private ColumnChartView ccvTrendStatistics;

    @Inject
    public TourismDataPreFragment() {
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return mPresenter;
    }

    @Override
    public void init(View view) {
        this.parentView = view;
        changeViewState(LOADING_STATE);
        mPresenter.onStart();
        dialog = new SmallDialog(getActivity());
        swpDatePreview.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        swpDatePreview.setOnRefreshListener(this);

        initScenicView();//今日景区
        initSceneryPreview();//初始景区客流趋势的折线图
        initSourceView();//客源地
        initAbroadStatistics(); //境内外游客数量对比
        initSexStatisticsPie();//性别分部统计
        initGuestAgeStatistics();//游客年龄分布
        initScenicAuthTimeStatistics();//景区认证时间分布
        initStayDayStatistics();//团队停留天数统计
        initPeopleAndDayTendencyView();//旅游人天趋势分析
        tendencyAnalyse();//趋势分析

        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frame_tourism_data_pre;
    }

    @Override
    public void update() {
    }

    @Override
    public void showProgress() {
        if (isVisible && dialog != null && loadView.getVisibility() != View.VISIBLE && errorView.getVisibility() !=
                View.VISIBLE && !swpDatePreview.isRefreshing()) {
            dialog.shows();
        }
    }

    @Override
    public void hideProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismisss();
        }
        swpDatePreview.setRefreshing(false);
        swpDatePreview.setEnabled(true);
    }

    @Override
    public void showContent() {
        changeViewState(CONTENT_STATE);
    }

    @Override
    public void handleSummary(DataPreviewPresenterImpl.ResultBean resultBean) {
        tvCreateTeamSize.setText(String.format(App.getResourceString(R.string.holder_count), resultBean
                .getCreateTeamCount()));
        tvCisborderTeamSize.setText(String.format(App.getResourceString(R.string.holder_count), resultBean
                .getDomesticTeamCount()));
        tvCisborderGuestsNum.setText(String.format(App.getResourceString(R.string.holder_peopel_num),
                resultBean.getDomesticCustomCount()));
    }

    @Override
    public String[] getTimes() {
        return new String[]{
                TimeFormat.getCurrentDate(TimeFormat.regular7), TimeFormat.getCurrentDate(TimeFormat.regular7)
        };
    }

    @Override
    public String getScenicId() {
        return "";
    }

    //********************************presenter数据回调*****************************************

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void handleSexStatistics(DataPreviewPresenterImpl.ResultBean resultBean) {
        showSexStatisticsData(resultBean.getOrigenBean().getCustomSexCountList(), resultBean.getSexData());
    }

    @Override
    public void handlePeopleAndDayStatisticsData(PeopleAndDayStatisticsBeanWrapper.ResultBean resultBean) {
        List<PeopleAndDayStatisticsBeanWrapper.PeopleAndDayStatisticsBean> data = resultBean.getOriginData();
        ColumnChartData columnChartData = resultBean.getColumnChartData();
        if (data == null || data.size() <= 0) {
            peopleAndDayView.setColumnChartData(null);
            return;
        }
        int size = columnChartData.getColumns().size();
        if (size <= 5) {
            peopleAndDayView.setScrollEnabled(false);
        } else {
            peopleAndDayView.setScrollEnabled(true);
        }
        peopleAndDayView.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.5f, peopleAndDayView.getMaximumViewport().height() * 1.25f, size, 0);
        peopleAndDayView.setMaximumViewport(viewportMax);
        Viewport viewport = new Viewport(0, peopleAndDayView.getMaximumViewport().height() * 1.25f, size > 5 ? 5 :
                size, 0);
        peopleAndDayView.setCurrentViewport(viewport);
        peopleAndDayView.moveTo(0, 0);
        peopleAndDayView.setVisibility(View.VISIBLE);
    }

    @Override
    public void handlerGuestAgeData(GuestAgeStatisticsBeanWrapper.ResultBean resultBean) {
        List<GuestAgeStatisticsBeanWrapper.GuestAgeStatisticsBean> data = resultBean.getOrigin();
        ColumnChartData columnChartData = resultBean.getColumnChartData();
        if (data == null || data.size() <= 0) {
            customColumnChartView.setColumnChartData(null);
            return;
        }
        int size = columnChartData.getColumns().size();
        if (size <= 5) {
            customColumnChartView.setScrollEnabled(false);
        } else {
            customColumnChartView.setScrollEnabled(true);
        }
        customColumnChartView.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.5f, 110, size, 0);
        customColumnChartView.setMaximumViewport(viewportMax);
        Viewport viewport = new Viewport(0, 110, size > 5 ? 5 : size, 0);
        customColumnChartView.setCurrentViewport(viewport);
        customColumnChartView.moveTo(0, 0);
    }

    @Override//显示趋势分析柱形图
    public void handleTendencyAnalysis(DataPreviewPresenterImpl.ResultBean resultBean) {
        ColumnChartData columnChartData = resultBean.getTendencyData();
        List<TendencyBean> data = resultBean.getOrigenBean().getTeamCountList();
        if (data == null || data.size() <= 0) {
            ccvTrendStatistics.setColumnChartData(null);
            return;
        }
        ccvTrendStatistics.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.7f, ccvTrendStatistics.getMaximumViewport().height() * 1.25f, data
                .size(), 0);
        ccvTrendStatistics.setMaximumViewport(viewportMax);
        Viewport viewport = new Viewport(-0.7f, ccvTrendStatistics.getMaximumViewport().height(), data.size(), 0);
        ccvTrendStatistics.setCurrentViewport(viewport);
        ccvTrendStatistics.moveTo(0, 0);
    }

    @Override//景区认证时间分布
    public void showScenicAuthTimeData(List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList>
                                               data, ColumnChartData columnChartData) {
        if (data == null || data.size() <= 0) {
            authTimeView.setColumnChartData(null);
            return;
        }
        if (data.size() > 5) {
            authTimeView.setScrollEnabled(true);
        } else {
            authTimeView.setScrollEnabled(false);
        }
        int size = columnChartData.getColumns().size();
        authTimeView.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.5f, authTimeView.getMaximumViewport().height() * 1.25f, size, 0);
        Viewport viewport = new Viewport(0, authTimeView.getMaximumViewport().height() * 1.25f, size > 5 ? 5 :
                size, 0);
        authTimeView.setMaximumViewport(viewportMax);
        authTimeView.setCurrentViewport(viewport);
        authTimeView.moveTo(0, 0);
    }

    @Override//景区饼形图
    public void handleAccrossToScenery(DataPreviewPresenterImpl.ResultBean resultBean) {
        StatisticsHelper.hideView(scenicRlViewsList);
        if (resultBean != null && resultBean.getOrigenBean() != null) {
            List<ScenicCheckTeamCountListBean> data = resultBean.getOrigenBean()
                    .getScenicCheckTeamCountList();
            PieChartData pieChartData = resultBean.getSceneryPieData();
            if (data == null || data.size() <= 0) {
                scenic_statistics.setPieChartData(null);
                return;
            }
            int size = (data.size() >= 6 ? 6 : data.size());
            int totalNum = 0;
            for (int i = 0; i < size; i++) {
                totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
            }
            for (int i = 0; i < size; i++) {
                scenicContent.get(i).setText(data.get(i).getName());
                scenicRlViewsList.get(i).setVisibility(View.VISIBLE);
                scenicContentRatio.get(i).setText("(" + data.get(i).getCountNum() + ")");
                StatisticsHelper.resetTextViewWidth(scenicContentRatio.get(i),scenicContent.get(i), LAYOUT_WIDTH);
            }
            pieChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.text_size_12)));
            pieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.text_size_12)));
            pieChartData.setCenterText1Color(getResources().getColor(R.color.colorPrimary));
            pieChartData.setCenterText2Color(getResources().getColor(R.color.colorPrimary));
            scenic_statistics.setPieChartData(pieChartData);
        } else {
            scenic_statistics.setPieChartData(null);
        }
    }

    @Override //景区预览,景区客流趋势(折线图)
    public void handleSceneryPreview(DataPreviewPresenterImpl.ResultBean resultBean) {
        List<ScenicOrderCustomCountListBean> list = resultBean.getOrigenBean().getScenicOrderCustomCountList();
        StatisticsHelper.hideView(linearColorHint);
        StatisticsHelper.hideView(linearTextHint);
        if (list != null && list.size() > 0) {
            llColorHint.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                String spotName = list.get(i).getName();
                if (TextUtils.isEmpty(spotName)) {
                    continue;
                }
                TextView tv = linearTextHint.get(i);
                tv.setText(spotName);
                tv.setVisibility(View.VISIBLE);
                linearColorHint.get(i).setVisibility(View.VISIBLE);
            }
        }

        lineChart.setLineChartData(resultBean.getLineChartData());
        lineChart.setValueSelectionEnabled(true);
        Viewport max = lineChart.getMaximumViewport();
        final Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = -0.1f;
        LineChartData data = resultBean.getLineChartData();
        if (data != null) {
            v.right = data.getAxisXBottom().getValues().size() - 1;
        } else {
            v.right = 5;
        }
        v.top = max.top * 1.25f;
        v.bottom = 0;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
    }

    @Override//显示客源地总览饼形图
    public void handleGuestsRegion(DataPreviewPresenterImpl.ResultBean resultBean) {
        List<GuestSourceBeanWrapper.GuestSourceBean> data = resultBean.getOrigenBean().getCustomSourceCountList();
        PieChartData pieChartData = resultBean.getGuestsPieData();
        StatisticsHelper.hideView(sourcellViewsList);
        if (data == null || data.size() <= 0) {
            pcvOriginStatistics.setPieChartData(null);
            return;
        }
        int totalNum = 0;

        for (int i = 0; i < data.size(); i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
        }
        double totalPercent = 0;
        for (int i = 0; i < data.size(); i++) {
            sourcellViewsList.get(i).setVisibility(View.VISIBLE);

            double currentNum = Double.parseDouble(data.get(i).getCountNum());
            double ratio = StatisticsHelper.calculateRatio(currentNum, totalNum, totalPercent, i == data.size() - 1,
                    0, BigDecimal.ROUND_FLOOR);

            //设置比率
            sourceContentRatio.get(i).setText(" " + data.get(i).getCountNum() + "人(" + (int)ratio + "%)");
            //设置label
            sourceContent.get(i).setText(data.get(i).getName());
            //重绘比率的tv的大小
            StatisticsHelper.resetTextViewWidth(sourceContentRatio.get(i),sourceContent.get(i), LAYOUT_WIDTH);
            //累加比率
            totalPercent = totalPercent + ratio;
        }
        pcvOriginStatistics.setPieChartData(pieChartData);
    }

    @Override
    public void handleAbroadStatistics(DataPreviewPresenterImpl.ResultBean resultBean) {
        List<TouristAbroadStatisticsBean> abroadStatisticsList = resultBean.getOrigenBean().getCustomFromCountList();
        if (abroadStatisticsList == null || abroadStatisticsList.size() <= 0) {
            abroadLineChart.setLineChartData(null);
            return;
        }
        int size = resultBean.getOrigenBean().getCustomFromCountList().get(0).getCustomFromCountBillList().size();
        abroadLineChart.setLineChartData(resultBean.getAbroadLineChartData());
        Viewport viewportMax = new Viewport(-0.5f, abroadLineChart.getMaximumViewport().top * 1.25f, size, 0);
        Viewport viewport = new Viewport(-0.1f, abroadLineChart.getMaximumViewport().top * 1.25f, size > 5 ? 5 :  size, 0);
        abroadLineChart.setMaximumViewport(viewportMax);
        abroadLineChart.setCurrentViewport(viewport);
    }

    @Override
    public void handleStayDayStatistics(DataPreviewPresenterImpl.ResultBean resultBean) {
        List<GuestSourceBeanWrapper.GuestSourceBean> teamStayDaysCountList = resultBean.getOrigenBean()
                .getTeamStayDaysCountList();
        if (teamStayDaysCountList == null || teamStayDaysCountList.size() <= 0) {
            stayDayLineChart.setLineChartData(null);
            return;
        }
        int size = resultBean.getOrigenBean().getTeamStayDaysCountList().size();
        stayDayLineChart.setLineChartData(resultBean.getStayDayLineChartData());
        Viewport viewportMax = new Viewport(-0.5f, stayDayLineChart.getMaximumViewport().top * 1.25f, size, 0);
        Viewport viewport = new Viewport(-0.1f, stayDayLineChart.getMaximumViewport().top * 1.25f, size > 5 ? 5 :
                size, 0);
        stayDayLineChart.setMaximumViewport(viewportMax);
        stayDayLineChart.setCurrentViewport(viewport);
    }


    //****************************error*********************************************
    @Override//旅游人天异常
    public void onPeopleAndDayError(int resultCode, String errorMsg) {
        peopleAndDayView.setColumnChartData(null);
        Logger.e(errorMsg);
    }

    @Override
    public void onError(@ResultCode int resultCode, String errorMsg) {
        ToastUtils.showToast(errorMsg);
        changeViewState(ERROR_STATE);
    }

    @Override//景点认证时间异常
    public void onScenicAuthTimeError(@ResultCode int resultCode, String errorMsg) {
        authTimeView.setColumnChartData(null);
        Logger.e(errorMsg);
    }

    @Override//年龄统计请求异常
    public void onGuestAgeError(@ResultCode int resultCode, String errorMsg) {
        customColumnChartView.setColumnChartData(null);
    }

    //***************************private***********************************************

    /**
     * 游客年龄统计
     */
    private void initGuestAgeStatistics() {
        TextView tvMore = (TextView) framelayoutAge.findViewById(R.id.tvMore);
        TextView tvTitle = (TextView) framelayoutAge.findViewById(R.id.tv_guide_title);
        tvTitle.setText(App.getResource().getStringArray(R.array.guestAgeConditions)[0]);
        customColumnChartView = (CustomColumnChartView) framelayoutAge.findViewById(R.id.statisticsView);
        tvMore.setVisibility(View.VISIBLE);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestAgeArrangeActivity.callActivity(getActivity());
            }
        });
        //柱形图
        customColumnChartView.setValueTouchEnabled(false);
        customColumnChartView.setZoomEnabled(false);
        customColumnChartView.setScrollEnabled(true);
        customColumnChartView.setHorizontalScrollBarEnabled(true);
        customColumnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
    }

    private void initListener() {
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter != null) {
                    mPresenter.onStart();
                    changeViewState(LOADING_STATE);
                }
            }
        });
    }

    private void changeViewState(int state) {
        switch (state) {
            case LOADING_STATE:
                loadView.setVisibility(View.VISIBLE);
                swpDatePreview.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case ERROR_STATE:
                loadView.setVisibility(View.GONE);
                swpDatePreview.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
            case CONTENT_STATE:
                loadView.setVisibility(View.GONE);
                swpDatePreview.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 今日景区View初始化
     */
    private void initScenicView() {
        scenic_statistics = (PieChartView) parentView.findViewById(R.id.scenic_statistics).findViewById(R.id
                .statistics_content_piechart);
        scenic_statistics.setChartRotationEnabled(false);
        scenic_statistics.setValueTouchEnabled(false);
        LinearLayout scenicLayout = (LinearLayout) parentView.findViewById(R.id.scenic_statistics);
        scenicContent = new ArrayList<>();
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel1));
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel2));
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel3));
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel4));
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel5));
        scenicContent.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_tracel6));
        scenicContentRatio = new ArrayList<>();
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio1));
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio2));
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio3));
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio4));
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio5));
        scenicContentRatio.add((TextView) scenicLayout.findViewById(R.id.statistics_content_tv_ratio6));

        scenicRlViewsList.add(statistics_content_rl1);
        scenicRlViewsList.add(statistics_content_rl2);
        scenicRlViewsList.add(statistics_content_rl3);
        scenicRlViewsList.add(statistics_content_rl4);
        scenicRlViewsList.add(statistics_content_rl5);
        scenicRlViewsList.add(statistics_content_rl6);
    }

    /**
     * 景区客流趋势分析
     */
    private void initSceneryPreview() {
        linearTextHint.add(0, tvScenicName1);
        linearTextHint.add(1, tvScenicName2);
        linearTextHint.add(2, tvScenicName3);
        linearTextHint.add(3, tvScenicName4);

        linearColorHint.add(0, ivColorBlue);
        linearColorHint.add(1, ivColorRed);
        linearColorHint.add(2, ivColorPurple);
        linearColorHint.add(3, ivColorLightBlue);
        lineChart.setZoomEnabled(false);
        lineChart.setScrollEnabled(true);
    }

    /**
     * 客源地View初始化
     */
    private void initSourceView() {
        pcvOriginStatistics = (PieChartView) parentView.findViewById(R.id.item_team_statistics).findViewById(R.id
                .statistics_content_piechart);
        TextView rl_tv_more = (TextView) parentView.findViewById(R.id.item_team_statistics).findViewById(R.id
                .rl_tv_more);
        rl_tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestSourceStatisticsActivity.callActivity(getActivity());//
            }
        });

        rl_tv_more.setVisibility(View.VISIBLE);
        pcvOriginStatistics.setChartRotationEnabled(false);
        pcvOriginStatistics.setValueTouchEnabled(false);
        LinearLayout sourceLayout = (LinearLayout) parentView.findViewById(R.id.item_team_statistics);
        sourceContent = new ArrayList<>();
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel1));
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel2));
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel3));
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel4));
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel5));
        sourceContent.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_tracel6));
        sourceContentRatio = new ArrayList<>();
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio1));
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio2));
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio3));
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio4));
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio5));
        sourceContentRatio.add((TextView) sourceLayout.findViewById(R.id.statistics_content_tv_ratio6));

        sourcellViewsList.add(statistics_content_ll1);
        sourcellViewsList.add(statistics_content_ll2);
        sourcellViewsList.add(statistics_content_ll3);
        sourcellViewsList.add(statistics_content_ll4);
        sourcellViewsList.add(statistics_content_ll5);
        sourcellViewsList.add(statistics_content_ll6);
    }

    /**
     * 境内外游客对比
     */
    private void initAbroadStatistics() {
        RelativeLayout abroadLayout = (RelativeLayout) parentView.findViewById(R.id.abroad_statistics);
        abroadLineChart = (LineChartView) abroadLayout.findViewById(R.id.lineChart);
        TextView abroadLineChartTitle = (TextView) abroadLayout.findViewById(R.id.lineChart_title);
        LinearLayout lineChart_ll_hint = (LinearLayout) abroadLayout.findViewById(R.id.lineChart_ll_hint);
        TextView abroadTvScenicName1 = (TextView) abroadLayout.findViewById(R.id.tvScenicName1);
        TextView abroadTvScenicName2 = (TextView) abroadLayout.findViewById(R.id.tvScenicName2);
        ImageView ivColorBlue = (ImageView) abroadLayout.findViewById(R.id.ivColorBlue); //标题2
        ImageView ivColorRed = (ImageView) abroadLayout.findViewById(R.id.ivColorRed); //标题2
        TextView lineChartView_unit = (TextView) abroadLayout.findViewById(R.id.lineChartView_unit);

        ivColorBlue.setVisibility(View.VISIBLE);
        ivColorRed.setVisibility(View.VISIBLE);
        abroadTvScenicName1.setVisibility(View.VISIBLE);
        abroadTvScenicName2.setVisibility(View.VISIBLE);

        abroadTvScenicName1.setText("境内游客");
        abroadTvScenicName2.setText("境外游客");
        abroadLineChartTitle.setText("境内境外游客数量对比图");
        lineChartView_unit.setText("游客数(人)");
        lineChart_ll_hint.setVisibility(View.GONE);

        abroadLineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        abroadLineChart.setZoomEnabled(false);
        abroadLineChart.setScrollEnabled(false);
        abroadLineChart.setValueSelectionEnabled(true);

    }

    /**
     * 团队停留天数统计
     */
    private void initStayDayStatistics() {
        RelativeLayout abroadLayout = (RelativeLayout) parentView.findViewById(R.id.stayday_statistics);
        stayDayLineChart = (LineChartView) abroadLayout.findViewById(R.id.lineChart);
        TextView stayDayLineChartTitle = (TextView) abroadLayout.findViewById(R.id.lineChart_title);
        LinearLayout lineChart_ll_hint_stayDay = (LinearLayout) abroadLayout.findViewById(R.id.lineChart_ll_hint);
        TextView stayDayTvScenicName1 = (TextView) abroadLayout.findViewById(R.id.tvScenicName1);
        TextView lineChartView_unit = (TextView) abroadLayout.findViewById(R.id.lineChartView_unit);
        ImageView ivColorBlue = (ImageView) abroadLayout.findViewById(R.id.ivColorBlue); //标题2

        ivColorBlue.setVisibility(View.VISIBLE);
        stayDayTvScenicName1.setVisibility(View.VISIBLE);

        stayDayTvScenicName1.setText("团队数量");
        stayDayLineChartTitle.setText("团队停留天数统计");
        lineChartView_unit.setText("团队数(个)");
        lineChart_ll_hint_stayDay.setVisibility(View.GONE);

        stayDayLineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        stayDayLineChart.setZoomEnabled(false);
        stayDayLineChart.setScrollEnabled(true);
        stayDayLineChart.setValueSelectionEnabled(true);

    }

    /**
     * 景区认证时间分布
     */
    private void initScenicAuthTimeStatistics() {
        authTimeView = (ColumnChartView) scnicAuthTimeStatistics.findViewById(R.id.ccv_guide);
        //柱形图
        authTimeView.setValueTouchEnabled(false);
        authTimeView.setZoomEnabled(false);
        authTimeView.setScrollEnabled(true);
        authTimeView.setHorizontalScrollBarEnabled(true);
        authTimeView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        TextView ScenicAuthTimeUnit = (TextView) scnicAuthTimeStatistics.findViewById(R.id.tvUnit);
        ScenicAuthTimeUnit.setText(App.getResourceString(R.string.auth_time_unit));

        TextView tvGuideTitle = (TextView) scnicAuthTimeStatistics.findViewById(R.id.tv_guide_title);
        tvGuideTitle.setText(App.getResourceString(R.string.auth_time_hint));

        TextView tvMoreAuthTime = (TextView) scnicAuthTimeStatistics.findViewById(R.id.tvMore);
        tvMoreAuthTime.setVisibility(View.VISIBLE);
        tvMoreAuthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthTimeDistributionActivity.callActivity(getActivity());
            }
        });
    }

    /**
     * 旅游人.天趋势分析(柱形图)
     */
    private void initPeopleAndDayTendencyView() {
        peopleAndDayView = (ColumnChartView) flPeopleAndDayView.findViewById(R.id.ccv_guide);
        //柱形图
        peopleAndDayView.setValueTouchEnabled(false);
        peopleAndDayView.setZoomEnabled(false);
        peopleAndDayView.setScrollEnabled(true);
        peopleAndDayView.setHorizontalScrollBarEnabled(true);
        peopleAndDayView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        TextView unit = (TextView) flPeopleAndDayView.findViewById(R.id.tvUnit);
        unit.setText(App.getResourceString(R.string.people_day_unit));

        TextView hint = (TextView) flPeopleAndDayView.findViewById(R.id.tv_guide_title);
        hint.setText(App.getResourceString(R.string.people_day_hint));

    }

    /**
     * 趋势分析
     */
    private void tendencyAnalyse() {
        ccvTrendStatistics = (ColumnChartView) parentView.findViewById(R.id.item_guide_statistics).findViewById(R.id
                .ccv_guide);
        ccvTrendStatistics.setValueTouchEnabled(false);
        ccvTrendStatistics.setZoomEnabled(false);

        TextView scenic_textView = (TextView) parentView.findViewById(R.id.scenic_statistics).findViewById(R.id
                .tv_guide_title);
        scenic_textView.setText("今日景区认证");
        TextView tvOriginTitle = (TextView) parentView.findViewById(R.id.item_team_statistics).findViewById(R.id
                .tv_guide_title);
        tvOriginTitle.setText("今日客源地总览");
        TextView tvTrendTitle = (TextView) parentView.findViewById(R.id.item_guide_statistics).findViewById(R.id
                .tv_guide_title);
        tvTrendTitle.setText("团队趋势分析");
    }

    /**
     * 性别统计饼图
     */
    private void initSexStatisticsPie() {
        sex_statistics = (PieChartView) parentView.findViewById(R.id.sex_statistics).findViewById(R.id
                .statistics_content_piechart);
        sex_statistics.setChartRotationEnabled(false);
        sex_statistics.setValueTouchEnabled(false);

        TextView tvSexTitle = (TextView) parentView.findViewById(R.id.sex_statistics).findViewById(R.id.tv_guide_title);
        tvSexTitle.setText("今日游客性别比例");
        TextView tvMore = (TextView) parentView.findViewById(R.id.sex_statistics).findViewById(R.id.rl_tv_more);
        tvMore.setVisibility(View.VISIBLE);
        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TouristSexStatisticsActivity.callActivity(getActivity());
            }
        });

        LinearLayout sexLayout = (LinearLayout) parentView.findViewById(R.id.sex_statistics);
        sexContent = new ArrayList<>();
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel1));
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel2));
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel3));
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel4));
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel5));
        sexContent.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_tracel6));
        sexContentRatio = new ArrayList<>();
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio1));
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio2));
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio3));
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio4));
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio5));
        sexContentRatio.add((TextView) sexLayout.findViewById(R.id.statistics_content_tv_ratio6));

        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll1));
        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll2));
        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll3));
        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll4));
        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll5));
        sexllViewsList.add((LinearLayout) sexLayout.findViewById(R.id.statistics_content_ll6));

        View sextitleColor = sexLayout.findViewById(R.id.statistics_content_Color2);
        sextitleColor.setBackground(getResources().getDrawable(R.drawable.chart_solid_orange_light_3dp));

    }

    /**
     * 性别饼形图
     *
     * @param data 景区数据
     */
    private void showSexStatisticsData(List<TouristSexStatisticsBean> data, PieChartData pieChartData) {
        StatisticsHelper.hideView(sexllViewsList);
        if (data == null || data.size() <= 0) {
            sex_statistics.setPieChartData(null);
            return;
        }
        int size;
        if (data.size() >= 6) {
            size = 6;
        } else {
            size = data.size();
        }
        int totalNum = 0;
        for (int i = 0; i < size; i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
        }
        double totalPercent = 0;
        for (int i = 0; i < data.size(); i++) {
            sexllViewsList.get(i).setVisibility(View.VISIBLE);

            double currentNum = Double.parseDouble(data.get(i).getCountNum());
            double ratio = StatisticsHelper.calculateRatio(currentNum, totalNum, totalPercent, i == data.size() - 1,
                    0, BigDecimal.ROUND_FLOOR);

            //设置比率
            sexContentRatio.get(i).setText(" " + data.get(i).getCountNum() + "人(" + (int)ratio + "%)");
            //设置label
            sexContent.get(i).setText(data.get(i).getName());
            //重绘比率的tv的大小
            StatisticsHelper.resetTextViewWidth(sexContentRatio.get(i),sexContent.get(i), LAYOUT_WIDTH);

            //累加比率
            totalPercent = totalPercent + ratio;
        }
        pieChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.text_size_12)));
        pieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.text_size_12)));
        pieChartData.setCenterText1Color(getResources().getColor(R.color.colorPrimary));
        pieChartData.setCenterText2Color(getResources().getColor(R.color.colorPrimary));
        sex_statistics.setPieChartData(pieChartData);
    }
}
