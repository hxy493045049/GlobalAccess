package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.NavigationDataApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean_Old;
import com.msy.globalaccess.data.interactor.INavigationDataInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

import static com.msy.globalaccess.base.App.getResource;

/**
 * Created by WuDebin on 2017/2/21.
 */

public class NavigationDataInteractorImpl implements INavigationDataInteractor {

    private NavigationDataApi navigationDataApi;

    /**
     * 饼图的六种颜色
     */
    private int[] pieColors = new int[]{
            R.color.guide_pie_blue_theme,
            R.color.guide_pie_green,
            R.color.guide_pie_orange,
            R.color.guide_pie_yellow,
            R.color.guide_pie_sky_blue,
            R.color.guide_pie_deep_blue
    };

    @Inject
    NavigationDataInteractorImpl(Retrofit retrofit) {
        navigationDataApi = retrofit.create(NavigationDataApi.class);
    }


    @Override
    public Subscription loadNavigationData(final RequestCallBack callBack, String startDate, String endDate) {
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(NavigationDataApi.statistics_index_method)
                .setParam(NavigationDataApi.teamCreateStartDate, startDate)
                .setParam(NavigationDataApi.teamCreateEndDate, endDate)
                .build().getParam();
        return navigationDataApi.getNavigationData(params)
                .compose(RxJavaUtils.<BaseBean<NavigationDataBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<NavigationDataBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<NavigationDataBean>, BaseBean<NavigationDataBean>>() {
                    @Override
                    public BaseBean<NavigationDataBean> call(BaseBean<NavigationDataBean> navigationDataBeanBaseBean) {
                        NavigationDataBean.StatisticsDataBean statisticsDataBean = new NavigationDataBean().new StatisticsDataBean();
                        PieChartData pieChartData = changeStatisticsData(navigationDataBeanBaseBean.getData().getTravelAgentTeamCountList());
                        ColumnChartData columnChartData = changeTravelStatisticsData(navigationDataBeanBaseBean.getData().getGuideTeamCountList());
                        statisticsDataBean.setPieChartData(pieChartData);
                        statisticsDataBean.setColumnChartData(columnChartData);
                        navigationDataBeanBaseBean.getData().setStatisticsDataBean(statisticsDataBean);
                        return navigationDataBeanBaseBean;
                    }
                })
                .subscribe(new Subscriber<BaseBean<NavigationDataBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        Logger.e(e,"NavigationDataInteractorImpl");
                        ToastUtils.showToast(R.string.internet_error);
                    }

                    @Override
                    public void onNext(BaseBean<NavigationDataBean> navigationDataBeanBaseBean) {
                        if (navigationDataBeanBaseBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(navigationDataBeanBaseBean.getData());
                        } else {
                            ToastUtils.showToast(navigationDataBeanBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, navigationDataBeanBaseBean.getMessage());
                        }
                    }
                });
    }

    private PieChartData changeStatisticsData(ArrayList<NavigationDataBean.SubBean> data){
        PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setCenterText1Color(getResource().getColor(R.color.text_guide_blue));
        pieChartData.setSlicesSpacing(0);

        if (data == null || data.size() <= 0) {
            return null;
        }
        List<SliceValue> sliceValues = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (i >= 6) {
                break;
            }
            SliceValue sliceValue = new SliceValue();
            float value=data.get(i).getCountNum();
            if (value == 0) {
                value = 0.000001f;
            }
            sliceValue.setValue(value);
            sliceValue.setColor(App.getResource().getColor(pieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setCenterText1FontSize(ChartUtils.px2sp(App.getResource().getDisplayMetrics().scaledDensity, (int) App.getResource().getDimension(R.dimen.text_size_12)));
        pieChartData.setValues(sliceValues);
        return pieChartData;
    }

    private ColumnChartData changeTravelStatisticsData(ArrayList<NavigationDataBean.SubBean> data){
        ColumnChartData columnChartData = new ColumnChartData();
        columnChartData.setValueLabelBackgroundEnabled(false);
        columnChartData.setValueLabelsTextColor(getResource().getColor(R.color.guide_pie_blue_theme));
        columnChartData.setValueLabelTextSize(ChartUtils.px2sp(App.getResource().getDisplayMetrics().scaledDensity, (int) App.getResource().getDimension(R.dimen.chart_text_size)));
        if (data == null || data.size() <= 0) {
            return null;
        }
        int size = data.size();
        ArrayList<Column> columns = new ArrayList<>();
        ArrayList<AxisValue> axisValuesX = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            subcolumnValue.setValue(data.get(i).getCountNum());
            subcolumnValue.setColor(getResource().getColor(R.color.guide_pie_blue_theme));
            subcolumnValues.add(subcolumnValue);
            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            columns.add(column);
            String label = data.get(i).getName();
            if (size >= 4 && label.length() > 4) {
                label = label.substring(0, 4) + "..";
            }
            axisValuesX.add(new AxisValue(i).setLabel(label));
        }
        if (size >= 5) {
            columnChartData.setFillRatio(0.5f);//根据列数不同改变宽度
        } else if (size == 4) {
            columnChartData.setFillRatio(0.4f);
        } else if (size == 3) {
            columnChartData.setFillRatio(0.3f);
        } else if (size == 2) {
            columnChartData.setFillRatio(0.2f);
        } else if (size == 1) {
            columnChartData.setFillRatio(0.1f);
        }
        Axis axisY = new Axis()
                .setTextColor(getResource().getColor(R.color.text_primary_light))
                .setHasLines(true)
                .setHasSeparationLine(true);
        columnChartData.setAxisYLeft(axisY);
        Axis axisX = new Axis(axisValuesX)
                .setTextColor(getResource().getColor(R.color.text_primary_light))
                .setHasLines(false)
                .setHasSeparationLine(true)
                ;
        columnChartData.setAxisXBottom(axisX);

        columnChartData.setColumns(columns);
        return columnChartData;
    }
    /**
     * 旧版本首页。
     * @param callBack
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    @Deprecated
    public Subscription loadUsrData(final RequestCallBack callBack, String startDate, String endDate) {
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(NavigationDataApi.APP_STATISTICS)
                .setParam("searchStartDate", startDate)
                .setParam("searchEndDate", endDate)
                .build().getParam();
        return navigationDataApi.getGuideData(params).compose(RxJavaUtils.<BaseBean<NavigationDataBean_Old>>defaultSchedulers()).subscribe(new Subscriber<BaseBean<NavigationDataBean_Old>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                callBack.onError(ResultCode.NET_ERROR, "");
                ToastUtils.showToast(R.string.internet_error);
                Logger.e(e, "获取详情失败");
            }

            @Override
            public void onNext(BaseBean<NavigationDataBean_Old> guideDataBean) {
                if (guideDataBean.getStatus() == ResultCode.SUCCESS) {
                    callBack.success(guideDataBean.getData());
                } else {
                    ToastUtils.showToast(guideDataBean.getMessage());
                    callBack.onError(ResultCode.NET_ERROR, guideDataBean.getMessage());
                }
            }
        });
    }
}
