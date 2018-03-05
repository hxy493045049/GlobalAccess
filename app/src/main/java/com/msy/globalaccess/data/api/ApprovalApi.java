package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.NoDataBean;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by pepys on 2017/2/15.
 * description: 审批
 */
public interface ApprovalApi {

    String METHOD = "appTeamOpRecord";
    String USERID = "userId";
    String TEAMID = "teamId";
    String OPTYPE = "opType";
    String OPSTATUS = "opStatus";
    String REMARK = "remark";

    /**
     * 审批团队
     */
    @FormUrlEncoded
    @POST("appInterface.do")
    Observable<NoDataBean> Approval(@FieldMap HashMap<String, String> paramsMap);

    interface ApprovalDelegateTouristApi{
        String METHOD = "appApprovalDelegateTeamGuide";
        String TEAMGUIDEID = "teamGuideId";
        String APPROVALTYPE = "approvalType";
        String REMARK = "remark";
        /**
         * 审批委派导游
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<NoDataBean> ApprovalDelegateTourist(@FieldMap HashMap<String, String> paramsMap);
    }
}
