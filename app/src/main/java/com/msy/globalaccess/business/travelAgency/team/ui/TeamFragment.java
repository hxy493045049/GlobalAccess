package com.msy.globalaccess.business.travelAgency.team.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.event.RefreshTeamBadgeEvent;
import com.msy.globalaccess.event.TeamBadgeInfo;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.widget.BadgeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 团队管理
 * Created by chensh on 2017/1/20 0020.
 */

public class TeamFragment extends BaseFragment {

    public static final String TEAM_DATA_TYPE_ALL = "";
    public static final String TEAM_DATA_TYPE_GROUP = "0";
    public static final String TEAM_DATA_TYPE_CHANGE = "1";
    public static final String TEAM_DATA_TYPE_REFUND = "2";

    @BindView(R.id.tabTeam)
    TabLayout tabTeam;
    @BindView(R.id.vpTeam)
    ViewPager mViewPager;
    private List<String> module;
    private List<BadgeView> badgeCache = new ArrayList<>();
    private List<SubTeamFragment> fragments = new ArrayList<>();

    @Inject
    public TeamFragment() {
        module = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.teamModule));

        String dataType = TEAM_DATA_TYPE_ALL;//默认
        for (int i = 0; i < module.size(); i++) {
            switch (i) {
                case 1:
                    dataType = TEAM_DATA_TYPE_GROUP;//出团待审批
                    break;
                case 2:
                    dataType = TEAM_DATA_TYPE_CHANGE;//变更待审批
                    break;
                case 3:
                    dataType = TEAM_DATA_TYPE_REFUND;//作废待审批
                    break;
            }
            SubTeamFragment fragment = SubTeamFragment.newInstance(dataType);
            fragments.add(fragment);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observerBadgeInit();//监听小圆点的初始化
        observerBadgeUpdate();//监听小圆点的更新删除
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    @Override
    public void initInjector() {

    }

    @Override
    public void init(View view) {
        mViewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
        tabTeam.setupWithViewPager(mViewPager);
        for (int i = 1; i < tabTeam.getTabCount(); i++) {
            View v = LayoutInflater.from(mFragmentComponent.getActivityContext()).inflate(R.layout.item_tab_badge,
                    tabTeam, false);
            BadgeView badge = new BadgeView(getActivity());
            badge.setTargetView(v.findViewById(android.R.id.text1));
            tabTeam.getTabAt(i).setCustomView(v);
            badgeCache.add(badge);
        }
        tabTeam.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SubTeamFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SubTeamFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team;
    }

    public void update() {
        if (mViewPager == null) {
            return;
        }
        SubTeamFragment fragment = fragments.get(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onRefresh();
        }
    }

    private void observerBadgeInit() {
        Subscription subscription = RxBus.getInstance().toObservable(TeamBadgeInfo.class)
                .subscribe(new Action1<TeamBadgeInfo>() {
                    @Override
                    public void call(TeamBadgeInfo teamBadgeInfo) {
                        initBadge(teamBadgeInfo);
                    }
                });
        rxBusCache.add(subscription);
    }

    private void observerBadgeUpdate() {
        Subscription subscription = RxBus.getInstance().toObservable(RefreshTeamBadgeEvent.class)
                .subscribe(new Action1<RefreshTeamBadgeEvent>() {
                    @Override
                    public void call(RefreshTeamBadgeEvent teamBadgeInfo) {
                        switch (teamBadgeInfo.getType()) {
                            case TEAM_DATA_TYPE_ALL:
                                switch (teamBadgeInfo.getOperType()) {
                                    case "0":
                                        badgeCache.get(0).minus();
                                        break;
                                    case "2":
                                        badgeCache.get(1).minus();
                                        break;
                                    case "1":
                                        if (teamBadgeInfo.getChange() == 1) {
                                            badgeCache.get(2).plus();
                                        } else if (teamBadgeInfo.getChange() == 0) {
                                            badgeCache.get(2).minus();
                                        }
                                        break;
                                }
                                break;
                            case TEAM_DATA_TYPE_GROUP:
                                badgeCache.get(0).minus();
                                break;
                            case TEAM_DATA_TYPE_CHANGE:
                                badgeCache.get(1).minus();
                                break;
                            case TEAM_DATA_TYPE_REFUND:
                                if (teamBadgeInfo.getChange() == 1) {
                                    badgeCache.get(2).plus();
                                } else if (teamBadgeInfo.getChange() == 0) {
                                    badgeCache.get(2).minus();
                                }
                                break;
                        }
                    }
                });
        rxBusCache.add(subscription);
    }

    private void initBadge(TeamBadgeInfo teamBadgeInfo) {
        badgeCache.get(0).setBadgeCount(teamBadgeInfo.getGroupNum());
        badgeCache.get(1).setBadgeCount(teamBadgeInfo.getChangeNum());
        badgeCache.get(2).setBadgeCount(teamBadgeInfo.getCancelNum());
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return module.get(position);//页卡标题
        }
    }
}
