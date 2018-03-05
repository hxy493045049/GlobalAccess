package com.msy.globalaccess.di.module;

import android.content.Context;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.di.qualifier.ContextLife;
import com.msy.globalaccess.di.scope.PerApp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hxy on 2016/12/7 0007.
 * <p>
 * description :
 */
@Module
public class ApplicationModule {

    private Context mApplicationContext;

    public ApplicationModule(App context) {
        mApplicationContext = context.getApplicationContext();
    }

    @Provides
    @PerApp
    @ContextLife
    Context provideApplicationContext() {
        return mApplicationContext;
    }

}
