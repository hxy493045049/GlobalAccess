package com.msy.globalaccess.business.touristAdmin.statistics.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.travelAgency.team.ui.SubTeamFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 团队信息查询  阉割版,viewpager中只有一个subteamfragment,只显示所有类型
 * Created by chensh on 2017/1/20 0020.
 */
public class TeamStatisticsFragment extends BaseFragment {

    public static final String TEAM_DATA_TYPE_ALL = "";

    @BindView(R.id.tabTeam)
    TabLayout tabTeam;
    @BindView(R.id.vpTeam)
    ViewPager mViewPager;
    private List<String> module;
    private List<SubTeamFragment> fragments = new ArrayList<>();

    @Inject
    public TeamStatisticsFragment() {
        module = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.teamModule));

        String dataType = TEAM_DATA_TYPE_ALL;//默认

        SubTeamFragment fragment = SubTeamFragment.newInstance(dataType);
        fragments.add(fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mViewPager.setAdapter(new MyViewPagerAdapter(getActivity().getSupportFragmentManager()));
        tabTeam.setVisibility(View.GONE);
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
