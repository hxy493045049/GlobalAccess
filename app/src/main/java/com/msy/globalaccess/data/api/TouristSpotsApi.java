package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.city.CityBeanWrapper;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pepys on 2017/3/21.
 * description:选择景点
 */
public interface TouristSpotsApi {
    /*查询景点数据*/
    interface SpotList {
        String method = "appScenicSearch";
        String currentPageNum = "currentPageNum";
        String showNum = "showNum";
        String userId = "userId";
        String isAcc = "isAcc";
        String scenicName = "scenicName";
        String cityName = "cityName";
        String scenicLevel = "scenicLevel";
        String isAlaway = "isAlaway";
        /**
         * 请求景点列表
         *
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchScenicBean>> getSpotList(@FieldMap HashMap<String, String> paramsMap);
    }

    /*城市列表*/
    interface CityList{

        String method = "appQueryPubCity";
        String cityId = "cityId";
        String cityLevel = "cityLevel";
        String cityCode = "cityCode";
        String foreigh = "foreigh";
        String pcityId = "pcityId";
        String cityName = "cityName";
        String province = "province";
        String xian = "xian";
        String city = "city";
        String isAlaway = "isAlaway";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<CityBeanWrapper>> getCityList(@FieldMap HashMap<String, String> paramsMap);
    }

}
