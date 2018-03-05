package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by WuDebin on 2017/2/15.
 */

public interface SearchResultInteractor<T>   {
    //获取团队查询结果
    Subscription loadTeamSearchResult(RequestCallBack<T> callBack, HashMap<String, Object> searchCondition, int page);

    //获取结算查询结果
    Subscription loadSettlementSearchResult(RequestCallBack<T> callBack, HashMap<String, Object> searchCondition, int page);

    //获取游客查询结果
    Subscription loadMemberSearchResult(RequestCallBack<T> callBack, HashMap<String, Object> searchCondition, int page);

}
