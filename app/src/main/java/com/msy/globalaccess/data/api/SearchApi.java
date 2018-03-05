package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.search.SearchHotelBean;
import com.msy.globalaccess.data.bean.search.SearchInsuranceBean;
import com.msy.globalaccess.data.bean.search.SearchScenicBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.team.TeamMemberWrapper;
import com.msy.globalaccess.data.bean.TravelDepBean;
import com.msy.globalaccess.data.bean.search.SearchVehicleBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 查询接口
 * Created by chensh on 2017/2/10 0010.
 */

public interface SearchApi {
    //团队类型method
    String SEARCH_TEAM_TYPE = "appTeamTypeSearch";
    String TravelcurrentPageNum = "currentPageNum";
    String TravelshowNum = "showNum";
    String TraveluserId = "userId";
    String cityName = "cityName";
    String scenicLevel = "scenicLevel";
    String isAlaway = "isAlaway";

    /**
     * 团队类型
     *
     * @param paramsMap  参数
     * @return
     */
    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<SearchTeamTypeBean>> getTeamType(
            @FieldMap HashMap<String, String> paramsMap
    );

    /**
     * 旅行社部门查询
     */
    interface TravelDepApi {
        //固定值   Travel
        String Travelmethod = "appTravelDepListSearch";
        //
        String TraveltravelDepName = "travelDepName";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TravelDepBean>> loadTravelDep(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    /**
     * 转换器
     */
    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<BaseBean<Object>> SearchConvert(
            @FieldMap HashMap<String, String> paramsMap
    );


    /**
     * 景区查询
     */
    interface ScenicSearchApi {
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchScenicBean>> ScenicSearch(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    //固定值   method
    String appScenicSearch = "appScenicSearch";
    String scenicName = "scenicName";

    /**
     * 保险公司查询
     */
    interface InsuranceSearchApi {

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchInsuranceBean>> InsuranceSearch(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    //固定值   method
    String appInsuranceSearch = "appInsuranceSearch";
    String insuranceName = "insuranceName";

    /**
     * 车队查询
     */
    interface VehicleSearchApi {

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchVehicleBean>> VehicleSearch(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    //固定值   method
    String appVehicleSearch = "appVehicleSearch";
    String vehicleName = "vehicleName";

    /**
     * 酒店查询
     */
    interface HotelSearchApi {
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchHotelBean>> HotelSearch(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    //固定值   method
    String appHotelSearch = "appHotelSearch";
    String hotelName = "hotelName";

    /**
     * 旅行社查询
     */
    interface TravelAgentSearchApi {
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SearchTravelAgentBean>> HotelSearch(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    //固定值   method
    String appTravelAgentSearch = "appTravelAgentSearch";
    String travelAgentName = "travelAgentName";


    /**
     * 游客查询
     * 固定值 method
     */
    interface TeamMemberSearch {
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TeamMemberWrapper>> TeamMember(
                @FieldMap HashMap<String, String> paramsMap
        );
    }

    String appTeamMemberSearch = "appTeamMemberSearch";
}
