/*
 * Copyright (c) 2016 shawn <kaku201313@163.com>
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
package com.msy.globalaccess.utils.rxhelper;

import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.exception.RxException;
import com.msy.globalaccess.utils.ErrorUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author shawn
 * @version 1.0 2016/8/20
 */
public class RxJavaUtils {

    // 对API调用了observeOn(MainThread)之后，线程会跑在主线程上，包括onComplete也是，
    // unsubscribe也在主线程，然后如果这时候调用call.cancel会导致NetworkOnMainThreadException
    // 加一句unsubscribeOn(io)
    public static <T> Observable.Transformer<T, T> defaultSchedulers() {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Func1<BaseBean<T>, Observable<BaseBean<T>>> defaultBaseFlatMap() {
        return new Func1<BaseBean<T>, Observable<BaseBean<T>>>() {
            @Override
            public Observable<BaseBean<T>> call(BaseBean<T> base) {
                //异常处理
                if (base.getStatus() == ResultCode.SUCCESS || base.getStatus() == ResultCode.SUCCESS_NO_MORE_DATA ||
                        base.getStatus() == ResultCode.SUCCESS_EMPTY || base.getStatus() == ResultCode.SEARCH_NO_DATA) {
                    return Observable.just(base);
                } else {
                    String errorMessage = ErrorUtils.getErrorMessage(base, R.string.internet_error);
                    return Observable.error(new RxException(base.getStatus(), errorMessage));
                }
            }
        };
    }

    public static Func1<NoDataBean, Observable<NoDataBean>> defaultNoDataFlatMap() {
        return new Func1<NoDataBean, Observable<NoDataBean>>() {
            @Override
            public Observable<NoDataBean> call(NoDataBean base) {
                //异常处理
                if (base.getStatus() == ResultCode.SUCCESS || base.getStatus() == ResultCode.SUCCESS_NO_MORE_DATA ||
                        base.getStatus() == ResultCode.SUCCESS_EMPTY || base.getStatus() == ResultCode.SEARCH_NO_DATA) {
                    return Observable.just(base);
                } else {
                    String errorMessage = ErrorUtils.getErrorMessage(base, R.string.internet_error);
                    return Observable.error(new RxException(base.getStatus(), errorMessage));
                }
            }
        };
    }
}
