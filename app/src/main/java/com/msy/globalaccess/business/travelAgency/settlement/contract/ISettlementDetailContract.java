package com.msy.globalaccess.business.travelAgency.settlement.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;

/**
 * Created by WuDebin on 2017/1/20.
 */

public interface ISettlementDetailContract {

    interface View extends BaseContract.View {
        void handlerDetailData(SettlementDetailBean bean);

        void onError(@ResultCode int code, String errorMessage);

        void showLoading();

        void dismissLoading();

        void handlerSettlementData(NoDataBean bean);

        String getTeamAuditId();

        String getUserId();

        String getPassWord();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadSettlementDetail();

        void settlement();

    }

}
