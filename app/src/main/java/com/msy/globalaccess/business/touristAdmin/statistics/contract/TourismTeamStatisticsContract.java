package com.msy.globalaccess.business.touristAdmin.statistics.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.TeamStatisticsImpl;

/**
 * Created by WuDebin on 2017/5/16.
 */

public interface TourismTeamStatisticsContract {
    interface View extends BaseContract.View {
        /**
         */
        void showTeamInfoStatistics(TeamStatisticsImpl.ResultBean teamInfoStatisticsBean);

        /**
         * @return 旅行社ID
         */
        String getTravelAgentId();
        /**
         * @return 行程开始日期（yyyy-MM-dd）
         */
        String getTeamDate();
        /**
         * @return 团队创建开始日期（yyyy-MM-dd）
         */
        String getTeamCreateStartDate();
        /**
         * @return 团队创建结束日期（yyyy-MM-dd）
         */
        String getTeamCreateEndDate();
        /**
         * @return 团队类型Id,团队类型查询接口中获取
         */
        String getTeamType();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadTeamInfoData();
    }
}
