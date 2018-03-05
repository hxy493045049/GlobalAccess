package com.msy.globalaccess.business.demo.contract;


import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.data.bean.NewsChannel;

import java.util.List;

/**
 * Created by hxy on 2016/12/12 0012.
 * <p>
 * description :
 */
public interface NewsContract {
    interface View extends BaseContract.View {
        void initViewPager(List<NewsChannel> newsChannels);
    }

    interface Presenter extends BaseContract.Presenter {
        void onChannelDbChanged();
        void loadRemoteNews();
    }
}
