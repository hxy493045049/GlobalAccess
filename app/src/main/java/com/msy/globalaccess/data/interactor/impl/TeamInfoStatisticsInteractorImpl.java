package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.TeamStatisticsImpl;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.team.TeamInfoStatisticsBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentStatisticsBean;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

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
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by WuDebin on 2017/5/17.
 */
public class TeamInfoStatisticsInteractorImpl implements IStatisticsInteractor.ITeamInfoStatisticsInteractor {

    private StatisticsApi.TeamInfoStatisticsApi api;

    @Inject
    TeamInfoStatisticsInteractorImpl(Retrofit retrofit) {
        api = retrofit.create(StatisticsApi.TeamInfoStatisticsApi.class);
    }

    @Override
    public Subscription loadTeamInfoStatisticsData(@NotNull final RequestCallBack<TeamStatisticsImpl.ResultBean>
                                                           callBack, @NotNull HashMap<String, String> map) {
        callBack.beforeRequest();
        return api.loadTeamInfo(map)
                .compose(RxJavaUtils.<BaseBean<TeamInfoStatisticsBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<TeamInfoStatisticsBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<TeamInfoStatisticsBean>, TeamStatisticsImpl.ResultBean>() {
                    @Override
                    public TeamStatisticsImpl.ResultBean call(BaseBean<TeamInfoStatisticsBean>
                                                                      teamInfoStatisticsBeanBaseBean) {
                        TeamInfoStatisticsBean origin = teamInfoStatisticsBeanBaseBean.getData();
                        TeamStatisticsImpl.ResultBean resultBean = new TeamStatisticsImpl.ResultBean();
                        resultBean.setOrigin(origin);
                        resultBean.setAdultCount(origin.getAdultCount());
                        resultBean.setChildCount(origin.getChildCount());
                        resultBean.setPeopleCount(origin.getPeopleCount());
                        resultBean.setTeamCount(origin.getTeamCount());
                        resultBean.setTravelTeamCount(parseTravelTeamCount(origin.getTravelAgentStatisticsBeen()));
                        resultBean.setTravelTeamPeopleCount(parseTravelTeamPeopleCount(origin
                                .getTravelAgentStatisticsBeen()));
                        return resultBean;
                    }
                }).subscribe(new DefaultSubscriber<>(callBack));
    }

    //getTravelAgentStatisticsBeen

    /**
     * 显示旅行社接待团队统计
     */
    private ColumnChartData parseTravelTeamCount(List<TravelAgentStatisticsBean> data) {
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        int size = data.size() > 100 ? 100 : data.size();
        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            subcolumnValue.setValue(Float.parseFloat(data.get(i).getTeamCount()));
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
        columnChartData.setValueLabelsTextColor(App.getResource().getColor(R.color.colorPrimary));
        Axis axisY = new Axis().setHasLines(true)
                .setHasSeparationLine(true)
                .setMaxLabelChars(5)
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

    /**
     * 显示旅行社接待团队人数统计
     */
    private ColumnChartData parseTravelTeamPeopleCount(List<TravelAgentStatisticsBean> data) {
        ColumnChartData columnChartData = new ColumnChartData();
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        int size = data.size() > 100 ? 100 : data.size();
        if (size == 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            Column column = new Column();
            List<SubcolumnValue> subcolumnValues = new ArrayList<>();
            SubcolumnValue subcolumnValue = new SubcolumnValue();
            subcolumnValue.setValue(Float.parseFloat(data.get(i).getPeopleCount()));
            subcolumnValue.setColor(App.getResource().getColor(R.color.guide_pie_blue_theme));
            subcolumnValues.add(subcolumnValue);
            column.setValues(subcolumnValues);
            column.setHasLabels(true);
            columns.add(column);
            String label = data.get(i).getName();
            //            if (size >= 4 && label.length() > 4) {
            //                label = label.substring(0, 4) + "..";
            //            } else if (size == 3 && label.length() > 7) {
            //                label = label.substring(0, 7) + "..";
            //            } else if (size == 2 && label.length() > 12) {
            //                label = label.substring(0, 12) + "..";
            //            } else if (size == 1 && label.length() > 22) {
            //                label = label.substring(0, 20) + "..";
            //            }
            axisValues.add(new AxisValue(i).setLabel(label));
        }
        //        List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
        //        int maxNum=getMaxNum(data,true);
        //        int avg=maxNum/4;
        //        for(int i=0;i<=6;i++){
        //            axisValuesY.add(new AxisValue(avg*i));
        //        }
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
                .setMaxLabelChars(6)
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
