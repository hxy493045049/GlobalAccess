/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.msy.globalaccess.data.interactor.impl;


import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.NewsService;
import com.msy.globalaccess.data.bean.NewsSummary;
import com.msy.globalaccess.data.interactor.NewsRemoteInteractor;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.msy.zc.commonutils.TimeFormat;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by hxy on 2016/12/13 0013.
 * <p>
 * description : it's just a demo for remote
 */
@Deprecated
public class NewsRemoteInteractorImpl implements NewsRemoteInteractor<List<NewsSummary>> {

    private Retrofit mRetrofit;

    @Inject
    public NewsRemoteInteractorImpl(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    @Override
    public Subscription loadRemoteNews(final RequestCallBack<List<NewsSummary>> listener, String type,
                                       final String id, int startPage) {
        listener.beforeRequest();
        NewsService service = mRetrofit.create(NewsService.class);
        return service.getNewsList(NetUtil.getCacheControl(), type, id, startPage)
                .flatMap(new Func1<Map<String, List<NewsSummary>>, Observable<NewsSummary>>() {
                    @Override
                    public Observable<NewsSummary> call(Map<String, List<NewsSummary>> map) {
                        if (id.endsWith("111")) {
                            return Observable.from(map.get("北京"));
                        }
                        return Observable.from(map.get(id));
                    }
                })
                .map(new Func1<NewsSummary, NewsSummary>() {
                    @Override
                    public NewsSummary call(NewsSummary newsSummary) {
                        String ptime = TimeFormat.transformData(newsSummary.getPtime(),"yyyy-MM-dd HH:mm:ss","MM-dd HH:mm");
                        newsSummary.setPtime(ptime);
                        return newsSummary;
                    }
                })
                .distinct()
                .toSortedList(new Func2<NewsSummary, NewsSummary, Integer>() {
                    @Override
                    public Integer call(NewsSummary newsSummary, NewsSummary newsSummary2) {
                        return newsSummary2.getPtime().compareTo(newsSummary.getPtime());
                    }
                })
                .compose(RxJavaUtils.<List<NewsSummary>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsSummary>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d("");
                        listener.after();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e,"");
                        listener.onError( ResultCode.NET_ERROR,"加载失败");
                    }

                    @Override
                    public void onNext(List<NewsSummary> newsSummaries) {
                        Logger.d("");
                        listener.success(newsSummaries);
                    }
                });

    }

}
