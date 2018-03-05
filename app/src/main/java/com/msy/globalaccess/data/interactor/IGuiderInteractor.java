package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by shawn on 2017/3/16 0016.
 * <p>
 * description :
 */

public interface IGuiderInteractor {
    //获取团队导游列表
    Subscription getTeamGuiderList(RequestCallBack<List<GuiderListBean>> callBack, HashMap<String, String> param);

    Subscription getFreeGuiderList(RequestCallBack<List<FreeGuiderListBean>> callBack, HashMap<String, String>
            param);

    Subscription modifyGuider(RequestCallBack<NoDataBean> callBack, HashMap<String, String> param);

    Subscription whetherCanDeleteGuider(RequestCallBack<NoDataBean> callBack, HashMap<String, String> param);

    Subscription loadDelegate(RequestCallBack<List<TouristDelegateBean>> callBack, HashMap param);
}
