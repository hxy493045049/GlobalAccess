package com.msy.globalaccess.business.travelAgency.team.contract;

import com.msy.globalaccess.base.BaseContract;

/**
 * Created by pepys on 2017/2/15.
 * description: 审批
 */
public interface ApprovalContract {
    interface View extends BaseContract.View {
        //----------------审批团队状态--------------
        String getTeamID();
        int getOpType();
        int getOpStatus();
        String getRemark();
        void approvalSuccess(String message);
        void approvalFailure();
        //----------------审批委派导游--------------
        String getTeamGuideID();
        String getApprovalType();
        void approvalDelegateTouristSuccess(String message);
        void approvalDelegateTouristFailure();
    }

    interface Presenter extends BaseContract.Presenter {
        void loadApproval();
        void loadApprovalDelegateTourist();
    }
}
