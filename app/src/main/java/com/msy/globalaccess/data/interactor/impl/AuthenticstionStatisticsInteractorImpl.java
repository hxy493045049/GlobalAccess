package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 认证处理数据
 * Created by chensh on 2017/5/16 0016.
 */

@SuppressWarnings("deprecation")
public class AuthenticstionStatisticsInteractorImpl implements IStatisticsInteractor
        .IAuthenticstionStatisticsInteractor {
    private StatisticsApi.AuthenticationStatisticsApi api;

    @Inject
    AuthenticstionStatisticsInteractorImpl(Retrofit mRetrofit) {
        this.api = mRetrofit.create(StatisticsApi.AuthenticationStatisticsApi.class);
    }

    @Override
    public Subscription requestAuthenticsStatist(final @NotNull RequestCallBack<TravelTeamStatisticsBean> callBack,
                                                 @NotNull HashMap<String, String> map) {
        callBack.beforeRequest();
        return api.submitScenic(map)
                .compose(RxJavaUtils.<BaseBean<TravelTeamStatisticsBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<TravelTeamStatisticsBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<TravelTeamStatisticsBean>, BaseBean<TravelTeamStatisticsBean>>() {
                    @Override
                    public BaseBean<TravelTeamStatisticsBean> call(BaseBean<TravelTeamStatisticsBean>
                                                                           addressStatisticsBean) {
                        addressStatisticsBean.getData().setTeamColumnChartData(changeStatisticsData
                                (addressStatisticsBean.getData().getTeamCheckCountList(), 1));
                        addressStatisticsBean.getData().setPeopleColumnChartData(changeStatisticsData
                                (addressStatisticsBean.getData().getTeamCheckCountList(), 2));
                        return addressStatisticsBean;
                    }
                })
                .subscribe(new Subscriber<BaseBean<TravelTeamStatisticsBean>>() {
                    @Override
                    public void onCompleted() {
                        callBack.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.internet_error);
                        Logger.e(e, "AuthenticstionStatisticsInteractorImpl");
                        callBack.after();
                    }

                    @Override
                    public void onNext(BaseBean<TravelTeamStatisticsBean> travelTeamStatisticsBeanBaseBean) {
                        if (travelTeamStatisticsBeanBaseBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(travelTeamStatisticsBeanBaseBean.getData());
                        } else {
                            ToastUtils.showToast(travelTeamStatisticsBeanBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, travelTeamStatisticsBeanBaseBean.getMessage());
                        }
                    }
                });
    }

    /**
     * @param statisticsType 1：统计团队数量 2：统计人数
     */
    private ColumnChartData changeStatisticsData(ArrayList<TravelTeamStatisticsBean.TeamCheckCount> data, int
            statisticsType) {
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int size = data.size();
        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            subcolumnValue.setValue(Float.parseFloat(statisticsType == 1 ? data.get(i).getTeamCount() : data.get(i)
                    .getPeopleCount()));
            subcolumnValue.setColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
            subcolumnValues.add(subcolumnValue);
            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            columns.add(column);
            String label = data.get(i).getName();
            axisValues.add(new AxisValue(i).setLabel(label));
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
        columnChartData.setValueLabelsTextColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
        Axis axisY = new Axis().setHasLines(true)
                .setHasSeparationLine(true)
                .setMaxLabelChars(6)
                .setTextSize(11)
                .setTextColor(App.getResource().getColor(R.color.text_primary_light));
        columnChartData.setAxisYLeft(axisY);
        Axis axisX = new Axis(axisValues)
                .setHasLines(false)
                .setName("  ")
                .setHasSeparationLine(true)
                .setTextSize(11)
                .setTextColor(App.getResource().getColor(R.color.text_primary_light));
        columnChartData.setAxisXBottom(axisX);
        return columnChartData;
    }


}
