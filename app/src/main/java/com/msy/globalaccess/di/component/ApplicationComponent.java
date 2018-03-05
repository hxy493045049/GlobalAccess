package com.msy.globalaccess.di.component;

import android.content.Context;

import com.msy.globalaccess.di.module.ApplicationModule;
import com.msy.globalaccess.di.module.NetModule;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.di.scope.PerApp;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by hxy on 2016/12/6 0006.
 * <p>
 * description :
 */
@PerApp
@Component(modules = {ApplicationModule.class, NetModule.class})
public interface ApplicationComponent {

    @ContextLife("Application")
    Context getApplication();

    Retrofit getRetrofit();
}
