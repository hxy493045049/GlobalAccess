package com.msy.globalaccess.business.touristAdmin.datapreview.contract;

import com.msy.globalaccess.base.BaseContract;

/**
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description :
 */

public interface PeopleAndDayTendencyContract {
    interface View extends BaseContract.View {
        void onPeopleAndDayError(int resultCode, String errorMsg);
    }

    interface Presenter extends BaseContract.Presenter {
        void getPeopleAndDayData();
    }
}
