package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;

/**
 * Created by shawn on 2017/7/17 0017.
 * <p>
 * description :
 */

public interface GuestAgeContract {
    interface View extends BaseContract.View {
        String[] getTimes();

        void onGuestAgeError(@ResultCode int resultCode, String errorMsg);

        void handlerGuestAgeData(GuestAgeStatisticsBeanWrapper.ResultBean resultBean);

    }

    interface Presenter extends BaseContract.Presenter {
        void getGuestAgeStatisticsData();
    }
}
