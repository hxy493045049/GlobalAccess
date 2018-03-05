package com.msy.globalaccess.data.interactor.impl;


import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.KeyMapApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.data.bean.base.MapBeanWrapper;
import com.msy.globalaccess.data.interactor.IKeyMapInteractor;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by shawn on 2017/3/28 0028.
 * <p>
 * description :
 */

public class KeyMapInteractorImpl implements IKeyMapInteractor<KeyMapBean> {
    private KeyMapApi mapApi;

    @Inject
    public KeyMapInteractorImpl(Retrofit retrofit) {
        mapApi = retrofit.create(KeyMapApi.class);
    }

    @Override
    public Subscription getDataByType(@NotNull RequestCallBack<List<KeyMapBean>> callBack, @NotNull HashMap<String,
            String> params) {
        callBack.beforeRequest();
        return mapApi.getMapByKey(NetUtil.getCacheControl(), params)
                .compose(RxJavaUtils.<BaseBean<MapBeanWrapper<KeyMapBean>>>defaultSchedulers())
                .flatMap(RxJavaUtils.<MapBeanWrapper<KeyMapBean>>defaultBaseFlatMap())
                .map(new Func1<BaseBean<MapBeanWrapper<KeyMapBean>>, List<KeyMapBean>>() {
                    @Override
                    public List<KeyMapBean> call(BaseBean<MapBeanWrapper<KeyMapBean>> base) {
                        return base.getData().getPubmapList();
                    }
                })
                .subscribe(new DefaultSubscriber<>(callBack));
    }
}
