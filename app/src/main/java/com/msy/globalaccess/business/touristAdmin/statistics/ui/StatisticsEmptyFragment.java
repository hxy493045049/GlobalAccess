package com.msy.globalaccess.business.touristAdmin.statistics.ui;

import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.listener.IUpdateable;

import javax.inject.Inject;

/**
 * Created by pepys on 2017/5/24.
 * description:
 */
@Deprecated
public class StatisticsEmptyFragment extends BaseFragment implements IUpdateable {

    @Inject
    public StatisticsEmptyFragment(){

    }


    @Override
    public void update() {

    }

    @Override
    public void initInjector() {

    }

    @Override
    public void init(View view) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistics_empty;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }
}
