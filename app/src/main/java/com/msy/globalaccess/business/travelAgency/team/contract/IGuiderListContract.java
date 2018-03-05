package com.msy.globalaccess.business.travelAgency.team.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;

import java.util.List;

/**
 * Created by hxy on 2017/3/15 0015.
 * <p>
 * description :
 */

public interface IGuiderListContract {
    interface View extends BaseContract.View {

        String getTeamInfoId();

        List<GuiderListBean> getTeamGuideList();

        List<GuiderListBean> getUncheckGuideList();

        void modifyGuiderSuccess();

        void handleData(@ResultCode int resultCode, List<GuiderListBean> data);

        void onError(@ResultCode int resultCode, String errorMsg);

        void showCommitLoading();

        void hideCommitLoading();

        void canDelete();

    }

    interface Presenter extends BaseContract.Presenter {
        void loadGuiderList();

        void modifyGuider();

        void whetherCanDeleteGuider();
    }
}
