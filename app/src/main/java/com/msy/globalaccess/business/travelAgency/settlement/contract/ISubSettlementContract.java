package com.msy.globalaccess.business.travelAgency.settlement.contract;

import android.support.annotation.Nullable;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;

import java.util.List;

/**
 * Created by hxy on 2017/1/24 0024.
 * 结算管理列表的契约
 */
public interface ISubSettlementContract {
    interface View extends BaseContract.View {
        void handlerListData(@ResultCode int result, @RequestType int code, List<SettlementListBean> data);

        void onListError(@ResultCode int resultCode, @RequestType int type, @Nullable String message);

        String getDataType();

        int getPage();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadLatestData( );

        void loadMore( );
    }
}
