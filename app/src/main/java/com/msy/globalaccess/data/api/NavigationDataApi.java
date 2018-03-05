package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean;
import com.msy.globalaccess.data.bean.navigation.NavigationDataBean_Old;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by WuDebin on 2017/2/21.
 */

public interface NavigationDataApi {

    String  APP_STATISTICS="appStatistics";

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<NavigationDataBean_Old>> getGuideData(
            @FieldMap HashMap<String, String> paramsMap
    );

    String statistics_index_method = "appIndexStatistics";

    String teamCreateStartDate = "teamCreateStartDate";
    String teamCreateEndDate = "teamCreateEndDate";
    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<NavigationDataBean>> getNavigationData(
            @FieldMap HashMap<String, String> paramsMap
    );
}
