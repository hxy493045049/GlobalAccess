package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.DataPreviewPresenterImpl;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;

/**
 * Created by shawn on 2017/5/22 0022.
 * <p>
 * description : 数据概览
 */

public interface TourismDataPreviewContract {
    interface View extends BaseContract.View, ScenicAuthTimeContract.View, PeopleAndDayTendencyContract.View,
            GuestAgeContract.View {
        void showContent();

        //团队快报
        void handleSummary(DataPreviewPresenterImpl.ResultBean resultBean);

        //今日景区
        void handleAccrossToScenery(DataPreviewPresenterImpl.ResultBean resultBean);

        //景区客流趋势
        void handleSceneryPreview(DataPreviewPresenterImpl.ResultBean resultBean);

        //客源地总览
        void handleGuestsRegion(DataPreviewPresenterImpl.ResultBean resultBean);

        //境内外游客对比
        void handleAbroadStatistics(DataPreviewPresenterImpl.ResultBean resultBean);

        //趋势分析
        void handleTendencyAnalysis(DataPreviewPresenterImpl.ResultBean resultBean);

        //性别分析
        void handleSexStatistics(DataPreviewPresenterImpl.ResultBean resultBean);

        //团队停留天数
        void handleStayDayStatistics(DataPreviewPresenterImpl.ResultBean resultBean);

        //旅游人.天分析
        void handlePeopleAndDayStatisticsData(PeopleAndDayStatisticsBeanWrapper.ResultBean resultBean);

        void onError(@ResultCode int resultCode, String errorMsg);

        String[] getTimes();
    }

    interface Presenter extends BaseContract.Presenter, PeopleAndDayTendencyContract.Presenter {
        void loadDataSummary();


    }
}
