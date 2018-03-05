package com.msy.globalaccess.business.travelAgency.delegate.ui;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;



/**
 * Created by pepys on 2017/7/3
 * description:
 *
 */
public class TouristDelegateFragment extends BaseFragment{

    public static final String TEAM_DATA_TYPE_ALL = "";
    public static final String TEAM_DATA_TYPE_PROCRSSING = "0";
    public static final String TEAM_DATA_TYPE_UNPROCRSS = "1";
    public static final String TEAM_DATA_TYPE_PROCRSSED = "2";

    @BindView(R.id.settlement_tabTeam)
    TabLayout tabTeam;

    @BindView(R.id.settlement_vpTeam)
    ViewPager mViewPager;

    /**
     * 列表名称
     */
    private List<String> module;
    /**
     *
     */
    private ArrayList<SubTouristDelegateFragment> fragments = new ArrayList<>();

    @Inject
    public TouristDelegateFragment() {
        module = Arrays.asList(App.getAppContext().getResources().getStringArray(R.array.touristDelegate));

        String dataType = TEAM_DATA_TYPE_ALL;//默认

        for (int i = 0; i < module.size(); i++) {
            switch (i) {
                case 1:
                    dataType = TEAM_DATA_TYPE_PROCRSSING;//处理中
                    break;
                case 2:
                    dataType = TEAM_DATA_TYPE_PROCRSSED;//已通过
                    break;
                case 3:
                    dataType = TEAM_DATA_TYPE_UNPROCRSS;//未通过
                    break;
            }
            SubTouristDelegateFragment fragment = SubTouristDelegateFragment.newInstance(dataType);
            fragments.add(fragment);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_delegate_tourist;
    }

    @Override
    public void initInjector() {
    }

    @Override
    public void init(View view) {
        mViewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
        tabTeam.setupWithViewPager(mViewPager);
        tabTeam.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SubTouristDelegateFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SubTouristDelegateFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null) {
                    fragment.onRefresh();
                }
            }
        });
        tabTeam.getTabAt(1).select();
    }


    public void update() {
        if (mViewPager == null) {
            return;
        }
        SubTouristDelegateFragment fragment = fragments.get(mViewPager.getCurrentItem());
        if (fragment != null) {
            fragment.onRefresh();
        }
        tabTeam.getTabAt(1).select();
    }



    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
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
