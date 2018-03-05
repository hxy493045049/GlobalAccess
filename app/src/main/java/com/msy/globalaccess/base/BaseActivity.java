package com.msy.globalaccess.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.msy.globalaccess.R;
import com.msy.globalaccess.annotation.BindValues;
import com.msy.globalaccess.business.navigation.NavigationActivity;
import com.msy.globalaccess.business.navigation.StartupActivity;
import com.msy.globalaccess.di.component.ActivityComponent;
import com.msy.globalaccess.di.component.DaggerActivityComponent;
import com.msy.globalaccess.di.module.ActivityModule;
import com.msy.globalaccess.utils.MyUtils;
import com.msy.globalaccess.utils.NetUtil;
import com.msy.globalaccess.utils.RxJavaUtils;
import com.msy.globalaccess.utils.ToastUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Created by hxy on 2016/12/7 0007.
 * <p>
 * description :
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Handler mainHandler;
    protected ActivityComponent mActivityComponent;
    protected BaseContract.Presenter basePresenter;
    protected String TAG = BaseActivity.class.getSimpleName();
    //使用rxbux一定要注意讲subscription添加到cache中,否则会内存泄漏
    protected List<Subscription> rxBusCache = new LinkedList<>();
    private boolean mIsHasNavigationView;
    private DrawerLayout mDrawerLayout;
    private boolean exit;
    private ActionBarDrawerToggle toggle;
    private
    @ColorInt
    int defaultStatusColor = R.color.colorPrimary;

    protected abstract int getLayoutId();//获取布局id

    protected abstract void initInjector();//注入对象,attach契约view,设置父类的presenter

    protected abstract void init();//初始化

    protected int getStatusColor() {
        return defaultStatusColor;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((!(this instanceof StartupActivity)) && App.allActivity != null && App.allActivity.size() == 0 &&
                savedInstanceState != null) {
            StartupActivity.callActivity(this);
            finish();
            return;
        }
        TAG = this.getClass().getSimpleName();
        initAnnotation();
        NetUtil.isNetworkErrThenShowMsg();
        initActivityComponent();
        setStatusBarTranslucent();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initKeyboard();

        initInjector();
        initToolBar();
        init();
        setTitle("");
        if (mIsHasNavigationView) {
            initDrawerLayout();
        }

        if (basePresenter != null) {
            basePresenter.onStart();
        }
        App.allActivity.add(this);
    }


    private void initKeyboard() {
        //点击空白处隐藏键盘
        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    public void onBackPressed() {
        if (mIsHasNavigationView && mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (this instanceof NavigationActivity) {
            if (!exit) {
                if (mainHandler == null) {
                    mainHandler = new MainHandler(this);
                }
                mainHandler.sendEmptyMessageDelayed(MainHandler.EXIT_APP, 2000);
                ToastUtils.showToast(2000, R.string.exit);
                exit = true;
            } else {
                exit();
            }
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_in_x, R.anim.right_out_x);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (toggle != null) {
            toggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (toggle != null) {
            toggle.syncState();
        }
    }


    //退出
    private void exit() {
        finish();
        MobclickAgent.onKillProcess(App.getAppContext());
        //        System.exit(0);
        // TODO: 2017/1/23 0023
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsHasNavigationView) {
            //            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null && mDrawerLayout != null) {
            toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        overridePendingTransition(R.anim.left_in_x, R.anim.right_out_x);
    }

    //toolbar支持
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayShowTitleEnabled(false);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            AppCompatTextView tvToolbarCenter = (AppCompatTextView) toolbar.findViewById(R.id.tvToolbarCenter);
            tvToolbarCenter.setVisibility(View.VISIBLE);
            tvToolbarCenter.setText(title);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in_x, R.anim.right_out_x);
        App.allActivity.remove(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rxBusCache != null && rxBusCache.size() > 0) {
            for (Subscription subscription : rxBusCache) {
                RxJavaUtils.cancelSubscription(subscription);
            }
        }
        //        RefWatcher refWatcher = App.getRefWatcher();
        //        refWatcher.watch(this);
        if (basePresenter != null) {
            basePresenter.onDestroy();
        }
        MyUtils.fixInputMethodManagerLeak(this);
        App.containsActivity(this);
    }

    //初始化activitycomponent注射器
    private void initActivityComponent() {
        mActivityComponent = DaggerActivityComponent.builder().applicationComponent(App
                .getApplicationComponent()).activityModule(new ActivityModule(this)).build();
    }

    //获取子类的标识,判断是否需要显示navigation(侧滑栏)
    private void initAnnotation() {
        if (this.getClass().isAnnotationPresent(BindValues.class)) {
            BindValues annotation = this.getClass().getAnnotation(BindValues.class);
            mIsHasNavigationView = annotation.mIsHasNavigationView();
        }
    }

    //适配4.4及以上的沉浸式
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(getStatusColor());
        }
    }

    public static class MainHandler extends Handler {
        private static final int EXIT_APP = 0x001;
        private WeakReference<BaseActivity> reference;

        MainHandler(BaseActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case EXIT_APP:
                        activity.exit = false;
                        break;
                }
            }
        }
    }

}
