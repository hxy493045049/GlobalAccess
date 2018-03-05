package com.msy.globalaccess.business.main.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.search.SearchTravelAgentBean;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;

import java.util.ArrayList;

/**
 * 旅游局-旅行社-团队类型契约类
 * Created by WuDebin on 2017/5/16.
 */

public interface TourismMainContract {
    interface View extends BaseContract.View {
        /**
         * 把获取到的旅行社信息保存到数据库中
         */
        void saveTravelData(SearchTravelAgentBean data);

        /**
         * 获取旅游局团队类型
         */
        void getTeamType(ArrayList<TourismDropListBean.DropSelectableBean> data);

        /**
         * 获取景点数据
         */
        void getTouristData(ArrayList<ScenicListBean> data);

        /*起始页*/
        int currentPageNum();

        /* 显示数据条数 */
        int showNum();

        /*景区名称*/
        String scenicName();

        /*城市名称 */
        String cityName();

        /*景区级别 */
        String scenicLevel();

        /* 是否常用 ""不常用 1常用*/
        String isAlaway();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 获取所有的旅行社信息
         */
        void loadTravelData();

        /**
         * 获取旅游局团队类型
         */
        void loadTravelTeamType();

        /**
         * 获取所有景点数据
         */
        void loadTouristData();
    }
}

