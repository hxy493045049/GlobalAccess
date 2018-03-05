package com.msy.globalaccess.data.interactor.impl;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.NewsChannel;
import com.msy.globalaccess.data.db.manager.NewsChannelManager;
import com.msy.globalaccess.data.interactor.NewsInteractor;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by hxy on 2016/12/13 0013.
 * <p>
 * description : it's just a demo for local
 */
@Deprecated
public class NewsLocalInteractImpl implements NewsInteractor<List<NewsChannel>> {
    @Inject
    public NewsLocalInteractImpl() {
    }

    @Override
    public Subscription loadLocalNews(final RequestCallBack<List<NewsChannel>> callBack) {
        callBack.beforeRequest();
        return Observable.create(new Observable.OnSubscribe<List<NewsChannel>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannel>> subscriber) {
                NewsChannelManager.initNewsChannels();
                subscriber.onNext(NewsChannelManager.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        }).compose(RxJavaUtils.<List<NewsChannel>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannel>>() {
                    @Override
                    public void onCompleted() {
                        callBack.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.DATABASE_ERROR,App.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannel> newsChannels) {
                        callBack.success(newsChannels);
                    }
                });
    }
}
