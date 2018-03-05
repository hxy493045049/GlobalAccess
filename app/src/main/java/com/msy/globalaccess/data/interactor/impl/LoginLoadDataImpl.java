package com.msy.globalaccess.data.interactor.impl;

import com.google.gson.Gson;
import com.msy.globalaccess.common.LoginType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.LoginApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.RoleBean;
import com.msy.globalaccess.data.bean.user.User;
import com.msy.globalaccess.data.bean.user.UserInfo;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.data.interactor.LoginInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.msy.zc.commonutils.StringToMD5;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 登录实际请求类
 * Created by chensh on 2017/2/8 0008.
 */

public class LoginLoadDataImpl implements LoginInteractor {
    private static String TAG = LoginLoadDataImpl.class.getSimpleName();
    private Retrofit mRetrofit;

    @Inject
    LoginLoadDataImpl(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    /**
     * 登录时设置权限
     *
     * @param roleList
     * @return
     */
    private List<RoleBean> changeRole(List<RoleBean> roleList) {
        List<RoleBean> newRoleList = new ArrayList<>();
        for (RoleBean roleBean : roleList) {
            for (String role : RoleBean.RoleList) {
                if (roleBean.getRoleTag().equals(role) && roleBean.getIsOwn().equals("1")) {
                    newRoleList.add(roleBean);
                    break;
                }
            }
        }
        return newRoleList;
    }

    @Override
    public Subscription loadUsrData(final RequestCallBack callBack, final String loginName, String password) {
        final String finalPassword = StringToMD5.MD5(password);
        LoginApi service = mRetrofit.create(LoginApi.class);
        //获取Map集合
        HashMap<String, String> param = new ParamsHelper.Builder()
                .setMethod(LoginApi.APPLOGIN)
                .setParam("loginName", loginName)
                .setParam("password", finalPassword)
                .build().getParam();
        return service.getUser(param)
                .compose(RxJavaUtils.<BaseBean<UserInfo>>defaultSchedulers())
                .observeOn(Schedulers.io())//请求在新的线程中执行
                .doOnNext(new Action1<BaseBean<UserInfo>>() {
                    @Override
                    public void call(BaseBean<UserInfo> userInfo) {
                        //保存到数据库
                        if (userInfo != null && ResultCode.SUCCESS == userInfo.getStatus()) {
                            UserInfo bean = userInfo.getData();
                            String roleListstr = new Gson().toJson(changeRole(bean.getRoleList()));
                            UserHelper.getInstance().setUserListLogout();
                            UserHelper.getInstance().insertUser(new User(null, bean.getUserId(), loginName, finalPassword,
                                    LoginType.STATUS_LOGING, bean.getUserName(), bean.getUserTel(), bean
                                    .getUserMobile(), bean
                                    .getUserEmail(), bean.getUserAddr(), bean.getUserRegDate(), bean.getUserRoleType
                                    (), bean.getUserSystem(), bean.getUserUnitName(), bean.getUserDepName(), roleListstr, bean.getUnitId(), bean.getCityId(), bean.getCityName(), bean.getProvince()));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean<UserInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //请求失败
                        callBack.onError(ResultCode.NET_ERROR, "网络异常,请检查网络设置");
                        Logger.e(e, "登录出现异常");
                    }

                    @Override
                    public void onNext(BaseBean<UserInfo> userInfo) {
                        //请求成功
                        if (userInfo != null) {
                            if (ResultCode.SUCCESS == userInfo.getStatus()) {
                                callBack.success(userInfo.getData());
//                                if (userInfo.getData().getUserRoleType().equals("0")) {
//
//                                } else {
//                                    String msg = "请使用旅行社帐号登录";
//                                    callBack.onError(ResultCode.NET_ERROR, msg);
//                                    ToastUtils.showToast(msg);
//                                }
                            } else {
                                callBack.onError(ResultCode.NET_ERROR, userInfo.getMessage());
                            }
                        }
                    }
                });
    }
}
