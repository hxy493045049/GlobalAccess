package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.DataPreviewPresenterImpl;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.ScenicAuthTimePresenter;
import com.msy.globalaccess.business.touristAdmin.statistics.contract.impl.TeamStatisticsImpl;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.statistics.AddressStatisticsBean;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.GuestSourceBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.PeopleAndDayStatisticsBeanWrapper;
import com.msy.globalaccess.data.bean.statistics.TravelTeamStatisticsBean;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;

import rx.Subscription;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 数据概览中相关的各种统计图
 */

public interface IStatisticsInteractor {
    interface ITeamAddressStatisticsInteractor {
        Subscription requestAddressStatist(@NotNull RequestCallBack<AddressStatisticsBean> callBack, @NotNull
                HashMap<String, String> map);
    }

    interface ITeamInfoStatisticsInteractor {
        Subscription loadTeamInfoStatisticsData(@NotNull RequestCallBack<TeamStatisticsImpl.ResultBean> callBack,
                                                @NotNull HashMap<String, String> map);
    }

    interface IAuthenticstionStatisticsInteractor {
        Subscription requestAuthenticsStatist(@NotNull RequestCallBack<TravelTeamStatisticsBean> callBack, @NotNull
                HashMap<String, String> map);
    }

    interface ITourismDataSummaryInteractor {//旅游数据概览统计

        Subscription getSummary(@NotNull RequestCallBack<DataPreviewPresenterImpl.ResultBean> callBack,
                                @NotNull HashMap<String, String> param);
    }

    interface IGuestSourceStaticticsInteractor {//客源地统计

        Subscription getStaticticsData(@NotNull RequestCallBack<GuestSourceBeanWrapper.ResultBean> callBack,
                                       @NotNull HashMap<String, String> param);
    }

    interface IScenicAuthTimeInteractor {
        Subscription getStaticsData(@NotNull RequestCallBack<ScenicAuthTimePresenter.ResultBean> callBack, @NotNull
                HashMap<String, String> param);
    }

    interface IPeopleAndDayInteractor {
        Subscription getStatisticsData(@NotNull RequestCallBack<PeopleAndDayStatisticsBeanWrapper.ResultBean>
                                               callBack, @NotNull HashMap<String, String> param);
    }

    interface IGuestAgeInteractor {
        Subscription getStatisticsData(@NotNull RequestCallBack<GuestAgeStatisticsBeanWrapper.ResultBean>
                                               callBack, @NotNull HashMap<String, String> param);
    }

}
