package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import org.greenrobot.greendao.annotation.NotNull;

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
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 客源地统计交互器
 */

public class GuestSourceInteractorImpl implements IStatisticsInteractor.IGuestSourceStaticticsInteractor {
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

    private StatisticsApi.GuestSourceStatisticsApi api;

    @Inject
    public GuestSourceInteractorImpl(Retrofit retrofit) {
        api = retrofit.create(StatisticsApi.GuestSourceStatisticsApi.class);
    }

    @Override
    public Subscription getStaticticsData(@NotNull RequestCallBack<GuestSourceBeanWrapper.ResultBean> callBack,
                                          @NotNull HashMap<String, String> param) {
        callBack.beforeRequest();
        return api.getGuestSourceData(param)
                .compose(RxJavaUtils.<BaseBean<GuestSourceBeanWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<GuestSourceBeanWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<GuestSourceBeanWrapper>, GuestSourceBeanWrapper.ResultBean>() {
                    @Override
                    public GuestSourceBeanWrapper.ResultBean call(BaseBean<GuestSourceBeanWrapper> listBaseBean) {
                        List<GuestSourceBeanWrapper.GuestSourceBean> data = listBaseBean.getData()
                                .getCustomSourceCountList();
                        return parseGuestsOrigin(data);
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    //解析客源地
    private GuestSourceBeanWrapper.ResultBean parseGuestsOrigin(List<GuestSourceBeanWrapper.GuestSourceBean> data) {
        GuestSourceBeanWrapper.ResultBean bean = new GuestSourceBeanWrapper.ResultBean();
        bean.setOrigin(data);
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
            sliceValue.setColor(App.getResource().getColor(pieColors[i]));
            sliceValues.add(sliceValue);
        }
        pieChartData.setCenterText1(totalNum + "人次");
        pieChartData.setCenterText1FontSize(12);
        pieChartData.setCenterText1Color(App.getResource().getColor(R.color.colorPrimary));
        pieChartData.setValues(sliceValues);
        bean.setPieChartData(pieChartData);
        return bean;
    }

}
