package com.msy.globalaccess.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;

import com.liulishuo.filedownloader.FileDownloader;
import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.common.enums.EnvironmentType;
import com.msy.globalaccess.config.DBSetting;
import com.msy.globalaccess.data.db.DaoMaster;
import com.msy.globalaccess.data.db.DaoSession;
import com.msy.globalaccess.data.db.GreenDbHelper;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.di.component.ApplicationComponent;
import com.msy.globalaccess.di.component.DaggerApplicationComponent;
import com.msy.globalaccess.di.module.ApplicationModule;
import com.msy.globalaccess.di.module.NetModule;
import com.msy.globalaccess.utils.CrashHandler;
import com.msy.globalaccess.utils.ToastMgr;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;

/**
 * Created by hxy on 2016/12/6 0006.
 * <p>
 * description :
 */

public class App extends Application {
    public static UserHelper userHelper;
    public static ArrayList<Activity> allActivity = new ArrayList<>();// 保存Activity
    private static App sAppContext;
    private DaoSession daoSession;
    private RefWatcher refWatcher;
    private ApplicationComponent mApplicationComponent;

    public static RefWatcher getRefWatcher() {
        return sAppContext.refWatcher;
    }

    public static App getAppContext() {
        return sAppContext;
    }

    public static ApplicationComponent getApplicationComponent() {
        return sAppContext.mApplicationComponent;
    }

    public static String getResourceString(int stringId) {
        String str = "";
        try {
            str = sAppContext.getResources().getString(stringId);
        } catch (Resources.NotFoundException e) {
            Logger.e(e, "找不到指定资源", stringId);
        }
        return str;
    }

    public static Resources getResource() {
        return sAppContext.getResources();
    }

    public static DaoSession getDaoSession() {
        return sAppContext.daoSession;
    }

    // 通过name获取Activity对象
    public static Activity getActivityByName(String name) {
        Activity getac = null;
        for (Activity ac : allActivity) {
            if (ac.getClass().getName().contains(name)) {
                getac = ac;
            }
        }
        return getac;
    }

    public static ArrayList<Activity> getAllActivity() {
        return allActivity;
    }

    public static void containsActivity(Activity activity) {
        if (App.allActivity.contains(activity)) {
            App.allActivity.remove(activity);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.v("application --> onCreate ");
        sAppContext = this;
        initLog();
        initToastMgr();
        initUmeng();
        //        initLeakCanary();
        initActivityLifecycleLogs();
        //        initStrictMode();
        initDataBase();
        initApplicationComponent();
        userHelper = UserHelper.getInstance();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initFileDown();
    }
    private void initFileDown(){
        /**
         * 仅仅是缓存Application的Context，不耗时
         */
        FileDownloader.init(sAppContext);
    }
    private void initUmeng() {
        //友盟统计
        MobclickAgent.setDebugMode(BuildConfig.IS_DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    private void initApplicationComponent() {
        if (BuildConfig.ENVIRONMENT.equals("debug_test")) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new
                    ApplicationModule(this)).netModule(new NetModule(EnvironmentType.ENV_DEBUG)).build();
        } else if (BuildConfig.ENVIRONMENT.equals("qyb")) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new
                    ApplicationModule(this)).netModule(new NetModule(EnvironmentType.ENV_INTEGRATION)).build();
        } else if (BuildConfig.ENVIRONMENT.equals("qyt")) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new
                    ApplicationModule(this)).netModule(new NetModule(EnvironmentType.ENV_RELEASE)).build();
        }

    }

    /**
     * 初始化Toast
     */
    private void initToastMgr() {
        ToastMgr.ToastEnum.builder.init(getApplicationContext());
    }

    //开发环境采用严格模式
    private void initStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());

            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build());
        }
    }

    //初始化log配置
    private void initLog() {
        if (BuildConfig.DEBUG) {
            Logger.init("GlobalAccess").logLevel(LogLevel.FULL);
        } else {
            Logger.init("GlobalAccess").logLevel(LogLevel.NONE);
        }
    }

    //给全局的activity设置log信息
    private void initActivityLifecycleLogs() {
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  " +
                        "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Logger.v("activity name: " + activity.getComponentName().getClassName() + ",  onActivityDestroyed");
            }
        });
    }

    //配置内存追踪工具
    private void initLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            refWatcher = LeakCanary.install(this);
        } else {
            refWatcher = RefWatcher.DISABLED;
        }
    }

    private void initDataBase() {
        QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
        //        默认的实例是DevOpenHelper  但是DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        GreenDbHelper helper = new GreenDbHelper(this, DBSetting.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
