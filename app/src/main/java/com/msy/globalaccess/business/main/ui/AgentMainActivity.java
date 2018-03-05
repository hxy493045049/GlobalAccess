package com.msy.globalaccess.business.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.annotation.BindValues;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.travelAgency.delegate.ui.TouristDelegateFragment;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchDelegateGuiderActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.business.travelAgency.setting.PersonalCenterFragment;
import com.msy.globalaccess.business.travelAgency.settlement.ui.SettlementFragment;
import com.msy.globalaccess.business.travelAgency.team.ui.TeamFragment;
import com.msy.globalaccess.listener.AppbarInteractor;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description : 旅行社
 */
@BindValues
public class AgentMainActivity extends BaseActivity implements AppbarInteractor {
    @BindView(R.id.appbarMain)
    AppBarLayout appbarMain;

    @BindView(R.id.imgBtnBack)
    AppCompatImageButton imgBtnBack;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarTitle;

    @Inject
    TeamFragment teamFragment;//团队管理界面

    @Inject
    TouristDelegateFragment touristDelegateFragment;//导游委派

    @Inject
    SettlementFragment settlementFragment;//结算管理界面

    @Inject
    PersonalCenterFragment mPersonalCenterFragment;//系统设置界面

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ivToolbarRight)
    AppCompatImageView ivToolbarRight;

    private FragmentManager mManager;
    private BaseFragment mCurrentFragment;
    private List<String> title;

    public static void callActivity(Context ctx) {
        Intent intent = new Intent(ctx, AgentMainActivity.class);
        ctx.startActivity(intent);
    }

    public static void callActivityClearTop(Context ctx) {
        Intent intent = new Intent(ctx, AgentMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mManager = getSupportFragmentManager();
            mManager.beginTransaction().add(R.id.flContent, teamFragment).commitAllowingStateLoss();
            mCurrentFragment = teamFragment;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected void init() {
        title = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.titleDescription));
        mToolbar.setNavigationIcon(R.mipmap.icon_home);

        ivToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(title.get(0));
        initListener();
    }

    private void initListener() {
        //        imgBtnBack.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                HashMap<String, Object> hashMap = new HashMap<>();
        //                hashMap.put(TeamServiceApi.TeamListApi.optionSearchType, "0");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTeamCode, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTeamStatus, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTravelAgentId, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTravelDepId, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTeamStartDate, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTeamEndDate, "");
        //                hashMap.put(TeamServiceApi.TeamListApi.optionTeamTypeId, "");
        //                SearchResultActivity.callActivity(AgentMainActivity.this, SearchResultActivity.TEAM_SEARCH,
        // hashMap);
        //                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
        //            }
        //        });
        ivToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment instanceof SettlementFragment) {
                    SearchActivity.callActivity(AgentMainActivity.this, SearchResultActivity.SETTLEMENT_SEARCH);
                    overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                } else if (mCurrentFragment instanceof TeamFragment) {
                    SearchActivity.callActivity(AgentMainActivity.this, SearchResultActivity.TEAM_SEARCH);
                    overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                } else if (mCurrentFragment instanceof TouristDelegateFragment) {
                    SearchDelegateGuiderActivity.callActivity(AgentMainActivity.this);
                    overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                }
            }
        });
    }

    @OnClick({R.id.tvTeam, R.id.tourismDelegate, R.id.tvSettment, R.id.tvSystemSetting})
    public void onClickTable(View view) {
        switch (view.getId()) {
            case R.id.tvTeam:   // 团队管理
                showCurrentFragment(teamFragment);
                teamFragment.update();
                switchToolbar();
                appbarMain.setExpanded(true, false);
                changeAppbarScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP, AppBarLayout.LayoutParams
                        .SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED, AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                break;
            case R.id.tourismDelegate:  //导游委派
                showCurrentFragment(touristDelegateFragment);
                touristDelegateFragment.update();
                switchToolbar();
                appbarMain.setExpanded(true, false);
                changeAppbarScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP, AppBarLayout.LayoutParams
                        .SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED, AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);

                break;
            case R.id.tvSettment:   // 结算管理
                showCurrentFragment(settlementFragment);
                settlementFragment.update();
                switchToolbar();
                appbarMain.setExpanded(true, false);
                changeAppbarScrollFlag(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP, AppBarLayout.LayoutParams
                        .SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED, AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
                break;
            case R.id.tvSystemSetting:  //个人中心
                showCurrentFragment(mPersonalCenterFragment);
                switchToolbar();
                appbarMain.setExpanded(true, false);
                changeAppbarScrollFlag(0);
                break;
        }
    }


    private void switchToolbar() {
        if (mCurrentFragment instanceof TeamFragment) {//团队管理页面
            tvToolbarTitle.setText(title.get(0));//标题
            ivToolbarRight.setVisibility(View.VISIBLE);
        } else if (mCurrentFragment instanceof TouristDelegateFragment) {//导游委派
            tvToolbarTitle.setText(title.get(1));//标题
            ivToolbarRight.setVisibility(View.VISIBLE);
        } else if (mCurrentFragment instanceof SettlementFragment) {//结算页面
            tvToolbarTitle.setText(title.get(2));//标题
            ivToolbarRight.setVisibility(View.VISIBLE);
        } else if (mCurrentFragment instanceof PersonalCenterFragment) {
            //用户设置页面
            tvToolbarTitle.setText(title.get(3));
            ivToolbarRight.setVisibility(View.GONE);
        }
    }

    //判断是否从右到左的动画
    private boolean isRightInAnim(BaseFragment showFragment) {
        List<Fragment> fragments = mManager.getFragments();

        if (fragments.indexOf(showFragment) == -1) {
            return true;
        } else if (fragments.indexOf(mCurrentFragment) < fragments.indexOf(showFragment)) {
            return true;
        } else {
            return false;
        }

    }

    private void setAnim(FragmentTransaction transaction, BaseFragment showFragment) {
        if (isRightInAnim(showFragment)) {
            transaction.setCustomAnimations(R.anim.right_in_x, R.anim.left_out_x);
        } else {
            transaction.setCustomAnimations(R.anim.left_in_x, R.anim.right_out_x);
        }
    }

    /**
     * 显示对应的Fragment
     */
    private void showCurrentFragment(BaseFragment showFragment) {
        if (mCurrentFragment != showFragment) {
            FragmentTransaction transaction = mManager.beginTransaction();
            setAnim(transaction, showFragment);

            if (!showFragment.isAdded()) {
                transaction.hide(mCurrentFragment).add(R.id.flContent, showFragment);
            } else {
                transaction.hide(mCurrentFragment).show(showFragment);
            }

            transaction.commitAllowingStateLoss();
            mCurrentFragment = showFragment;
        }
    }

    /**
     * 变更首页appbar的滑动特性,0表示不能滚动,
     * 其他属性请参考{@link AppBarLayout.LayoutParams.ScrollFlags}
     */
    private void changeAppbarScrollFlag(int... flags) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        if (params != null) {
            int flagResult = 0;
            for (int flag : flags) {
                flagResult = flagResult | flag;
            }
            params.setScrollFlags(flagResult);
            mToolbar.setLayoutParams(params);
        }
    }

    @Override
    public void changeAppbar(boolean expanded, boolean anim) {
        appbarMain.setExpanded(expanded, anim);
    }

}
