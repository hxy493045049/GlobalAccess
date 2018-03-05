package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsParentBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by WuDebin on 2017/3/21.
 */

public interface TravelAttractionsApi {

    interface QueryTravelAttractionsApi{
        //必填参数
        String method = "appTripScenicSearch";
        String teamId="teamId";

        /**
         * 获取行程景点列表接口
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TravelAttractionsParentBean>> getTravelAttractionsList(@FieldMap HashMap<String, String> paramsMap);
    }

    interface CommitTravelAttractionsApi{
        //必填参数
        String method = "appSubmitChangeScenic";
        String teamId="teamId";
        String userId="userId";
        String teamTripId="teamTripId";
        String tripScenicList="tripScenicList";

        /**
         * 提交行程景点列表
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TravelAttractionsBean>> commitTravelAttractionsList(@FieldMap HashMap<String, String> paramsMap);
    }

    interface queryTripScenicIsOrderApi{
        //必填参数
        String method = "appTripScenicIsOrder";
        String tripScenicId="tripScenicId";

        /**
         * 查询景点是否被预定接口
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TravelAttractionsBean>> queryTripScenicIsOrder(@FieldMap HashMap<String, String> paramsMap);
    }
}
