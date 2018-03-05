package com.msy.globalaccess.business.demo.contract.impl;

import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.demo.contract.NewsContract;
import com.msy.globalaccess.common.enums.RequestType;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.NewsChannel;
import com.msy.globalaccess.data.bean.NewsSummary;
import com.msy.globalaccess.data.interactor.NewsInteractor;
import com.msy.globalaccess.data.interactor.NewsRemoteInteractor;
import com.msy.globalaccess.data.interactor.impl.NewsLocalInteractImpl;
import com.msy.globalaccess.data.interactor.impl.NewsRemoteInteractorImpl;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by hxy on 2016/12/12 0012.
 * <p>
 * description : 这仅仅是一个demo类,告诉你怎么用
 */
@Deprecated
public class NewPresenterImpl extends BasePresenterImpl<NewsContract.View> implements NewsContract.Presenter {

    private NewsInteractor<List<NewsChannel>> mNewsInteractor;
    private NewsRemoteInteractor<List<NewsSummary>> mNewsRemoteInteractor;

    /**
     * 依赖注入构造,下面参数根据去修选择
     *
     * @param newsInteractor 用于数据交互
     */
    @Inject
    public NewPresenterImpl(NewsLocalInteractImpl newsInteractor, NewsRemoteInteractorImpl remoteImpl) {
        mNewsInteractor = newsInteractor;
        mNewsRemoteInteractor = remoteImpl;
    }

    @Override
    public void onStart() {
        onChannelDbChanged();
        loadRemoteNews();
    }

    @Override
    public void onChannelDbChanged() {

        Subscription subscription = mNewsInteractor.loadLocalNews(new RequestCallBackAdapter<List<NewsChannel>>
                () {
            @Override
            public void success(List<NewsChannel> data) {
                mView.initViewPager(data);
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {

            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void loadRemoteNews() {
        Subscription subscription = mNewsRemoteInteractor.loadRemoteNews(new RequestCallBackAdapter<List<NewsSummary>>() {
            @Override
            public void success(List<NewsSummary> data) {
                // TODO: 2017/2/8
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
            }
        }, "a", "b", 0);
        cacheSubscription.add(subscription);
    }
}
