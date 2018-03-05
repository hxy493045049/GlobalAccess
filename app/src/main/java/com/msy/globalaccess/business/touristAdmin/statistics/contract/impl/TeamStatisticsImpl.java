package com.msy.globalaccess.business.touristAdmin.statistics.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.TourismTeamStatisticsContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.api.StatisticsApi;
import com.msy.globalaccess.data.bean.team.TeamInfoStatisticsBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.impl.TeamInfoStatisticsInteractorImpl;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.HashMap;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.ColumnChartData;
import rx.Subscription;

/**
 * Created by WuDebin on 2017/5/17.
 */

public class TeamStatisticsImpl extends BasePresenterImpl<TourismTeamStatisticsContract.View> implements
        TourismTeamStatisticsContract.Presenter {

    private TeamInfoStatisticsInteractorImpl interactor;

    @Inject
    TeamStatisticsImpl(TeamInfoStatisticsInteractorImpl impl) {
        this.interactor = impl;
    }

    @Override
    public void loadTeamInfoData() {
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(StatisticsApi.TeamInfoStatisticsApi.method)
                .setParam(StatisticsApi.TeamInfoStatisticsApi.userId, UserHelper.getInstance().getUser().getUserId())
                .setParam(StatisticsApi.TeamInfoStatisticsApi.travelAgentId, mView.getTravelAgentId())
                .setParam(StatisticsApi.TeamInfoStatisticsApi.teamDate, mView.getTeamDate())
                .setParam(StatisticsApi.TeamInfoStatisticsApi.teamCreateStartDate, mView.getTeamCreateStartDate())
                .setParam(StatisticsApi.TeamInfoStatisticsApi.teamCreateEndDate, mView.getTeamCreateEndDate())
                .setParam(StatisticsApi.TeamInfoStatisticsApi.teamTypeId, mView.getTeamType())
                .build().getParam();
        Subscription subscription = interactor.loadTeamInfoStatisticsData(new RequestCallBackAdapter<ResultBean>
                () {
            @Override
            public void beforeRequest() {
                if (mView != null) {
                    mView.showProgress();
                }
            }

            @Override
            public void success(ResultBean data) {
                mView.showTeamInfoStatistics(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                ToastUtils.showToast(errorMsg);
            }

            @Override
            public void after() {
                if (mView != null) {
                    mView.hideProgress();
                }
            }
        }, params);
        cacheSubscription.add(subscription);
    }

    @Override
    public void onStart() {

    }

    public static final class ResultBean {
        private TeamInfoStatisticsBean origin;
        private String teamCount;//总团数
        private String peopleCount;//总人数
        private String adultCount;//成人总人数
        private String childCount;//儿童总人数
        private ColumnChartData travelTeamCount;//旅行社接待团队统计
        private ColumnChartData travelTeamPeopleCount;//旅行社接待团队人数

        public TeamInfoStatisticsBean getOrigin() {
            return origin;
        }

        public void setOrigin(TeamInfoStatisticsBean origin) {
            this.origin = origin;
        }

        public ColumnChartData getTravelTeamCount() {
            return travelTeamCount;
        }

        public void setTravelTeamCount(ColumnChartData travelTeamCount) {
            this.travelTeamCount = travelTeamCount;
        }

        public ColumnChartData getTravelTeamPeopleCount() {
            return travelTeamPeopleCount;
        }

        public void setTravelTeamPeopleCount(ColumnChartData travelTeamPeopleCount) {
            this.travelTeamPeopleCount = travelTeamPeopleCount;
        }

        public String getTeamCount() {
            return teamCount;
        }

        public void setTeamCount(String teamCount) {
            this.teamCount = teamCount;
        }

        public String getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(String peopleCount) {
            this.peopleCount = peopleCount;
        }

        public String getAdultCount() {
            return adultCount;
        }

        public void setAdultCount(String adultCount) {
            this.adultCount = adultCount;
        }

        public String getChildCount() {
            return childCount;
        }

        public void setChildCount(String childCount) {
            this.childCount = childCount;
        }
    }


}
