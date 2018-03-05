package com.msy.globalaccess.business.login;

import com.msy.globalaccess.base.BaseContract;

/**
 * Created by chensh on 2017/1/24 0024.
 */

public interface LoginContract {
    interface View extends BaseContract.View {
        /**
         * 得到用户信息
         * <p>
         * param user
         */
        void showUserDetail();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 请求用户的登录信息
         */
        void loadserUsrDetail(String name, String pwd);
    }
}
