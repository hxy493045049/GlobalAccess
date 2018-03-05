/*
 * Copyright (c) 2016 shawn <shawn0729@foxmail.com>
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
package com.msy.globalaccess.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msy.globalaccess.di.component.DaggerFragmentComponent;
import com.msy.globalaccess.di.component.FragmentComponent;
import com.msy.globalaccess.di.module.FragmentModule;
import com.msy.globalaccess.utils.RxJavaUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * @author shawn
 * @version 1.0 2017/1/17
 */
public abstract class BaseFragment extends Fragment {
    protected FragmentComponent mFragmentComponent;
    protected BaseContract.Presenter superPresenter;
    protected boolean isVisible;//fragment是否可见
    //使用rxbux一定要注意讲subscription添加到cache中,否则会内存泄漏
    protected List<Subscription> rxBusCache = new LinkedList<>();
    protected View mFragmentView;

    public FragmentComponent getFragmentComponent() {
        return mFragmentComponent;
    }

    public abstract void initInjector();

    public abstract void init(View view);

    public abstract int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(App.getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();

        initInjector();
    }

    //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mFragmentView);
            init(mFragmentView);
        } else {
            ViewGroup parent = (ViewGroup) mFragmentView.getParent();
            if (null != parent) {
                parent.removeView(mFragmentView);
            }
        }
        return mFragmentView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rxBusCache != null && rxBusCache.size() > 0) {
            for (Subscription subscription : rxBusCache) {
                RxJavaUtils.cancelSubscription(subscription);
            }
        }

        //        RefWatcher refWatcher = App.getRefWatcher();
        //        refWatcher.watch(this);
        superPresenter = getBasePresenter();
        if (superPresenter != null) {
            superPresenter.onDestroy();
        }
    }

    public abstract BaseContract.Presenter getBasePresenter();

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
}
