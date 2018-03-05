package com.msy.globalaccess.business.navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.login.LoginActivity;
import com.msy.globalaccess.business.main.ui.AgentMainActivity;
import com.msy.globalaccess.business.main.ui.TourismMainActivity;
import com.msy.globalaccess.business.navigation.contract.NavigationContract;
import com.msy.globalaccess.business.navigation.contract.impl.NavigationPresenterImpl;
import com.msy.globalaccess.common.LoginType;
import com.msy.globalaccess.common.UserRoleType;
import com.msy.globalaccess.config.BannerSetting;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean_Old;
import com.msy.globalaccess.data.bean.user.User;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.utils.helper.GlideHelper;
import com.msy.globalaccess.utils.helper.StatisticsHelper;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static com.msy.globalaccess.base.App.getResource;

/**
 * Created by pepys on 2017/5/22.
 * description:
 */
public class NavigationActivity extends BaseActivity implements NavigationContract.View {

    //手动计算出来的布局宽度,别问怎么算出来的,德斌算的
    private static final int LAYOUT_WIDTH = DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(207);
    @Inject
    NavigationPresenterImpl presenter;
    //---------------旅行社统计比例-------------------------
    @BindView(R.id.statistics_content_rl1)
    RelativeLayout statistics_content_ll1;
    @BindView(R.id.statistics_content_rl2)
    RelativeLayout statistics_content_ll2;
    @BindView(R.id.statistics_content_rl3)
    RelativeLayout statistics_content_ll3;
    @BindView(R.id.statistics_content_rl4)
    RelativeLayout statistics_content_ll4;
    @BindView(R.id.statistics_content_rl5)
    RelativeLayout statistics_content_ll5;
    @BindView(R.id.statistics_content_rl6)
    RelativeLayout statistics_content_ll6;
    @BindView(R.id.statistics_content_tv_tracel1)
    TextView statisticsContentTvTracel1;
    @BindView(R.id.statistics_content_tv_tracel2)
    TextView statisticsContentTvTracel2;
    @BindView(R.id.statistics_content_tv_tracel3)
    TextView statisticsContentTvTracel3;
    @BindView(R.id.statistics_content_tv_tracel4)
    TextView statisticsContentTvTracel4;
    @BindView(R.id.statistics_content_tv_tracel5)
    TextView statisticsContentTvTracel5;
    @BindView(R.id.statistics_content_tv_tracel6)
    TextView statisticsContentTvTracel6;
    @BindView(R.id.statistics_content_tv_ratio1)
    TextView statistics_content_tv_ratio1;
    @BindView(R.id.statistics_content_tv_ratio2)
    TextView statistics_content_tv_ratio2;
    @BindView(R.id.statistics_content_tv_ratio3)
    TextView statistics_content_tv_ratio3;
    @BindView(R.id.statistics_content_tv_ratio4)
    TextView statistics_content_tv_ratio4;
    @BindView(R.id.statistics_content_tv_ratio5)
    TextView statistics_content_tv_ratio5;
    @BindView(R.id.statistics_content_tv_ratio6)
    TextView statistics_content_tv_ratio6;
    //---------------------------------------
    @BindView(R.id.navigation_sRefresh)
    SwipeRefreshLayout navigation_sRefresh;
    /**
     * 旅行社饼图
     */
    @BindView(R.id.statistics_content_piechart)
    PieChartView statisticsContentPiechart;
    /**
     * 导游柱形图
     */
    @BindView(R.id.ccv_guide)
    ColumnChartView ccvGuide;
    @BindView(R.id.banner_guide)
    Banner bannerGuide;
    int teamTotal = 0;
    /**
     * 旅行社名称
     */
    private ArrayList<TextView> tvViews = new ArrayList<>();
    /**
     * 旅行社比例
     */
    private ArrayList<RelativeLayout> rlViewsList = new ArrayList<>();
    private ArrayList<TextView> ratioViews = new ArrayList<>();
    /**
     * 轮播图数据
     */
    private List<String> images = new ArrayList<>();
    /**
     * 页面数据
     */
    private NavigationDataBean mnNavigationData;
    /**
     * 数字格式化
     */
    private DecimalFormat df = new DecimalFormat("0");
    private SmallDialog smallDialog;

