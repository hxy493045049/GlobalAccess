package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 客源地契约
 */

public interface GuestSourceStaticticsContract {
    interface View extends BaseContract.View {
        String getSourceType();

        String getStartTime();

        String getEndTime();

        void handlerGuestSourceData(GuestSourceBeanWrapper.ResultBean resultBean);

        void onGuestSourceError(String errorMsg, int errorCode);

    }

    interface Presenter extends BaseContract.Presenter {
        void loadData();
    }
}
