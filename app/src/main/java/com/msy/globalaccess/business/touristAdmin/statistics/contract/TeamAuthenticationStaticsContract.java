package com.msy.globalaccess.business.touristAdmin.statistics.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;

/**
 * 团队认证统计契约类
 * Created by chensh on 2017/5/16 0016.
 */

public interface TeamAuthenticationStaticsContract {

    interface View extends BaseContract.View {
        /**
         * 获取统计数据
         */
        void getTravelTeamCheckStatistics(TravelTeamStatisticsBean object);

        /**
         * @return  查询类型 ：0：旅行社；1：景区
         */
        String getSearchType();
        /**
         * @return  旅行社ID
         */
        String getTravelAgentId();
        /**
         * @return  景点ID
         */
        String getScenicId();
        /**
         * @return  行程开始日期（yyyy-MM-dd HH:mm）
         */
        String getTeamCheckStartDate();
        /**
         * @return  行程结束日期（yyyy-MM-dd HH:mm）
         */
        String getTeamCheckEndDate();
        /**
         * @return  团队类型Id,团队类型查询接口中获取
         */
        String getTeamTypeId();
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 获取团队认证统计数据
         */
        void requestTTCS();
    }
}
