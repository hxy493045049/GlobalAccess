package com.msy.globalaccess.business.travelAgency.team.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;


/**
 * Created by pepys on 2017/2/10.
 * description:团队详情
 */
public interface TeamDetailContract {
    interface View extends BaseContract.View {
        /*加载团队详情*/
        void showTeamDetail(TeamDetailBean teamDetail);
        /*加载失败*/
        void LoadFailure();
        String getTeamID();
        int getOpType();
        int getOpStatus();
        String getRemark();
        void approvalSuccess(String message);

        void canChange();
        void noCanChange(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        void loadTeamDetail();
        void loadApproval();
        void checkIsChange();
    }
}
