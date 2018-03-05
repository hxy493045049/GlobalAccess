package com.msy.globalaccess.business.demo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.demo.contract.NewsContract;
import com.msy.globalaccess.business.demo.contract.impl.NewPresenterImpl;
import com.msy.globalaccess.data.bean.NewsChannel;
import com.msy.globalaccess.event.ChannelChangeEvent;
import com.msy.globalaccess.utils.RxBus;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by hxy on 2016/12/7 0007.
 * <p>
 * description :  这个页面仅仅是个demo
 */
@Deprecated
public class NewsActivity extends BaseActivity implements NewsContract.View {
    @Inject
    NewPresenterImpl mNewsPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        mNewsPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = mNewsPresenter;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //类似于eventbus
        RxBus.getInstance().toObservable(ChannelChangeEvent.class)
                .subscribe(new Action1<ChannelChangeEvent>() {
                    @Override
                    public void
                    call(ChannelChangeEvent channelChangeEvent) {
                        mNewsPresenter.onChannelDbChanged();
                    }
                });
    }

    @Override
    public void initViewPager(List<NewsChannel> newsChannels) {

    }

    @Override
    public void showProgress() {

    }


    @Override
    public void hideProgress() {

    }


}
