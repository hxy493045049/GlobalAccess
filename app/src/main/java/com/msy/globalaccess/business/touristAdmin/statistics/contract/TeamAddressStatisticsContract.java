package com.msy.globalaccess.business.touristAdmin.statistics.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.statistics.AddressStatisticsBean;

/**
 * 旅游局团队地区统计契约类
 * Created by chensh on 2017/5/17 0017.
 */

public interface TeamAddressStatisticsContract {

    interface View extends BaseContract.View {
        /**
         * 获取统计数据
         */
        void getTravelCustomSourceStatistics(AddressStatisticsBean object);
        /**
         * @return 旅行社ID
         */
        String getTravelAgentId();
        /**
         * @return 行程开始日期（yyyy-MM-dd）
         */
        String getTeamDate();
        /**
         * @return 团队状态：空值：查询全部；0：编辑中；1：已提交；2：生效；3：作废；
         */
        String getTeamStauts();
        /**
         * @return  团队类型Id,团队类型查询接口中获取，空值：查询全部
         */
        String getTeamTypeID();
        /**
         * @return 客源地：空值：查询全部;0:国内；1：入境
         */
        String getCustomSourceType();
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 请求团队地区统计数据
         */
        void requestTCSS( );
    }
}
