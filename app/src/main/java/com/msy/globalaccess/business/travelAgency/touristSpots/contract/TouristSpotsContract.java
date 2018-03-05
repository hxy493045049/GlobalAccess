package com.msy.globalaccess.business.travelAgency.touristSpots.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pepys on 2017/3/21.
 * description: 选择景点
 */
public interface TouristSpotsContract {

    interface View extends BaseContract.View {
        /**
         * 加载景点列表成功
         * @param searchScenicList
         */
        void loadSpotsSuccess(ArrayList<ScenicListBean> searchScenicList);
        /* 加载景点列表失败 */
        void loadSpotsFailure();

        /**
         * 加载城市列表成功
         * @param cityList
         */
        void loadCitySuccess(ArrayList<CityBean> cityList);
        /* 加载城市列表失败 */
        void loadCityFailure(String msg);

        /**
         * 加载景区级别成功
         * @param keyMapList
         */
        void loadLevelSuccess(List<KeyMapBean> keyMapList);

        void loadLevelFailure(String msg);

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
    }

    interface Presenter extends BaseContract.Presenter {
        void getSpotsList();
        void getCityList();
        void getLevel();
    }
}
