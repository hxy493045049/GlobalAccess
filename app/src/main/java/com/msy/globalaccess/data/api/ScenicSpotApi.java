package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 景点相关接口
 * Created by chensh on 2017/3/17 0017.
 */
public interface ScenicSpotApi {

    String appScenicTicketTypeSearch = "appScenicTicketTypeSearch";
    String tripDate = "tripDate";//行程日期
    String scenicId = "scenicId";//景点id

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<Object>> submitScenic(
            @FieldMap HashMap<String, String> paramsMap
    );
}
