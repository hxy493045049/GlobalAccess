package com.msy.globalaccess.business.travelAgency.setting;

import com.msy.globalaccess.base.BaseContract;

/**
 * Created by WuDebin on 2017/2/10.
 */

public interface PersonalCenterContract {
    interface View extends BaseContract.View {
        /**
         * 注销登录
         */
        void logout();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 请求注销用户登录信息
         */
        void logout();
    }
}
