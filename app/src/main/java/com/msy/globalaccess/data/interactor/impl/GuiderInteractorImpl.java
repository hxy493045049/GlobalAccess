package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.guider.DelegateBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBeanWrapper;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.data.bean.guider.GuiderListWrapper;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.data.interactor.IGuiderInteractor;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by shawn on 2017/3/16 0016.
 * <p>
 * description : 导游相关的接口
 */
public class GuiderInteractorImpl implements IGuiderInteractor {
    private GuiderApi.QueryTeamGuiderApi teamGuiderApi;
    private GuiderApi.ModifyGuiderApi modifyGuiderApi;
    private GuiderApi.QueryFreeGuderApi freeGuderApi;
    private GuiderApi.QueryGuiderAttrApi queryGuiderAttrApi;
    private GuiderApi.DelegateApi delegateApi;

    @Inject
    GuiderInteractorImpl(Retrofit retrofit) {
        teamGuiderApi = retrofit.create(GuiderApi.QueryTeamGuiderApi.class);
        modifyGuiderApi = retrofit.create(GuiderApi.ModifyGuiderApi.class);
        freeGuderApi = retrofit.create(GuiderApi.QueryFreeGuderApi.class);
        queryGuiderAttrApi = retrofit.create(GuiderApi.QueryGuiderAttrApi.class);
        delegateApi = retrofit.create(GuiderApi.DelegateApi.class);
    }

    @Override
    public Subscription getTeamGuiderList(RequestCallBack<List<GuiderListBean>> callBack, HashMap<String, String>
            param) {
        callBack.beforeRequest();
        return teamGuiderApi.getTeamGuiderList(NetUtil.getCacheControl(), param)
                .compose(RxJavaUtils.<BaseBean<GuiderListWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<GuiderListWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<GuiderListWrapper>, List<GuiderListBean>>() {
                    @Override
                    public List<GuiderListBean> call(BaseBean<GuiderListWrapper> baseBean) {
                        return baseBean.getData().getTeamGuideList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    public Subscription getFreeGuiderList(RequestCallBack<List<FreeGuiderListBean>> callBack, HashMap<String,
            String> param) {
        callBack.beforeRequest();
        return freeGuderApi.getFreeGuiderApi(param)
                .compose(RxJavaUtils.<BaseBean<FreeGuiderListBeanWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<FreeGuiderListBeanWrapper>defaultBaseFlatMap())
                .map(new Func1<BaseBean<FreeGuiderListBeanWrapper>, List<FreeGuiderListBean>>() {
                    @Override
                    public List<FreeGuiderListBean> call(BaseBean<FreeGuiderListBeanWrapper> baseBean) {
                        return baseBean.getData().getTourGuideList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    public Subscription modifyGuider(RequestCallBack<NoDataBean> callBack, HashMap<String, String> params) {
        callBack.beforeRequest();
        return modifyGuiderApi.modifyGuiderList(params)
                .compose(RxJavaUtils.<NoDataBean>defaultSchedulers())
                .flatMap(RxJavaUtils.defaultNoDataFlatMap())
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    public Subscription whetherCanDeleteGuider(RequestCallBack<NoDataBean> callBack, HashMap<String, String> params) {
        callBack.beforeRequest();
        return queryGuiderAttrApi.queryDeleteAttr(params)
                .compose(RxJavaUtils.<NoDataBean>defaultSchedulers())
                .flatMap(RxJavaUtils.defaultNoDataFlatMap())
                .subscribe(new DefaultSubscriber<>(callBack));
    }

    @Override
    public Subscription loadDelegate(RequestCallBack<List<TouristDelegateBean>> callBack, HashMap param) {
        callBack.beforeRequest();

        return delegateApi.Delegate(param)
                .compose(RxJavaUtils.<BaseBean<DelegateBean>>defaultSchedulers())
                .flatMap(RxJavaUtils.<DelegateBean>defaultBaseFlatMap())
                .flatMap(new Func1<BaseBean<DelegateBean>, Observable<BaseBean<DelegateBean>>>() {
                    @Override
                    public Observable<BaseBean<DelegateBean>> call(BaseBean<DelegateBean> base) {
                        DelegateBean wrapper = base.getData();
                        // TODO: 2017/7/5 0005 小圆点不做处理
                        return Observable.just(base);
                    }
                })
                .map(new Func1<BaseBean<DelegateBean>, List<TouristDelegateBean>>() {
                    @Override
                    public List<TouristDelegateBean> call(BaseBean<DelegateBean> base) {
                        return base.getData().getTourGuideList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));

    }
}
