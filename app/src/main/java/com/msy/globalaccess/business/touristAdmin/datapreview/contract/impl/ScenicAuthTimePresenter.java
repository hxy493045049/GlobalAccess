package com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.ScenicAuthTimeContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTimeCountListWrapper;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.ScenicAuthTimeInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.ColumnChartData;
import rx.Subscription;

/**
 * Created by shawn on 2017/7/10 0010.
 * <p>
 * description :
 */

public class ScenicAuthTimePresenter extends BasePresenterImpl<ScenicAuthTimeContract.View> implements
        ScenicAuthTimeContract.Presenter {
    private IStatisticsInteractor.IScenicAuthTimeInteractor mInteractor;

    @Inject
    public ScenicAuthTimePresenter(ScenicAuthTimeInteractorImpl interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart() {
        getScnicAuthTimeStatisticsData();
    }

    @Override
    public void getScnicAuthTimeStatisticsData() {
        Subscription subscription = mInteractor.getStaticsData(new RequestCallBackAdapter<ResultBean>() {
            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onScenicAuthTimeError(resultCode, errorMsg);
            }

            @Override
            public void success(ResultBean data) {
                super.success(data);
                mView.showScenicAuthTimeData(data.getOriginData(), data.getColumnChartData());
            }
        }, buildParam());
        cacheSubscription.add(subscription);
    }

    private HashMap<String, String> buildParam() {
        HashMap<String, String> param = new ParamsHelper.Builder().setMethod(StatisticsApi.ScenicAuthTimeApi.method)
                .setParam(StatisticsApi.ScenicAuthTimeApi.userId, App.userHelper.getUser().getUserId())
                .setParam(StatisticsApi.ScenicAuthTimeApi.searchDateStart, mView.getTimes()[0])
                .setParam(StatisticsApi.ScenicAuthTimeApi.searchDateEnd, mView.getTimes()[1])
                .setParam(StatisticsApi.ScenicAuthTimeApi.scenicId, mView.getScenicId())
                .build().getParam();
        return param;
    }

    public static class ResultBean {
        private List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList> originData;
        private ColumnChartData columnChartData;

        public List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList> getOriginData() {
            return originData;
        }

        public void setOriginData(List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList> originData) {
            this.originData = originData;
        }

        public ColumnChartData getColumnChartData() {
            return columnChartData;
        }

        public void setColumnChartData(ColumnChartData columnChartData) {
            this.columnChartData = columnChartData;
        }
    }

}
