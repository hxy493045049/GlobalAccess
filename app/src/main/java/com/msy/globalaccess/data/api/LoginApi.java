package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.user.UserInfo;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 登录接口
 * Created by chensh on 2017/2/8 0008.
 */

public interface LoginApi {
    String APPLOGIN = "appLogin";

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<UserInfo>> getUser(
            @FieldMap HashMap<String, String> paramsMap
    );
}
