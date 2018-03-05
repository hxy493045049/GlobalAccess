package com.msy.globalaccess.business.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msy.globalaccess.base.BaseFragment;

import java.util.List;

/**
 * Created by shawn on 2017/5/22 0022.
 * <p>
 * description :
 */

public class TourismViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;

    public TourismViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
