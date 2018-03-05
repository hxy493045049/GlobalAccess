package com.msy.globalaccess.utils;


import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by hxy on 2016/12/12 0012.
 * <p>
 * description :
 */

public class RxBus {

    private final Subject<Object, Object> mBus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    // 单例RxBus
    public static RxBus getInstance() {
        return Holder.instance;
    }
    // 发送一个新的事件
    public void post(Object o) {
        mBus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType).compose(RxJavaUtils.<T>defaultSchedulers());
    }

    private static class Holder {
        final static RxBus instance = new RxBus();
    }
}
