package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.team.TeamListWrapper;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pepys on 2017/2/10.
 * description:团队API
 */
public interface TeamServiceApi {
    interface TeamListApi {
        //必填参数
        String method = "appTeamListSearch";//固定值
        String currentPageNum = "currentPageNum";//查看数据页数
        String showNum = "showNum";//显示数据条数
        String userId = "userId";//用户id

        //可选参数
        String optionSearchType = "searchType";//查询类型:0:出团审批;1:变更审批;2：作废审批
        String optionTeamCode = "teamCode";//团队编号
        String optionTeamStatus = "teamStatus";//团队状态:0:编辑中;1:已提交;2:生效;3:作废;
        String optionTravelAgentId = "travelAgentId";//旅行社Id,旅行社查询接口中获取
        String optionTravelDepId = "travelDepId";//部门Id,旅行社部门查询接口中获取
        String optionTeamStartDate = "teamStartDate";//行程开始日期
        String optionTeamEndDate = "teamEndDate";//行程结束日期
        String optionTeamTypeId = "teamTypeId";//团队类型Id,团队类型查询接口中获取


        /**
         * 请求团队列表
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TeamListWrapper>> getTeamList(@Header("Cache-Control") String cacheControl, @FieldMap
                HashMap<String, String> paramsMap);
    }

    /**
     * 团队详情请求参数
     */
    interface TeamDetailApi {
        String TEAM_DETAIL = "appTeamInfoDetail";
        String USER_ID = "userId";
        String TEAM_ID = "teamId";

        /**
         * 请求团队详情
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<TeamDetailBean>> getTeamDetail(
                @FieldMap HashMap<String, String> paramsMap);

        //必填参数
        String method_appTeamIsChange = "appTeamIsChange";//固定值

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<NoDataBean>> checkIsChange(@FieldMap HashMap<String, String> paramsMap);
    }
}
