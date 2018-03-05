package com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.GuestAgeContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.ScenicAuthTimeContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.TourismDataPreviewContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.TourismSummaryBean;
import com.msy.globalaccess.data.interactor.IStatisticsInteractor;
import com.msy.globalaccess.data.interactor.impl.PeopleAndDayInteractorImpl;
import com.msy.globalaccess.data.interactor.impl.TourismDataInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;

import java.util.HashMap;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import rx.Subscription;

/**
 * Created by shawn on 2017/5/23 0023.
 * <p>
 * description :
 */

public class DataPreviewPresenterImpl extends BasePresenterImpl<TourismDataPreviewContract.View> implements
        TourismDataPreviewContract.Presenter {
    private TourismDataInteractorImpl mInteractor;
    private IStatisticsInteractor.IPeopleAndDayInteractor peopleAndDayInteractor;
    private ScenicAuthTimeContract.Presenter mSATPresenter;
    private GuestAgeContract.Presenter mGuestAgePresenter;

    @Inject
    DataPreviewPresenterImpl(TourismDataInteractorImpl interactor,
                                    PeopleAndDayInteractorImpl peopleAndDayInteractor,
                                    ScenicAuthTimePresenter SATPresenter,
                                    GuestAgePresenterImpl guestAgePresenter) {
        this.mInteractor = interactor;
        this.peopleAndDayInteractor = peopleAndDayInteractor;

        //已经存在现有的activity和presenter,直接重用即可
        mSATPresenter = SATPresenter;
        mGuestAgePresenter = guestAgePresenter;
    }

    @Override
    public void onStart() {
        loadDataSummary();//加载首页总数据

        getPeopleAndDayData();//加载旅游人天统计

        mSATPresenter.attachView(mView);//加载景点认证时间统计
        mSATPresenter.getScnicAuthTimeStatisticsData();

        mGuestAgePresenter.attachView(mView);//加载游客年龄统计
        mGuestAgePresenter.getGuestAgeStatisticsData();
    }

    @Override //加载首页总数据
    public void loadDataSummary() {
        Subscription subscription = mInteractor.getSummary(new RequestCallBackAdapter<ResultBean>() {
            @Override
            public void success(ResultBean data) {
                mView.showContent();
                mView.handleSummary(data);
                mView.handleAccrossToScenery(data);
                mView.handleSceneryPreview(data);
                mView.handleGuestsRegion(data);
                mView.handleTendencyAnalysis(data);
                mView.handleSexStatistics(data);
                mView.handleAbroadStatistics(data);
                mView.handleStayDayStatistics(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onError(resultCode, errorMsg);
            }
        }, buildDataSummaryParam());
        cacheSubscription.add(subscription);
    }

    @Override //加载旅游人天统计
    public void getPeopleAndDayData() {
        Subscription subscription = peopleAndDayInteractor.getStatisticsData(new RequestCallBackAdapter
                <PeopleAndDayStatisticsBeanWrapper.ResultBean>() {
            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.onPeopleAndDayError(resultCode, errorMsg);
            }

            @Override
            public void success(PeopleAndDayStatisticsBeanWrapper.ResultBean data) {
                super.success(data);
                mView.handlePeopleAndDayStatisticsData(data);
            }
        }, buildPeopleAndDayParam());
        cacheSubscription.add(subscription);
    }

    private HashMap<String, String> buildPeopleAndDayParam() {
        return new ParamsHelper.Builder()
                .setMethod(StatisticsApi.PeopleAndDayStatisticsApi.method)
                .setParam(StatisticsApi.PeopleAndDayStatisticsApi.userId, App.userHelper.getUser().getUserId())
                .setParam(StatisticsApi.PeopleAndDayStatisticsApi.searchDate, mView.getTimes()[0])
                .build().getParam();
    }

    private HashMap<String, String> buildDataSummaryParam() {
        return new ParamsHelper.Builder()
                .setMethod(StatisticsApi.TourismDataSummaryApi.method)
                .setParam(StatisticsApi.TourismDataSummaryApi.userId, App.userHelper.getUser().getUserId())
                .setParam(StatisticsApi.TourismDataSummaryApi.searchDate, mView.getTimes()[0])
                .build().getParam();
    }

    public static class ResultBean {
        private LineChartData lineChartData;
        private String domesticTeamCount;
        private String createTeamCount;
        private String domesticCustomCount;
        private TourismSummaryBean origenBean;
        private PieChartData guestsPieData;
        private PieChartData sceneryPieData;
        private ColumnChartData intentionData;
        private PieChartData sexData;
        private LineChartData abroadLineChartData;
        private LineChartData stayDayLineChartData;

        public ColumnChartData getTendencyData() {
            return intentionData;
        }

        public void setIntentionData(ColumnChartData intentionData) {
            this.intentionData = intentionData;
        }

        public PieChartData getSceneryPieData() {
            return sceneryPieData;
        }

        public void setSceneryPieData(PieChartData sceneryPieData) {
            this.sceneryPieData = sceneryPieData;
        }

        public PieChartData getGuestsPieData() {
            return guestsPieData;
        }

        public void setGuestsPieData(PieChartData guestsOriginData) {
            this.guestsPieData = guestsOriginData;
        }

        public TourismSummaryBean getOrigenBean() {
            return origenBean;
        }

        public void setOrigenBean(TourismSummaryBean origenBean) {
            this.origenBean = origenBean;
        }

        public String getDomesticTeamCount() {
            return domesticTeamCount;
        }

        public void setDomesticTeamCount(String domesticTeamCount) {
            this.domesticTeamCount = domesticTeamCount;
        }

        public String getCreateTeamCount() {
            return createTeamCount;
        }

        public void setCreateTeamCount(String createTeamCount) {
            this.createTeamCount = createTeamCount;
        }

        public String getDomesticCustomCount() {
            return domesticCustomCount;
        }

        public void setDomesticCustomCount(String domesticCustomCount) {
            this.domesticCustomCount = domesticCustomCount;
        }

        public LineChartData getLineChartData() {
            return lineChartData;
        }

        public void setLineChartData(LineChartData lineChartData) {
            this.lineChartData = lineChartData;
        }

        public PieChartData getSexData() {
            return sexData;
        }

        public void setSexData(PieChartData sexData) {
            this.sexData = sexData;
        }

        public LineChartData getAbroadLineChartData() {
            return abroadLineChartData;
        }

        public void setAbroadLineChartData(LineChartData abroadLineChartData) {
            this.abroadLineChartData = abroadLineChartData;
        }

        public LineChartData getStayDayLineChartData() {
            return stayDayLineChartData;
        }

        public void setStayDayLineChartData(LineChartData stayDayLineChartData) {
            this.stayDayLineChartData = stayDayLineChartData;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGuestAgePresenter.onDestroy();
        mSATPresenter.onDestroy();
    }
}