    public static void callActivity(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
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
        smallDialog = new SmallDialog(this);
        initHeaderView();
        initPieChartDate();
        initColumnChartDate();
        navigation_sRefresh.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        navigation_sRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadNavigationData();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        bannerGuide.stopAutoPlay();
    }

    //初始化拼图的一些数据
    private void initPieChartDate() {

        tvViews.add(statisticsContentTvTracel1);
        tvViews.add(statisticsContentTvTracel2);
        tvViews.add(statisticsContentTvTracel3);
        tvViews.add(statisticsContentTvTracel4);
        tvViews.add(statisticsContentTvTracel5);
        tvViews.add(statisticsContentTvTracel6);

        ratioViews.add(statistics_content_tv_ratio1);
        ratioViews.add(statistics_content_tv_ratio2);
        ratioViews.add(statistics_content_tv_ratio3);
        ratioViews.add(statistics_content_tv_ratio4);
        ratioViews.add(statistics_content_tv_ratio5);
        ratioViews.add(statistics_content_tv_ratio6);

        rlViewsList.add(statistics_content_ll1);
        rlViewsList.add(statistics_content_ll2);
        rlViewsList.add(statistics_content_ll3);
        rlViewsList.add(statistics_content_ll4);
        rlViewsList.add(statistics_content_ll5);
        rlViewsList.add(statistics_content_ll6);


        statisticsContentPiechart.setChartRotationEnabled(false);
        //是否允许点击操作
        statisticsContentPiechart.setValueTouchEnabled(false);
        //设置是否可以选中图表中的值，即当点击图表中的数据值后，会一直处于选中状态，直到用户点击其他。
        statisticsContentPiechart.setValueSelectionEnabled(false);
    }

    private void initColumnChartDate() {
        ccvGuide.setValueTouchEnabled(false);
        ccvGuide.setZoomEnabled(false);
        ccvGuide.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
    }

