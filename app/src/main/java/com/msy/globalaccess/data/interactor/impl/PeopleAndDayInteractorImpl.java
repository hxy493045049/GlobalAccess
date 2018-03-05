package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import org.greenrobot.greendao.annotation.NotNull;

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
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description :
 */

public class PeopleAndDayInteractorImpl implements IStatisticsInteractor.IPeopleAndDayInteractor {
    private StatisticsApi.PeopleAndDayStatisticsApi api;

    @Inject
    public PeopleAndDayInteractorImpl(Retrofit retrofit) {
        api = retrofit.create(StatisticsApi.PeopleAndDayStatisticsApi.class);
    }

    @Override
    public Subscription getStatisticsData(@NotNull RequestCallBack<PeopleAndDayStatisticsBeanWrapper
            .ResultBean> callBack, @NotNull HashMap<String, String> param) {

        callBack.beforeRequest();
        return api.getStatusData(param)
                .compose(RxJavaUtils.<BaseBean<PeopleAndDayStatisticsBeanWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<PeopleAndDayStatisticsBeanWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<PeopleAndDayStatisticsBeanWrapper>, PeopleAndDayStatisticsBeanWrapper
                        .ResultBean>() {
                    @Override
                    public PeopleAndDayStatisticsBeanWrapper.ResultBean call
                            (BaseBean<PeopleAndDayStatisticsBeanWrapper> base) {
                        PeopleAndDayStatisticsBeanWrapper.ResultBean resultBean = new
                                PeopleAndDayStatisticsBeanWrapper.ResultBean();
                        List<PeopleAndDayStatisticsBeanWrapper.PeopleAndDayStatisticsBean> origin =
                                base.getData().getCustomDaysCountList();
                        resultBean.setOriginData(origin);
                        resultBean.setColumnChartData(parseData(origin));
                        return resultBean;
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    private ColumnChartData parseData(List<PeopleAndDayStatisticsBeanWrapper.PeopleAndDayStatisticsBean>
                                              data) {
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int size = data.size() > 100 ? 100 : data.size();
        if (size == 0) {
            return null;
        }
        int maxCount = 0;
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            float count = Float.parseFloat(data.get(i).getCountNum());
            subcolumnValue.setValue(count);
            subcolumnValue.setColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
            subcolumnValues.add(subcolumnValue);
            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            column.setFormatter(new SimpleColumnChartValueFormatter(2));
            columns.add(column);
            String label = data.get(i).getName();
            axisValues.add(new AxisValue(i).setLabel(label));
            if (count > maxCount) {
                maxCount = (int) Math.ceil(count * 1.0);
            }
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
        columnChartData.setColumns(columns);
        columnChartData.setValueLabelBackgroundEnabled(false);
        columnChartData.setValueLabelsTextColor(App.getResource().getColor(R.color.colorPrimary));
        Axis axisY = new Axis().setHasLines(true)
                .setHasSeparationLine(true)
                .setMaxLabelChars((maxCount*1.25 + "").length())
                .setTextSize(11)
                .setTextColor(App.getResource().getColor(R.color.text_primary_light));
        columnChartData.setAxisYLeft(axisY);
        Axis axisX = new Axis(axisValues)
                .setName("  ")
                .setHasLines(false)
                .setHasSeparationLine(true)
                .setTextSize(11).setTextColor(App.getResource().getColor(R.color.text_primary_light));
        columnChartData.setAxisXBottom(axisX);
        return columnChartData;
    }
}
