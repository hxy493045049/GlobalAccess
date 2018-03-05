package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.base.MapBeanWrapper;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shawn on 2017/3/28 0028.
 * <p>
 * description :
 */

public interface KeyMapApi {
    //必填参数
    String method = "appQueryPubMap";
    String mapType = "mapType";//字典类别

    //说明
    String languageType = "LANGUAGE";    // 导游语种
    String levelType = "SCENLEVEL";    //景点级别

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<MapBeanWrapper<KeyMapBean>>> getMapByKey(@Header("Cache-Control") String cacheControl, @FieldMap HashMap<String, String> paramsMap);
}