    private void initHeaderView() {
        bannerGuide.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        bannerGuide.setIndicatorGravity(BannerConfig.CENTER);
        bannerGuide.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideHelper.urlToImageView(context, imageView, (String) path, 0);
            }
        });
        bannerGuide.isAutoPlay(true);
        bannerGuide.setDelayTime(BannerSetting.DELAY_TIME);
        images.add("");
        bannerGuide.setImages(images);
        bannerGuide.start();
    }

    @OnClick(R.id.cv_guide_travel)
    void TravelClick() {
        //旅行社
        IntentAction(UserRoleType.JOURNEY);
    }

    @OnClick(R.id.cv_guide_tourism)
    void TourismClick() {
        //旅游局
        IntentAction(UserRoleType.TOURIST_ADMIN);
    }

    /**
     * 用户角色类型
     *
     * @param type
     */
    private void IntentAction(String type) {
        if (UserHelper.getUserState()) {
            if (type.equals(UserHelper.getInstance().getUser().getUserRoleType())) {
                if (type.equals(UserRoleType.JOURNEY)) {
                    AgentMainActivity.callActivity(NavigationActivity.this);
                } else {
                    TourismMainActivity.callActivity(NavigationActivity.this);
                }
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            } else {
                showPop(type);
            }
        } else {
            LoginActivity.callActivity(this, type);
            overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
        }
    }

    /**
     * 显示pop
     */
    private void showPop(final String type) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage("您确定要切换帐号吗？", 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = UserHelper.getInstance().getUser();
                user.setUserLoginStatus(LoginType.STATUS_LOGOUT);
                UserHelper.getInstance().updateUser(user);
                UserHelper.getInstance().setUser(null);
                LoginActivity.callActivity(NavigationActivity.this, type);
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
    public void LoadNavigationSuccess(NavigationDataBean navigationData) {
        mnNavigationData = navigationData;
        showBannerData(navigationData.getPicUrl(), navigationData.getPicNum());
        showTeamData(navigationData);
        showTravelData(navigationData);
    }

    @Override
    public void LoadNavigationFail() {
    }

    @Override
    @Deprecated
    public void getGuideData(NavigationDataBean_Old navigationDataBeanOld) {
    }

    /**
     * 显示轮播图数据
     */
    private void showBannerData(String url, int num) {
        images.clear();
        //因为每次返回的图片路径都是一样的，所以不知道有没有更新图片，只能每次都清除缓存
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(App.getAppContext()).clearDiskCache();
            }
        }).start();
        Glide.get(App.getAppContext()).clearMemory();
        if (num > 0) {
            for (int i = 1; i <= num; i++) {
                images.add("http://" + url + i + ".png");
            }
        }
        bannerGuide.setImages(images);
        bannerGuide.start();
    }

    /**
     * 旅行社饼图
     *
     * @param data
     */
    private void showTeamData(NavigationDataBean data) {
        StatisticsHelper.hideView(rlViewsList);
        if (data.getStatisticsDataBean().getPieChartData() != null) {
            teamCount(data.getTravelAgentTeamCountList());
            int totalNum = 0;
            double totalRatio = 0;
            double ratio;
            int size = data.getTravelAgentTeamCountList().size();

            for (int i = 0; i < size; i++) {
                totalNum += data.getTravelAgentTeamCountList().get(i).getCountNum();
                //                if (i < 5) {
                //                    ratio = Math.floor((double) data.getTravelAgentTeamCountList().get(i)
                // .getCountNum() / teamTotal *
                //                            100);
                //                    totalRatio += ratio;
                //                } else {
                //                    if (totalRatio > 0) {
                //                        ratio = 100 - totalRatio;
                //                    } else {
                //                        ratio = Math.floor((double) data.getTravelAgentTeamCountList().get(i)
                // .getCountNum() /
                //                                teamTotal * 100);
                //                    }
                //                }
                Double currentNum = data.getTravelAgentTeamCountList().get(i).getCountNum() * 1.0;
                ratio = StatisticsHelper.calculateRatio(currentNum, teamTotal, totalRatio, i >= 5, 0,
                        BigDecimal.ROUND_FLOOR);
                totalRatio += ratio;
                ratioViews.get(i).setText("(" + (int)ratio + "%)");
                rlViewsList.get(i).setVisibility(View.VISIBLE);
                tvViews.get(i).setText(data.getTravelAgentTeamCountList().get(i).getName());

                StatisticsHelper.resetTextViewWidth(ratioViews.get(i), tvViews.get(i), LAYOUT_WIDTH);
            }
            if (totalNum > 0) {
                data.getStatisticsDataBean().getPieChartData().setCenterText1(totalNum + "团队");
                data.getStatisticsDataBean().getPieChartData().setCenterText1Color(getResource().getColor(R.color
                        .colorPrimary));
            }
            statisticsContentPiechart.setPieChartData(data.getStatisticsDataBean().getPieChartData());
        }
    }

    private int teamCount(ArrayList<NavigationDataBean.SubBean> data) {
        teamTotal = 0;
        for (int i = 0; i < data.size(); i++) {
            teamTotal += data.get(i).getCountNum();
        }
        if (teamTotal == 0) {
            teamTotal = 1;
        }
        return teamTotal;
    }

    /**
     * 导游柱状图
     */
    private void showTravelData(NavigationDataBean data) {
        if (data.getStatisticsDataBean().getColumnChartData() != null) {
            int size = data.getGuideTeamCountList().size();
            ccvGuide.setColumnChartData(data.getStatisticsDataBean().getColumnChartData());
            Viewport viewportMax = new Viewport(-0.5f, ccvGuide.getMaximumViewport().height() * 1.25f, size, 0);
            ccvGuide.setMaximumViewport(viewportMax);
            if (size > 7) {
                Viewport viewport = new Viewport(0, ccvGuide.getMaximumViewport().height() * 1.25f, size > 7 ? 7 :
                        size, 0);
                ccvGuide.setCurrentViewport(viewport);
                ccvGuide.moveTo(0, 0);
            }
        }

    }

    @Override
    public void showProgress() {
        navigation_sRefresh.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        navigation_sRefresh.setRefreshing(false);
    }

    @Override
    public String getStartDate() {
        return TimeFormat.getCurrentMonthStartDay();
    }

    @Override
    public String getEndDate() {
        return TimeFormat.getCurrentMonthEndDay();
    }

}
