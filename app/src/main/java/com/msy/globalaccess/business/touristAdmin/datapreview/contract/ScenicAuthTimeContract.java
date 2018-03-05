package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTimeCountListWrapper;

import java.util.List;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by shawn on 2017/7/10 0010.
 * <p>
 * description : 景点日期认证统计
 */

public interface ScenicAuthTimeContract {
    interface View extends BaseContract.View {
        void showScenicAuthTimeData(List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList> data, ColumnChartData
                columnChartData);

        String[] getTimes();

        String getScenicId();

        void onScenicAuthTimeError(@ResultCode int resultCode, String errorMsg);
    }

    interface Presenter extends BaseContract.Presenter {
        void getScnicAuthTimeStatisticsData();
    }
}
