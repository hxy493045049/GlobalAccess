package com.msy.globalaccess.business.travelAgency.search.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.search.SearchValueListBean;
import com.msy.globalaccess.data.bean.TravelDepBean;


/**
 * 查询契约类
 * Created by chensh on 2017/1/24 0024.
 */

public interface SearchContract {
    interface View extends BaseContract.View {
        /**
         * 得到团队类型
         *
         * @param object 数据
         */
        void getTeamType(SearchTeamTypeBean object);

        /**
         * 获得旅行社部门结果
         */
        void getTravelDep(TravelDepBean travelDepBean);

        /**
         * 返回支出/支入单位
         *
         * @param searchValueListBean 查询结果
         */
        void getScenicSearch(SearchValueListBean searchValueListBean);
    }

    interface Presenter extends BaseContract.Presenter {

        /**
         * 获取团队类型
         */
        void loadTeamType();

        /**
         * 查询旅行社部门
         */
        void loadTravelDep(String travelDepName);

        /**
         * 景区查询
         */
        void loadScenicSearch();

        /**
         * 保险公司查询
         */
        void loadInsuranceSearch();

        /**
         * 车队查询
         */
        void loadVehicleSearch();

        /**
         * 酒店查询
         */
        void loadHotelSearch();

        /**
         * 旅行社查询
         */
        void loadTravelAgentSearch();
    }
}
