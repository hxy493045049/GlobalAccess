package com.msy.globalaccess.business.travelAgency.team.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.TeamTransportVehicleAdapter;
import com.msy.globalaccess.data.bean.team.TeamTransportVehicleBean;
import com.msy.globalaccess.listener.SetTeamDetailVehicleInfoListener;
import com.msy.globalaccess.widget.customView.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by pepys on 2017/2/7.
 * description: 团队详情-运输车辆
 */
public class TeamTransportVehicleFragment extends BaseFragment implements SetTeamDetailVehicleInfoListener {

    @BindView(R.id.transport_vehicle_recycleView)
    RecyclerView transport_vehicle_recycleView;
    /**
     * 用于切换tab的时候重新计算当前Fragment的高度
     */
    private CustomViewPager vp;
    /**
     * 车辆信息adapter
     */
    private TeamTransportVehicleAdapter vehicleAdapter;
    /**
     * 车辆信息数据源
     */
    private ArrayList<TeamTransportVehicleBean> vehicleList = new ArrayList<>();
    /**
     * 无数据页面
     */
    private View notDataView;


    public TeamTransportVehicleFragment() {
    }

    public void setVp(CustomViewPager vp) {
        this.vp = vp;
    }

    public static TeamTransportVehicleFragment newInstance() {
        TeamTransportVehicleFragment fragment = new TeamTransportVehicleFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tem_transport_vehicle_info;
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
        vp.setObjectForPosition(view, 2);
        initData();
    }

    private void initData() {
        vehicleAdapter = new TeamTransportVehicleAdapter(R.layout.fragment_tem_transport_vehicle_info_item, vehicleList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        transport_vehicle_recycleView.setLayoutManager(linearLayoutManager);
        transport_vehicle_recycleView.setHasFixedSize(true);
        transport_vehicle_recycleView.setNestedScrollingEnabled(false);
        transport_vehicle_recycleView.setAdapter(vehicleAdapter);
        notDataView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_look_view, (ViewGroup) transport_vehicle_recycleView.getParent(), false);
        vehicleAdapter.setEmptyView(notDataView);
    }

    @Override
    public void setTeamDetailVehicleInfoListener(ArrayList<TeamTransportVehicleBean> teamVehicleList) {
        vehicleList.clear();
        vehicleList.addAll(teamVehicleList);
        if(vehicleAdapter != null){
            vehicleAdapter.setNewData(vehicleList);
        }
    }
}
