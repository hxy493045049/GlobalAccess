package com.msy.globalaccess.business.navigation.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean_Old;

/**
 * Created by WuDebin on 2017/2/7.
 */

public class NavigationContract {

    public interface View extends BaseContract.View {

        String getStartDate();
        String getEndDate();

        void LoadNavigationSuccess(NavigationDataBean navigationData);
        void LoadNavigationFail();

        @Deprecated
        void getGuideData(NavigationDataBean_Old navigationDataBeanOld);
    }

    public interface Presenter extends BaseContract.Presenter {
        /**
         *  旧版本获取首页数据
         * @param startDate  开始日期
         * @param endDate   结束日期
         */
        @Deprecated
        void loadGuideData(String startDate,String endDate);

        void loadNavigationData();
    }

}
