package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.statistics.AddressStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTimeCountListWrapper;
import com.msy.globalaccess.data.bean.statistics.TourismSummaryBean;
import com.msy.globalaccess.data.bean.statistics.TouristSexStatisticsListBean;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;
import com.msy.globalaccess.data.bean.team.TeamInfoStatisticsBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 统计相关的接口
 */

public interface StatisticsApi {
    interface AddressSourceStatisticsApi {//接口33.	旅游局团队地区统计查询
        String appTravelCustomSourceStatistics = "appTravelCustomSourceStatistics";
        String travelAgentId = "travelAgentId";//旅行社Id
        String teamDate = "teamDate";//行程日期（yyyy-MM-dd）
        String teamStauts = "teamStauts";//团队状态：空值：查询全部；0：编辑中；1：已提交；2：生效；3：作废
        String teamTypeId = "teamTypeId";//团队类型Id,团队类型查询接口中获取，空值：查询全部
        String customSourceType = "customSourceType";//客源地：空值：查询全部;0:国内；1：入境
        String userId = "userId";//用户Id


        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<AddressStatisticsBean>> submitScenic(@FieldMap HashMap<String, String> paramsMap);
    }

    interface TeamInfoStatisticsApi {//接口32.	旅游局团队信息统计查询

        String method = "appTravelTeamInfoStatistics";
        String userId = "userId";//用户Id
        String travelAgentId = "travelAgentId";//旅行社ID
        String teamDate = "teamDate";//行程日期（yyyy-MM-dd HH:mm）
        String teamCreateStartDate = "teamCreateStartDate";//团队创建开始日期（yyyy-MM-dd）
        String teamCreateEndDate = "teamCreateEndDate";//团队创建结束日期（yyyy-MM-dd）
        String teamTypeId = "teamTypeId";//团队类型Id,团队类型查询接口中获取

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TeamInfoStatisticsBean>> loadTeamInfo(@FieldMap HashMap<String, String> paramsMap);
    }

    interface AuthenticationStatisticsApi {//接口34.	旅游局团队认证统计查询
        String appTravelTeamCheckStatistics = "appTravelTeamCheckStatistics";
        String userId = "userId";//用户Id
        String searchType = "searchType";//查询类型 ：0：旅行社；1：景区
        String travelAgentId = "travelAgentId";//旅行社ID
        String scenicId = "scenicId";//景点ID
        String teamCheckStartDate = "teamCheckStartDate";//行程开始日期（yyyy-MM-dd HH:mm）
        String teamCheckEndDate = "teamCheckEndDate";//行程结束日期（yyyy-MM-dd HH:mm）
        String teamTypeId = "teamTypeId";//团队类型Id,团队类型查询接口中获取

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TravelTeamStatisticsBean>> submitScenic(@FieldMap HashMap<String, String> paramsMap);
    }

    interface TourismDataSummaryApi {//接口36.	旅游局数据概览
        //必填参数
        //        String method = "appTravelDataStatistics";//固定值
        String method = "appNewTravelDataStatistics";//固定值  新接口。加入其他统计数据
        String userId = "userId";//用户id
        String searchDate = "searchDate";//用户id

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TourismSummaryBean>> getSummaryData(@FieldMap HashMap<String, String> paramsMap);
    }

    interface GuestSourceStatisticsApi {//接口39.	客源地统计
        String method = "appCustomSourceStatistics";
        String userId = "userId";
        String sourceType = "sourceType";
        String searchDateStart = "searchDateStart";
        String searchDateEnd = "searchDateEnd";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<GuestSourceBeanWrapper>> getGuestSourceData(@FieldMap HashMap<String, String> paramsMap);
    }

    interface ScenicAuthTimeApi {
        String method = "appScenicCheckTimeStatistics";
        String userId = "userId";
        String searchDateStart = "searchDateStart";
        String searchDateEnd = "searchDateEnd";
        String scenicId = "scenicId";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<ScenicCheckTimeCountListWrapper>> getStatusData(@FieldMap HashMap<String,
                String> paramsMap);
    }

    interface PeopleAndDayStatisticsApi {
        String method = "appCustomDaysStatistics";
        String userId = "userId";
        String searchDate = "searchDate";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<PeopleAndDayStatisticsBeanWrapper>> getStatusData(@FieldMap HashMap<String,
                String> paramsMap);
    }

    interface GuestAgeStatisticsApi {
        String method = "appCustomAgeStatistics";
        String userId = "userId";
        String searchDateStart = "searchDateStart";
        String searchDateEnd = "searchDateEnd";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<GuestAgeStatisticsBeanWrapper>> getStatusData(@FieldMap HashMap<String, String> paramsMap);
    }

    /**
     * Created by pepys on 2017/7/10
     * description: 游客性别统计
     *
     */
    interface TouristSexStatisticsApi {
        //必填参数
        String method = "appCustomSexStatistics";//固定值
        String userId = "userId";//用户id
        String searchDateStart = "searchDateStart";//开始时间
        String searchDateEnd = "searchDateEnd";//结束时间


        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TouristSexStatisticsListBean>> getSexStatisticsData(@FieldMap HashMap<String, String>
                                                                                        paramsMap);
    }
}
