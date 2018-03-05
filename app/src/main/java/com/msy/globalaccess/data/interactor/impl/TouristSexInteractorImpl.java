package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsListBean;
import com.msy.globalaccess.data.interactor.ITouristSexStatisticsinteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by pepys on 2017/7/10
 * description: 游客性别统计
 *
 */
public class TouristSexInteractorImpl implements ITouristSexStatisticsinteractor<TouristSexStatisticsListBean> {
    /**
     * 饼图的二种颜色
     */
    private int[] sexPieColors = new int[]{
            R.color.guide_pie_blue_theme,
            R.color.guide_pie_orange_light,
    };

    private StatisticsApi.TouristSexStatisticsApi touristSexStatisticsApi;

    @Inject
    TouristSexInteractorImpl(Retrofit retrofit) {
        this.touristSexStatisticsApi = retrofit.create(StatisticsApi.TouristSexStatisticsApi.class);
    }

    @Override
    public Subscription loadTouristSex(RequestCallBack<TouristSexStatisticsListBean> callBack, HashMap<String,String> params) {
        callBack.beforeRequest();
        return touristSexStatisticsApi.getSexStatisticsData(params)
                .compose(RxJavaUtils.<BaseBean<TouristSexStatisticsListBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<TouristSexStatisticsListBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<TouristSexStatisticsListBean>, TouristSexStatisticsListBean>() {

                    @Override
                    public TouristSexStatisticsListBean call(BaseBean<TouristSexStatisticsListBean> touristSexStatisticsListBaseBean) {
                        PieChartData pieChartData = parSexStatistics(touristSexStatisticsListBaseBean.getData().getCustomSexCountList());
                        touristSexStatisticsListBaseBean.getData().setSexStatisticsPie(pieChartData);
                        return touristSexStatisticsListBaseBean.getData();
                    }

                } )
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    private PieChartData parSexStatistics(List<TouristSexStatisticsBean> data){
        if (data == null || data.size() <= 0) {
            return null;
        }
        PieChartData pieChartData = new PieChartData();
        pieChartData.setHasLabels(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setSlicesSpacing(0);
        List<SliceValue> sliceValues = new ArrayList<>();
        int totalNum = 0;
        for (int i = 0; i < data.size(); i++) {
            totalNum = totalNum + Integer.parseInt(data.get(i).getCountNum());
            SliceValue sliceValue = new SliceValue();
            float value = Float.parseFloat(data.get(i).getCountNum());
            if (value == 0) {
                value = 0.000001f;
            }
            sliceValue.setValue(value);
            sliceValue.setColor(App.getResource().getColor(sexPieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setCenterText1(totalNum+"人次");
        pieChartData.setCenterText1FontSize(12);
        pieChartData.setCenterText1Color(App.getResource().getColor(R.color.colorPrimary));
        return pieChartData.setValues(sliceValues);
    }
}
