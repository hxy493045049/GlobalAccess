package com.msy.globalaccess.business.travelAgency.team.contract;

import android.support.annotation.Nullable;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.team.TeamListBean;

import java.util.List;

/**
 * Created by hxy on 2017/2/7.
 * class description:
 */

public interface ISubTeamContract {
    interface View extends BaseContract.View {
        void handlerListData(@ResultCode int resultCode, @RequestType int code, List<TeamListBean> data);

        void onError(@ResultCode int resultCode, @RequestType int type, @Nullable String message);

        String getDataType();

        int getPage();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadLatestData();

        void loadMore( );
    }
}
