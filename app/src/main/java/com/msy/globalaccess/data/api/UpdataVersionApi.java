package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.UpdateVersionBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pepys on 2017/5/15.
 * description:
 */
public interface UpdataVersionApi {
    /**
     * APP版本更新
     */

    String method = "appVersionCheck";

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<UpdateVersionBean>> getNewVersion(@FieldMap HashMap<String, String> paramsMap);

}
