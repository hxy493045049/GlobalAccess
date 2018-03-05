package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.guider.DelegateBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBeanWrapper;
import com.msy.globalaccess.data.bean.guider.GuiderListWrapper;
import com.msy.globalaccess.data.bean.base.NoDataBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shawn on 2017/3/16 0016.
 * <p>
 * description : 导游相关的api
 */

public interface GuiderApi {
    interface QueryTeamGuiderApi {
        //必填参数
        String method = "appQueryTeamGuide";

        String teamInfoId = "teamInfoId";
        String userId = "userId";
        String delFlag = "delFlag";

        /**
         * 请求团队导游列表
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<GuiderListWrapper>> getTeamGuiderList(@Header("Cache-Control") String cacheControl,
                                                                  @FieldMap HashMap<String, String> paramsMap);
    }

    interface ModifyGuiderApi {
        //必填参数
        String method = "appChangeTeamGuide";
        String teamGuideList = "teamGuideList";
        String userId = "userId";

        /**
         * 请求修改导游
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<NoDataBean> modifyGuiderList(@FieldMap HashMap<String, String> paramsMap);
    }

    interface QueryFreeGuderApi {
        //必填参数
        String method = "appQueryTourGuide";
        String version = "version";
        String userId = "userId";
        String teamInfoId = "teamInfoId";

        //可选参数
        String cityId = "cityId";
        String delTag = "delTag";
        String guideCode = "guideCode";
        String name = "name";
        String travelAgentName = "travelAgentName";
        String language = "language";
        String checkTag = "checkTag";
        String city = "city";

        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<FreeGuiderListBeanWrapper>> getFreeGuiderApi(@FieldMap HashMap<String, String> paramsMap);
    }

    interface QueryGuiderAttrApi {
        //必填参数
        String method = "appCheckTeamGuideDelOrCancel";
        String teamGuideList = "teamGuideList";
        String userId = "userId";

        /**
         * 查询是否可以删除
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<NoDataBean> queryDeleteAttr(@FieldMap HashMap<String, String> paramsMap);
    }

    /**
     * Created by pepys on 2017/2/15.
     * description: 导游委派列表
     */
    interface DelegateApi {

        String METHOD = "appQueryDelegateTeamGuide";
        String USERID = "userId";
        String CURRENTPAGENUM = "currentPageNum";
        String SHOWNUM = "showNum";
        String OPTYPE = "opType";
        String OPSTATUS = "operStatus";
        String TEAMCODE = "teamCode";
        String CREATETIMESTART = "createTimeStart";
        String CREATETIMEEND = "createTimeEnd";

        /**
         * 审批
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<DelegateBean>> Delegate(@FieldMap HashMap<String, Object> paramsMap);
    }
}
