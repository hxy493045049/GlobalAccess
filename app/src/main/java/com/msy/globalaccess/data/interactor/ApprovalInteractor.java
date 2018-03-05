package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import java.util.HashMap;

import rx.Subscription;

/**
 * Created by pepys on 2017/2/15.
 * description:审批接口
 */
public interface ApprovalInteractor<T> {
    Subscription loadApproval(RequestCallBack<T> callBack, String userID, String teamID, int opType, int opStatus, String remark);
    Subscription loadApprovalDelegateTourist(RequestCallBack<T> callBack, HashMap params);
}
