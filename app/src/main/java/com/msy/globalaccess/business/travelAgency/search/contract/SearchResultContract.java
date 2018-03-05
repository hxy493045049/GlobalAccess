package com.msy.globalaccess.business.travelAgency.search.contract;

import android.support.annotation.Nullable;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WuDebin on 2017/2/15.
 */

public interface SearchResultContract {

    interface View extends BaseContract.View {
        /**
         * 得到团队查询的结果
         */
        void getTeamSearchResult(@ResultCode int result, @RequestType int code, List<TeamListBean> data);

        /**
         * 得到结算查询的结果
         */
        void getSettlementSearchResult(@ResultCode int result, @RequestType int code,List<SettlementListBean> data);

        void onError(@ResultCode int resultCode, @RequestType int type, @Nullable String message);

        /**
         * 加载委派导游查询结果
         * @param data
         */
        void loadDelegateListResult(@ResultCode int result, @RequestType int code,List<TouristDelegateBean> data);
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * @param searchCondition 查询条件
         * @param type 查询类型
         */
        void loadSearchResult(HashMap<String,Object> searchCondition,int type,int page);

        /**
         * @param type 查询类型
         */
        void loadMoreData( int type,int page);
    }
}
