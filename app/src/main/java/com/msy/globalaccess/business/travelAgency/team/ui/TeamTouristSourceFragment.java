package com.msy.globalaccess.business.travelAgency.team.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.TeamTouristSourceAdapter;
import com.msy.globalaccess.data.bean.team.TeamMemberBean;
import com.msy.globalaccess.data.bean.team.TeamMemberSourceBean;
import com.msy.globalaccess.data.bean.tourism.TouristsInfoBean;
import com.msy.globalaccess.listener.SetTeamDetailSourceListener;
import com.msy.globalaccess.widget.customView.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pepys on 2017/2/7.
 * description: 团队详情-客源地
 */
public class TeamTouristSourceFragment extends BaseFragment implements SetTeamDetailSourceListener {
    @BindView(R.id.detail_look_tourist_recycler)
    RecyclerView detail_look_tourist_recycler;
    @BindView(R.id.detail_look_tourist_info)
    AppCompatButton detail_look_tourist_info;
    /**
     * 客源地信息adapter
     */
    private TeamTouristSourceAdapter touristSourceAdapter;
    /**
     * 数据源
     */
    private ArrayList<TeamMemberSourceBean> teamMemberSourceList = new ArrayList<>();
    /**
     * 游客的名单
     */
    private ArrayList<TeamMemberBean> teamMemberList;
    /**
     * 游客名单上面的信息
     */
    private TouristsInfoBean touristsInfo;

    private CustomViewPager vp;
    /**
     * 无数据界面
     */
    private View notDataView;

    public TeamTouristSourceFragment() {
    }

    public void setVp(CustomViewPager vp) {
        this.vp = vp;
    }

    public static TeamTouristSourceFragment newInstance() {
        TeamTouristSourceFragment fragment = new TeamTouristSourceFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void init(View view) {
        vp.setObjectForPosition(view, 1);
        initData();
    }

    private void initData() {
        touristSourceAdapter = new TeamTouristSourceAdapter(R.layout.fragment_team_tourist_source_item, teamMemberSourceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        detail_look_tourist_recycler.setLayoutManager(linearLayoutManager);
        detail_look_tourist_recycler.setHasFixedSize(true);
        detail_look_tourist_recycler.setNestedScrollingEnabled(false);
        detail_look_tourist_recycler.setAdapter(touristSourceAdapter);
        notDataView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_look_view, (ViewGroup) detail_look_tourist_recycler.getParent(), false);
        touristSourceAdapter.setEmptyView(notDataView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_tourist_source;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    @OnClick(R.id.detail_look_tourist_info)
    public void clickTouristInfo() {
        CheckTouristInfoActivity.callActivity(getActivity(), teamMemberList, touristsInfo, teamId);
    }

    @Override
    public void setTeamDetailSourceListener(ArrayList<TeamMemberSourceBean> teamMemberSourceList, ArrayList<TeamMemberBean> teamMemberList, TouristsInfoBean touristsInfo, String teamId) {
        this.teamMemberList = teamMemberList;
        this.touristsInfo = touristsInfo;
        touristSourceAdapter.setNewData(teamMemberSourceList);
        this.teamId = teamId;
        if(teamMemberSourceList.size() == 0){
            detail_look_tourist_info.setVisibility(View.GONE);
        }else{
            detail_look_tourist_info.setVisibility(View.VISIBLE);
        }
    }

    private String teamId;
}
