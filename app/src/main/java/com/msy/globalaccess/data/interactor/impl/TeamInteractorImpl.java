package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.TeamServiceApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.team.TeamListBean;
import com.msy.globalaccess.data.bean.team.TeamListWrapper;
import com.msy.globalaccess.data.interactor.ITeamInteractor;
import com.msy.globalaccess.event.TeamBadgeInfo;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by pepys on 2017/2/10.
 * description:
 */
public class TeamInteractorImpl implements ITeamInteractor {

    private TeamServiceApi.TeamListApi mTeamListApi;
    private TeamServiceApi.TeamDetailApi teamDetailApi;

    @Inject
    TeamInteractorImpl(Retrofit retrofit) {
        mTeamListApi = retrofit.create(TeamServiceApi.TeamListApi.class);
        teamDetailApi = retrofit.create(TeamServiceApi.TeamDetailApi.class);
    }

    @Override
    public Subscription getTeamDetail(final RequestCallBack<TeamDetailBean> callBack, HashMap<String,
            String> param) {
        callBack.beforeRequest();
        return teamDetailApi.getTeamDetail(param)
                .compose(RxJavaUtils.<BaseBean<TeamDetailBean>>defaultSchedulers())
                .subscribe(new Subscriber<BaseBean<TeamDetailBean>>() {
                    @Override
                    public void onCompleted() {
                        callBack.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.internet_error);
                        callBack.after();
                        Logger.e(e, "获取详情失败");
                    }

                    @Override
                    public void onNext(BaseBean<TeamDetailBean> teamDetailBeanBaseBean) {
                        if (teamDetailBeanBaseBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(teamDetailBeanBaseBean.getData());
                        } else {
                            ToastUtils.showToast(teamDetailBeanBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, teamDetailBeanBaseBean.getMessage());
                        }
                    }
                });

    }

    @Override
    public Subscription checkIsChange(@NotNull final RequestCallBack<NoDataBean> callBack, @NotNull HashMap<String, String> param) {
        callBack.beforeRequest();
        return teamDetailApi.checkIsChange(param)
                .compose(RxJavaUtils.<BaseBean<NoDataBean>>defaultSchedulers())
                .subscribe(new Subscriber<BaseBean<NoDataBean>>() {
                    @Override
                    public void onCompleted() {
                        callBack.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        callBack.after();
                        ToastUtils.showToast(R.string.internet_error);
                        Logger.e(e, "TeamInteractorImpl");
                    }

                    @Override
                    public void onNext(BaseBean<NoDataBean> noDataBeanBaseBean) {
                        if (noDataBeanBaseBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(noDataBeanBaseBean.getData());
                        } else {
                            ToastUtils.showToast(noDataBeanBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, noDataBeanBaseBean.getMessage());
                        }
                    }
                });
    }


    @Override
    public Subscription getDataByPage(RequestCallBack<List<TeamListBean>> callBack, HashMap<String, String>
            param) {
        return loadListData(param, callBack);
    }


    private Subscription loadListData(HashMap<String, String> param, final RequestCallBack<List<TeamListBean>>
            callBack) {
        callBack.beforeRequest();
        return mTeamListApi.getTeamList(NetUtil.getCacheControl(), param)
                .compose(RxJavaUtils.<BaseBean<TeamListWrapper>>defaultSchedulers())
                .flatMap(RxJavaUtils.<TeamListWrapper>defaultBaseFlatMap())
                .flatMap(new Func1<BaseBean<TeamListWrapper>, Observable<BaseBean<TeamListWrapper>>>() {
                    @Override
                    public Observable<BaseBean<TeamListWrapper>> call(BaseBean<TeamListWrapper> base) {
                        TeamListWrapper wrapper = base.getData();
                        //发送一个广播,通知团队列表更新小圆点
                        TeamBadgeInfo badge = new TeamBadgeInfo();
                        badge.setCancelNum(wrapper.getCancelNum());
                        badge.setChangeNum(wrapper.getChangeNum());
                        badge.setGroupNum(wrapper.getGroupNum());
                        badge.setTotalNum(wrapper.getTotalNum());
                        RxBus.getInstance().post(badge);
                        return Observable.just(base);
                    }
                })
                .map(new Func1<BaseBean<TeamListWrapper>, List<TeamListBean>>() {
                    @Override
                    public List<TeamListBean> call(BaseBean<TeamListWrapper> wrapper) {
                        return wrapper.getData().getTeamList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }

}
