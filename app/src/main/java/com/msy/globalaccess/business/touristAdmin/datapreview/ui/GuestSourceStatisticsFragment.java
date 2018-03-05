package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.GuestSourceStaticticsContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.GuestSourceStaticticsPresenter;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.StatisticsHelper;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 客源地统计
 */

@SuppressWarnings({"WrongConstant", "deprecation"})
public class GuestSourceStatisticsFragment extends BaseFragment implements IUpdateable, GuestSourceStaticticsContract
        .View {
    public static final String ALL_STATISTICS = "";
    public static final String INBOUND_STATISTICS = "0";
    public static final String OUTBOUND_STATISTICS = "1";
    private static final String KEY_TYPE = "type";
    @Inject
    GuestSourceStaticticsPresenter mPresenter;
    ArrayList<String> conditions;
    @BindView(R.id.tvSelectedCondition)
    TextView tvSelectedCondition;
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
    @BindView(R.id.statistics_content_piechart)
    PieChartView pcvOriginStatistics;
    @BindView(R.id.rlStaticticsHint)
    RelativeLayout rlStaticticsHint;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.viewSplit)
    View viewSplit;
    @BindView(R.id.statistics_content_ll_text)
    LinearLayout statistics_content_ll_text;
    @StaticticsType //境内的或者境外的
    private String currentType = ALL_STATISTICS;
    private WheelViewDialog<String> chooserDialog;
    //-----------------客源地饼形图-------------
    private List<TextView> sourceContentRatio;
    private List<TextView> sourceContent;
    private ArrayList<LinearLayout> sourcellViewsList = new ArrayList<>();
    /**
     * 数字格式化
     */
    private DecimalFormat df = new DecimalFormat("0");
    private int conditonPos = 0;
    private Drawable arrowUp, arrowDown, left;

    /**
     * 获取实例
     *
     * @param type 查询类型
     * @return 返回对象
     */
    public static GuestSourceStatisticsFragment newInstance(@StaticticsType String type) {
        GuestSourceStatisticsFragment fragment = new GuestSourceStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {
        getFragmentComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public void init(View view) {
        viewSplit.setVisibility(View.GONE);
        srl.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        srl.setEnabled(false);
        currentType = getArguments().getString(KEY_TYPE);

        arrowUp = App.getResource().getDrawable(R.mipmap.icon_arrow_up1);
        arrowUp.setBounds(0, 0, arrowUp.getIntrinsicWidth(), arrowUp.getIntrinsicHeight());
        left = App.getResource().getDrawable(R.mipmap.icon_filter);
        left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        arrowDown = App.getResource().getDrawable(R.mipmap.icon_arrow_down1);
        arrowDown.setBounds(0, 0, arrowDown.getIntrinsicWidth(), arrowDown.getIntrinsicHeight());

        initSourceView(view);

        initDialog(currentType);
        initListener();
        mPresenter.onStart();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_guest_source_statistics;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return mPresenter;
    }

    @Override
    public void update() {
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void showProgress() {
        srl.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srl.setRefreshing(false);
    }

    @Override
    public String getSourceType() {
        return currentType;
    }

    @Override
    public String getStartTime() {
        switch (conditonPos) {
            case 0://今日客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.TODAY, TimeFormat.regular7)[0];
            case 1://近一周客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_WEEK, TimeFormat.regular7)[0];
            case 2://近一月客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_MOHTH, TimeFormat.regular7)[0];
            case 3://近三月客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_THREE_MONTH, TimeFormat.regular7)[0];
            case 4://近六月客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_SIX_MONTH, TimeFormat.regular7)[0];
            case 5://近一年客源地总览
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_YEAR, TimeFormat.regular7)[0];
        }
        Logger.e("条件异常");
        return "";
    }

    @Override
    public String getEndTime() {
        return TimeFormat.getCurrentDate(TimeFormat.regular7);
    }

    @Override
    public void handlerGuestSourceData(GuestSourceBeanWrapper.ResultBean resultBean) {
        showOriginStatisticsData(resultBean.getOrigin(), resultBean.getPieChartData());
    }


    //*************************************private***********************************************

    @Override
    public void onGuestSourceError(String errorMsg, int errorCode) {
        ToastUtils.showToast(errorMsg);
        pcvOriginStatistics.setPieChartData(null);
        statistics_content_ll_text.setVisibility(View.GONE);
    }

    /**
     * 显示客源地总览饼形图
     */
    private void showOriginStatisticsData(List<GuestSourceBeanWrapper.GuestSourceBean> data, PieChartData
            pieChartData) {

        StatisticsHelper.hideView(sourcellViewsList);
        if (data == null || data.size() <= 0) {
            statistics_content_ll_text.setVisibility(View.GONE);
            pcvOriginStatistics.setPieChartData(null);
            return;
        }
        statistics_content_ll_text.setVisibility(View.VISIBLE);
        int totalNum = 0;
        double totalPercent = 0;
        int layoutWidth = DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(207);

        for (int i = 0; i < data.size(); i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
        }

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
            StatisticsHelper.resetTextViewWidth(sourceContentRatio.get(i),sourceContent.get(i), layoutWidth);

            //累加比率
            totalPercent = totalPercent + ratio;
        }
        pcvOriginStatistics.setPieChartData(pieChartData);
    }

    private void initDialog(String currentType) {
        switch (currentType) {
            case ALL_STATISTICS:
                conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
                        .guestStaticticsConditions)));
                break;
            case INBOUND_STATISTICS:
                conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
                        .guestInboundStaticticsConditions)));
                break;
            case OUTBOUND_STATISTICS:
                conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
                        .guestOutboundStaticticsConditions)));
                break;
        }

        chooserDialog = new WheelViewDialog<>(getActivity(), conditions, new WheelViewDialog
                .onWheelViewPickedListener1<String>() {
            @Override
            public void onPicked(String pickedItem, int position) {
                conditonPos = position;
                tvSelectedCondition.setText(pickedItem);
                if (mPresenter != null) {
                    mPresenter.onStart();
                }
            }
        });
        chooserDialog.setCanceledOnTouchOutside(true);
        tvSelectedCondition.setText(conditions.get(0));
    }

    /**
     * 客源地View初始化
     */
    private void initSourceView(View parentView) {
        rlStaticticsHint.setVisibility(View.GONE);
        pcvOriginStatistics.setChartRotationEnabled(false);
        pcvOriginStatistics.setValueTouchEnabled(false);
        LinearLayout sourceLayout = (LinearLayout) parentView.findViewById(R.id.item_guest_source);

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

    private void initListener() {
        tvSelectedCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooserDialog != null) {
                    tvSelectedCondition.setCompoundDrawables(left, null, arrowUp, null);
                    chooserDialog.show();
                }
            }
        });
        chooserDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tvSelectedCondition.setCompoundDrawables(left, null, arrowDown, null);
            }
        });
    }

    //************************************************************************************
    @StringDef({ALL_STATISTICS, INBOUND_STATISTICS, OUTBOUND_STATISTICS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StaticticsType {

    }

}
