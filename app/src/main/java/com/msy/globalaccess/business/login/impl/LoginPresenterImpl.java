package com.msy.globalaccess.business.login.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.login.LoginContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.user.UserInfo;
import com.msy.globalaccess.data.interactor.impl.LoginLoadDataImpl;
import com.msy.globalaccess.utils.ToastUtils;

import javax.inject.Inject;

import rx.Subscription;

/**
 * 登录实现类
 * Created by chensh on 2017/1/24 0024.
 */

public class LoginPresenterImpl extends BasePresenterImpl<LoginContract.View>
        implements LoginContract.Presenter {
    private LoginLoadDataImpl impl;

    @Inject
    public LoginPresenterImpl(LoginLoadDataImpl impl) {
        this.impl = impl;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void loadserUsrDetail(String name, String pwd) {
        mView.showProgress();
        Subscription subscription = impl.loadUsrData(new RequestCallBackAdapter<UserInfo>
                () {
            @Override
            public void success(UserInfo data) {
                mView.hideProgress();
                mView.showUserDetail();
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.hideProgress();
                ToastUtils.showToast(errorMsg);
            }
        }, name, pwd);
        cacheSubscription.add(subscription);
    }
}
