package com.msy.globalaccess.business.travelAgency.team.contract;

import android.support.annotation.Nullable;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;

import java.util.List;

/**
 * Created by shawn on 2017/3/24 0024.
 * <p>
 * description :
 */

public interface IAddGuiderContract {
    interface View extends BaseContract.View {
        String getGuiderIdentityNum();

        String getGuiderName();

        String getGuiderAffiliateUnit();

        String getCity();

        String getLanguageType();

        String getCityLevel();

        String getTeamInfoId();

        String getRegisterProvince();

        void onLanguageCallBack(List<KeyMapBean> languageBeen);

        void onCityCallBack(List<CityBean> cityBeen);

        void onGuiderListCallBack(@ResultCode int result, List<FreeGuiderListBean> tourGuideList);

        void onGuiderListFailed(@ResultCode int resultCode, @Nullable String message);

    }

    interface Presenter extends BaseContract.Presenter {
        void loadGuiderList();

        void loadCityList();

        void loadLanguage();
    }
}
