package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsListBean;

/**
 * Created by shawn on 2017/5/22 0022.
 * <p>
 * description : 性别统计
 */

public interface TourismSexStatisticsContract {
    interface View extends BaseContract.View{
        /**
         * 时间范围
         * @return
         */
        String[] getTimes();

        /**
         * 加载性别数据成功
         */
        void loadSexStatisticsSuccess(TouristSexStatisticsListBean data);

        void loadFailer();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadSexStatistics();
    }
}
