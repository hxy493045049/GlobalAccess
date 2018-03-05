package com.msy.globalaccess.business.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.msy.globalaccess.base.BaseFragment;

import java.util.List;

/**
 * Created by shawn on 2017/7/7 0007.
 * <p>
 * description :
 */

public class BaseFragmentsAdapter<T extends BaseFragment> extends FragmentPagerAdapter {
    private List<T> fragmentList;

    public BaseFragmentsAdapter(FragmentManager manager, List<T> fragmentList) {
        super(manager);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
