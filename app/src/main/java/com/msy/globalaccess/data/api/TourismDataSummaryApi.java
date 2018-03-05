package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.TourismSummaryBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shawn on 2017/5/23 0023.
 * <p>
 * description : 旅游局数据概览
 * 废弃,已合并到{@link com.msy.globalaccess.data.api.StatisticsApi.TourismDataSummaryApi}中
 */
@Deprecated
public interface TourismDataSummaryApi {

    //必填参数
//    String method = "appTravelDataStatistics";//固定值
    String method = "appNewTravelDataStatistics";//固定值
    String userId = "userId";//用户id
    String searchDate = "searchDate";//用户id

    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<TourismSummaryBean>> getSummaryData(@FieldMap HashMap<String, String> paramsMap);
}
