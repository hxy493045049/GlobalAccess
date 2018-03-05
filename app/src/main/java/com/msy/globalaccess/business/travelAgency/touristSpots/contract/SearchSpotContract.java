package com.msy.globalaccess.business.travelAgency.touristSpots.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/4/17.
 * description:
 */
public interface SearchSpotContract {

    interface View extends BaseContract.View{
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
        /* 是否常用 0不常用 1常用*/
        String isAlaway();

        /**
         * 加载景点列表成功
         * @param searchScenicList
         */
        void loadSpotsSuccess(ArrayList<ScenicListBean> searchScenicList);
        /* 加载景点列表失败 */
        void loadSpotsFailure();
    }

    interface Presenter extends BaseContract.Presenter {
        void getSpotsList();
    }
}
