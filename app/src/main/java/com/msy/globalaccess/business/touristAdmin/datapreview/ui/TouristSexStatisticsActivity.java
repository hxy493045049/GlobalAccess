package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.TourismSexStatisticsContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.TourismSexStatisticsImpl;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsListBean;
import com.msy.globalaccess.utils.helper.StatisticsHelper;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

import static com.msy.globalaccess.utils.helper.TimeHelper.TODAY;


/**
 * Created by pepys on 2017/7/11
 * description:游客性别分析
 *
 */
public class TouristSexStatisticsActivity extends BaseActivity implements TourismSexStatisticsContract.View{


    //手动计算出来的布局宽度,别问怎么算出来的,德斌算的
    private static final int LAYOUT_WIDTH = DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(207);

    @Inject
    TourismSexStatisticsImpl mPresenter;

    @BindView(R.id.sex_statistics_tv_time)
    TextView sex_statistics_tv_time;

    @TimeHelper.TimeStrategy
    private int timeStrategy = TODAY;
    //------------------游客性别饼图---------------
    private List<TextView> sexContentRatio;
    private List<TextView> sexContent;
    private ArrayList<LinearLayout> sexllViewsList = new ArrayList<>();
    //----------------------------------------------
    private Drawable arrowDown, arrowUp;
    /**
     * loading
     */
    private SmallDialog smallDialog;
    /**
     * 时间选择内容
     */
    ArrayList<String> conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .sexStatisticsConditions)));
    /**
     * 数字格式化
     */
    private DecimalFormat df = new DecimalFormat("0");
    /**
     * 性别饼形图
     */
    private PieChartView sex_statistics;
    /**
     * 时间选择框
     */
    private WheelViewDialog timePickDialog;

    public static void callActivity(Context context){
        Intent intent = new Intent(context,TouristSexStatisticsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        basePresenter = mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tourist_sex_statistics;
    }


    @Override
    protected void init() {
        setTitle(App.getResourceString(R.string.tourist_sex_title));
        smallDialog = new SmallDialog(this);
        arrowDown = getResources().getDrawable(R.mipmap.icon_arrow_down1);
        arrowDown.setBounds(0, 0, arrowDown.getIntrinsicWidth(), arrowDown.getIntrinsicHeight());

        arrowUp = getResources().getDrawable(R.mipmap.icon_arrow_up1);
        arrowUp.setBounds(0, 0, arrowUp.getIntrinsicWidth(), arrowUp.getIntrinsicHeight());

        initSexStatisticsPie();
        initDialog();
    }

    @OnClick({R.id.sex_statistics_tv_time})
    public void onclickView(View view){
        switch (view.getId()){
            case R.id.sex_statistics_tv_time:
                timePickDialog.show();
                break;
        }
    }

    private void initDialog() {
        timePickDialog = new WheelViewDialog<>(this, conditions, new WheelViewDialog
                .onWheelViewPickedListener1<String>() {
            @Override
            public void onPicked(String pickedItem, int position) {
                sex_statistics_tv_time.setText(pickedItem);
                timeStrategy = position;
                if (mPresenter != null) {
                    mPresenter.loadSexStatistics();
                }
            }
        });
        timePickDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                sex_statistics_tv_time.setCompoundDrawables(null, null, arrowUp, null);
            }
        });
        timePickDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sex_statistics_tv_time.setCompoundDrawables(null, null, arrowDown, null);
            }
        });
    }

    @Override
    public void loadSexStatisticsSuccess(TouristSexStatisticsListBean data) {
        showSexStatisticsData(data.getCustomSexCountList(),data.getSexStatisticsPie());
    }

    @Override
    public void showProgress() {
        smallDialog.shows();
    }

    @Override
    public void hideProgress() {
        smallDialog.dismisss();
    }


    @Override
    public String[] getTimes() {
        return  TimeHelper.getTimeByStrategy(timeStrategy, TimeFormat.regular7);
    }


    @Override
    public void loadFailer() {

    }

    /**
     * 性别饼形图
     *
     * @param data 景区数据
     */
    public void showSexStatisticsData(List<TouristSexStatisticsBean> data, PieChartData pieChartData) {
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

    /**
     * 性别统计饼图
     */
    private void initSexStatisticsPie(){
        sex_statistics = (PieChartView) findViewById(R.id.statistics_content_piechart);
        sex_statistics.setChartRotationEnabled(false);
        sex_statistics.setValueTouchEnabled(false);

        sexContent = new ArrayList<>();
        sexContent.add((TextView) findViewById(R.id.statistics_content_tv_tracel1));
        sexContent.add((TextView) findViewById(R.id.statistics_content_tv_tracel2));
        sexContentRatio = new ArrayList<>();
        sexContentRatio.add((TextView) findViewById(R.id.statistics_content_tv_ratio1));
        sexContentRatio.add((TextView) findViewById(R.id.statistics_content_tv_ratio2));

        sexllViewsList.add((LinearLayout) findViewById(R.id.statistics_content_ll1));
        sexllViewsList.add((LinearLayout) findViewById(R.id.statistics_content_ll2));
    }
}
