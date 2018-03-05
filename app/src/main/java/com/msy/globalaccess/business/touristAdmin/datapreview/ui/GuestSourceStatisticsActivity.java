package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.BaseFragmentsAdapter;
import com.msy.globalaccess.listener.IUpdateable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description : 更多客源地统计
 */

public class GuestSourceStatisticsActivity extends BaseActivity implements IUpdateable {


    @BindView(R.id.settlement_vpTeam)
    ViewPager vp;
    @BindView(R.id.settlement_tabTeam)
    TabLayout tab;
    private List<String> items = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .guestSourceStatisticsItem)));

    private ArrayList<BaseFragment> fragments = new ArrayList<>();
    private MyAdapter mAdapter;

    public static void callActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, GuestSourceStatisticsActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guest_source;
    }

    @Override
    public void initInjector() {

    }

    @Override
    protected void init() {
        fragments.add(GuestSourceStatisticsFragment.newInstance(GuestSourceStatisticsFragment.ALL_STATISTICS));
        fragments.add(GuestSourceStatisticsFragment.newInstance(GuestSourceStatisticsFragment.INBOUND_STATISTICS));
        fragments.add(GuestSourceStatisticsFragment.newInstance(GuestSourceStatisticsFragment.OUTBOUND_STATISTICS));
        mAdapter = new MyAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(mAdapter);
        tab.setupWithViewPager(vp);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                BaseFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null && fragment instanceof IUpdateable) {
                    ((IUpdateable) fragment).update();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                BaseFragment fragment = fragments.get(tab.getPosition());
                if (fragment != null && fragment instanceof IUpdateable) {
                    ((IUpdateable) fragment).update();
                }
            }
        });
        setTitle(App.getResourceString(R.string.guestSourceTitle));
    }

    @Override
    public void update() {
        BaseFragment fragment = fragments.get(vp.getCurrentItem());
        if (fragment != null && fragment instanceof IUpdateable) {
            ((IUpdateable) fragment).update();
        }
    }


    private class MyAdapter extends BaseFragmentsAdapter<BaseFragment> {

        MyAdapter(FragmentManager manager, List<BaseFragment> fragmentList) {
            super(manager, fragmentList);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items.get(position);
        }
    }
}
