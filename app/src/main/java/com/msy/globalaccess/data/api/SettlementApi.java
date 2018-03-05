package com.msy.globalaccess.data.api;

import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListWrapper;

import java.util.HashMap;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hxy on 2017/2/13.
 * class description: 结算相关接口
 */

public interface SettlementApi {
    interface SettlementListApi {
        //必填参数
        String method = "appTeamAuditListSearch";//固定值
        String currentPageNum = "currentPageNum";//查看数据页数
        String showNum = "showNum";//显示数据条数
        String userId = "userId";//用户id

        //可选参数
        String optionSearchType = "searchType";//查询类型:0:出团审批;1:变更审批;2：作废审批
        String optionTeamCode = "teamCode";//团队编号
        String optionPayCode = "payCode";//支付编号
        String optionOutComeUnitCoding = "outComeUnitCoding";//支出单位编号
        String optionInComeUnitCoding = "inComeUnitCoding";//收入单位编号
        String optionIncreateTimeStart = "createTimeStart";//创建开始时间（yyyy-MM-dd）
        String optionIncreateTimeEnd = "createTimeEnd";//创建结束时间（yyyy-MM-dd）
        String optionSubmitPayTimeStart = "submitPayTimeStart";//支付开始时间（yyyy-MM-dd）
        String optionSubmitPayTimeEnd = "submitPayTimeEnd";//支付结束时间（yyyy-MM-dd）

        /**
         * 请求结算列表
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SettlementListWrapper>> getSettlementList(@FieldMap HashMap<String, String> paramsMap);
    }

    interface SettlementDetailApi {
        //必填参数
        String method = "appTeamAuditDetail";//固定值
        String teamAuditId = "teamAuditId";//结算单ID
        String userId = "userId";//用户id

        /**
         * 请求结算详情
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<BaseBean<SettlementDetailBean>> getSettlementDetail(@FieldMap HashMap<String, String> paramsMap);
    }

    //结算接口
    interface Settlement {
        String method = "appPayTeamAudit";
        String userId = "userId";
        String teamAuditId = "teamAuditId";
        String payPassword = "payPassword";

        /**
         * 结算
         */
        @FormUrlEncoded
        @POST("appInterface.do")
        Observable<NoDataBean> settlement(@FieldMap HashMap<String, String> paramsMap);

    }
}
