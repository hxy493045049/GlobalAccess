package com.msy.globalaccess.business.travelAgency.delegate.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/2/15.
 * description: 加载需要审批导游列表
 */
public interface IDelegateContract {
    interface View extends BaseContract.View {

        String getOpStatus();
        int getPageNum();
        void delegateSuccess(ArrayList<TouristDelegateBean> touristDelegateList);
        void delegateFailure();

    }

    interface Presenter extends BaseContract.Presenter {
        void loadDelegate();
    }
}
