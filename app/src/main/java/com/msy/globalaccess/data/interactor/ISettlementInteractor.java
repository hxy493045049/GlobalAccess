package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by hxy on 2017/2/14.
 * class description: 获取结算详情的交互器
 */

public interface ISettlementInteractor {
    Subscription getSettlementDetail(@NotNull RequestCallBack<SettlementDetailBean> callBack, @NotNull
            HashMap<String, String> param);

    Subscription settlement(@NotNull RequestCallBack<NoDataBean> callBack, @NotNull HashMap<String, String> param);

    Subscription getDataByPage(@NotNull RequestCallBack<List<SettlementListBean>> callBack, @NotNull HashMap<String,
            String> param);
}
