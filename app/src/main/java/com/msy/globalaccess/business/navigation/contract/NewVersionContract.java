package com.msy.globalaccess.business.navigation.contract;

import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.UpdateVersionBean;

/**
 * Created by WuDebin on 2017/2/7.
 */

public class NewVersionContract {

    public interface View extends BaseContract.View {
        String getdownUrl();
        String getPath();
        void nextActivity();
        void loadNewVersionSuccess(UpdateVersionBean updateVersionBean);
        void loadNewVersionFail();
        void setProgress(int soFarBytes, int totalBytes,int speed);
        void downSuccess();
        void downFail();
    }

    public interface Presenter extends BaseContract.Presenter {

        /**
         * APP是否需要升级
         */
        void loadNewVersion();

        void downNewApp();

        void paushDown();
    }

}
