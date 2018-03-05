package com.msy.globalaccess.business.travelAgency.search.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.search.SearchBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shawn on 2017/7/3 0003.
 * <p>
 * description :
 */

public interface SearchDelegateGuiderContract {
    interface View extends BaseContract.View {
        void onQueryConditionCallback(List<SearchBean> data);

        List<SearchBean> getCurrentCondition();

        void onParamCallBack(HashMap<String, Object> param);
    }

    interface Presenter extends BaseContract.Presenter {
        void loadQueryCondition();

        void getQueryParam();
    }
}
