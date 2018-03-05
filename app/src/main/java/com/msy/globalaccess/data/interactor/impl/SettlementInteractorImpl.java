package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.SettlementApi;
import com.msy.globalaccess.data.api.SettlementApi.SettlementDetailApi;
import com.msy.globalaccess.data.api.SettlementApi.SettlementListApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListBean;
import com.msy.globalaccess.data.bean.settlement.SettlementListWrapper;
import com.msy.globalaccess.data.interactor.ISettlementInteractor;
import com.msy.globalaccess.event.SettlementBadgeInfo;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by hxy on 2017/2/8.
 * class description:
 */

public class SettlementInteractorImpl implements ISettlementInteractor {
    private SettlementListApi listApi;
    private SettlementDetailApi detailApi;
    private SettlementApi.Settlement settlementApi;

    @Inject
    SettlementInteractorImpl(Retrofit retrofit) {
        listApi = retrofit.create(SettlementListApi.class);
        detailApi = retrofit.create(SettlementDetailApi.class);
        settlementApi = retrofit.create(SettlementApi.Settlement.class);
    }

    @Override
    public Subscription getDataByPage(@NotNull RequestCallBack<List<SettlementListBean>> callBack, HashMap<String,
            String> param) {
        return loadListData(param, callBack);
    }

    @Override
    public Subscription getSettlementDetail(final RequestCallBack<SettlementDetailBean> callBack, HashMap<String,
            String> param) {

        callBack.beforeRequest();
        return detailApi.getSettlementDetail(param)
                .compose(RxJavaUtils.<BaseBean<SettlementDetailBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<SettlementDetailBean>defaultBaseFlatMap())
                .map(new Func1<BaseBean<SettlementDetailBean>, SettlementDetailBean>() {
                    @Override
                    public SettlementDetailBean call(BaseBean<SettlementDetailBean> wrapper) {
                        return wrapper.getData();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    public Subscription settlement(RequestCallBack<NoDataBean> callBack, HashMap<String, String> param) {
        callBack.beforeRequest();
        return settlementApi.settlement(param)
                .compose(RxJavaUtils.<NoDataBean>defaultSchedulers())
                .flatMap(RxJavaUtils.defaultNoDataFlatMap())
                .subscribe(new DefaultSubscriber<>(callBack));
    }


    private Subscription loadListData(HashMap<String, String> param, final RequestCallBack<List<SettlementListBean>>
            callBack) {
        callBack.beforeRequest();
        return listApi.getSettlementList(param)
                .compose(RxJavaUtils.<BaseBean<SettlementListWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<SettlementListWrapper>defaultBaseFlatMap())
                .flatMap(new Func1<BaseBean<SettlementListWrapper>, Observable<BaseBean<SettlementListWrapper>>>() {
                    @Override
                    public Observable<BaseBean<SettlementListWrapper>> call(BaseBean<SettlementListWrapper> base) {
                        //小圆点处理
                        SettlementListWrapper wrapper = base.getData();
                        SettlementBadgeInfo badgeInfo = new SettlementBadgeInfo();
                        badgeInfo.setTotalNum(wrapper.getTotalNum());
                        badgeInfo.setAddPrePayNum(wrapper.getAddPrePayNum());
                        badgeInfo.setPrePayNum(wrapper.getPrePayNum());
                        badgeInfo.setBackNum(wrapper.getBackNum());
                        RxBus.getInstance().post(badgeInfo);
                        return Observable.just(base);
                    }
                })
                .map(new Func1<BaseBean<SettlementListWrapper>, List<SettlementListBean>>() {
                    @Override
                    public List<SettlementListBean> call(BaseBean<SettlementListWrapper> base) {
                        return base.getData().getTeamAuditList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }
}
