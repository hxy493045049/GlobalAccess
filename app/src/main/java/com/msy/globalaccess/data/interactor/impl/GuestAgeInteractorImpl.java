package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.helper.StatisticsHelper;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.msy.globalaccess.widget.chartview.CustomAxis;

import org.greenrobot.greendao.annotation.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by shawn on 2017/7/18 0018.
 * <p>
 * description :
 */

@SuppressWarnings("deprecation")
public class GuestAgeInteractorImpl implements IStatisticsInteractor.IGuestAgeInteractor {
    private StatisticsApi.GuestAgeStatisticsApi api;
    private double totalRatio = 0;

    @Inject
    GuestAgeInteractorImpl(Retrofit retrofit) {
        api = retrofit.create(StatisticsApi.GuestAgeStatisticsApi.class);
    }

    @Override
    public Subscription getStatisticsData(@NotNull RequestCallBack<GuestAgeStatisticsBeanWrapper.ResultBean>
                                                  callBack, @NotNull HashMap<String, String> param) {

        callBack.beforeRequest();
        return api.getStatusData(param)
                .compose(RxJavaUtils.<BaseBean<GuestAgeStatisticsBeanWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<GuestAgeStatisticsBeanWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<GuestAgeStatisticsBeanWrapper>, GuestAgeStatisticsBeanWrapper
                        .ResultBean>() {
                    @Override
                    public GuestAgeStatisticsBeanWrapper.ResultBean call(BaseBean<GuestAgeStatisticsBeanWrapper> base) {
                        GuestAgeStatisticsBeanWrapper.ResultBean resultBean = new GuestAgeStatisticsBeanWrapper
                                .ResultBean();
                        List<GuestAgeStatisticsBeanWrapper.GuestAgeStatisticsBean> origin = base.getData()
                                .getCustomAgeCountList();
                        resultBean.setOrigin(origin);
                        resultBean.setColumnChartData(parseData(origin));
                        return resultBean;
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    private ColumnChartData parseData(List<GuestAgeStatisticsBeanWrapper.GuestAgeStatisticsBean> data) {
        totalRatio = 0;
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int size = data.size() > 100 ? 100 : data.size();
        if (size == 0) {
            return null;
        }
        int totalCount = 0;
        for (int i = 0; i < size; i++) {
            totalCount += Integer.parseInt(data.get(i).getCountNum());
        }


        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            subcolumnValues.add(getSubColumn(data.get(i), totalCount, i == size - 1));

            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            column.setFormatter(new SimpleColumnChartValueFormatter(2));
            columns.add(column);

            String label = data.get(i).getName();
            axisValues.add(new AxisValue(i).setLabel(handlerLabel(label)));
        }

        columnChartData.setColumns(columns);
        handlerColumnChartData(columnChartData, size);

        columnChartData.setAxisYLeft(getAxisY());
        columnChartData.setAxisXBottom(getAxisX(axisValues));
        return columnChartData;
    }

    private Axis getAxisY() {
        Axis axisY = new Axis()
                .setHasLines(true)
                .setHasSeparationLine(true)
                .setMaxLabelChars(3)
                .setTextSize(11)
                .setTextColor(App.getResource().getColor(R.color.text_primary_light));

        List<AxisValue> values = new ArrayList<>();
        for (int i = 0; i <= 100; i += 20) {
            AxisValue value = new AxisValue(i);
            String label = i + "";
            value.setLabel(label);
            values.add(value);
        }
        axisY.setValues(values);

        return axisY;
    }

    //处理X轴
    private Axis getAxisX(List<AxisValue> axisValues) {
        return new CustomAxis(axisValues)
                .setHasLines(false)
                .setHasSeparationLine(true)
                .setTextSize(11).setTextColor(App.getResource().getColor(R.color.text_primary_light));
    }

    //如果label包含"(", 那么在"("之前添加换行符
    private String handlerLabel(String str) {
        if (str.contains("(")) {
            StringBuilder sb = new StringBuilder(str);
            sb.insert(str.indexOf("("), "\n");
            return sb.toString();
        } else if (str.contains("（")) {
            StringBuilder sb = new StringBuilder(str);
            sb.insert(str.indexOf("（"), "\n");
            return sb.toString();
        }
        return str;
    }

    //处理最基础的柱形数据
    private SubcolumnValue getSubColumn(GuestAgeStatisticsBeanWrapper.GuestAgeStatisticsBean bean, int totalCount,
                                        boolean isLast) {

        double ratio = StatisticsHelper.calculateRatio(Double.parseDouble(bean.getCountNum()), totalCount,
                totalRatio, isLast, 2, BigDecimal.ROUND_FLOOR);
        totalRatio+=ratio;

        SubcolumnValue subcolumnValue = new SubcolumnValue();

        subcolumnValue.setValue((float) ratio);
        subcolumnValue.setLabel(ratio + "%");
        subcolumnValue.setColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
        return subcolumnValue;
    }

    //处理封装好的柱形图数据集
    private void handlerColumnChartData(ColumnChartData columnChartData, int size) {
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

        columnChartData.setValueLabelBackgroundEnabled(false);
        columnChartData.setValueLabelsTextColor(App.getResource().getColor(R.color.colorPrimary));
    }
}
