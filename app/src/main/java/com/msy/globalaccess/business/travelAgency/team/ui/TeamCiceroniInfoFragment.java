package com.msy.globalaccess.business.travelAgency.team.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.PhotoActivity;
import com.msy.globalaccess.business.adapter.TeamCiceronInfoAdapter;
import com.msy.globalaccess.data.bean.team.TeamCiceronInfoBean;
import com.msy.globalaccess.listener.SetTeamDetailGuideInfoListener;
import com.msy.globalaccess.widget.customView.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by pepys on 2017/2/7.
 * description: 团队详情-导游信息
 */
public class TeamCiceroniInfoFragment extends BaseFragment implements SetTeamDetailGuideInfoListener {
    private CustomViewPager vp;
    @BindView(R.id.ciceroninfo_recycleView)
    RecyclerView recycleView;
    /**
     * 导游数据源
     */
    private ArrayList<TeamCiceronInfoBean> ciceronList = new ArrayList<>();
    /**
     * 导游适配器
     */
    private TeamCiceronInfoAdapter teamCiceronInfoAdapter;
    /**
     * 无数据
     */
    private View notDataView;

    public TeamCiceroniInfoFragment() {
    }


    public void setVp(CustomViewPager vp) {
        this.vp = vp;
    }

    public static TeamCiceroniInfoFragment newInstance() {
        TeamCiceroniInfoFragment fragment = new TeamCiceroniInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_ciceroninfo;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }

    @Override
    public void init(View view) {
        vp.setObjectForPosition(view, 3);
        initData();
        setRecycleViewClick();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setNestedScrollingEnabled(false);
        teamCiceronInfoAdapter = new TeamCiceronInfoAdapter(R.layout.fragment_team_ciceroninfo_item, ciceronList,getActivity());
        recycleView.setAdapter(teamCiceronInfoAdapter);
        notDataView = LayoutInflater.from(getActivity()).inflate(R.layout.empty_look_view, (ViewGroup) recycleView.getParent(), false);
        teamCiceronInfoAdapter.setEmptyView(notDataView);
    }



    @Override
    public void seteamDetailGuideInfo(ArrayList<TeamCiceronInfoBean> teamCiceronInfoList) {
        ciceronList.clear();
        ciceronList.addAll(teamCiceronInfoList);
//        ciceronList.get(0).setPicUrl("https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/wh%3D600%2C800/sign=19a99071fa1fbe091c0bcb125b50200b/1f178a82b9014a909461e9baa1773912b31bee5e.jpg");
        if (teamCiceronInfoAdapter != null) {
            teamCiceronInfoAdapter.setNewData(ciceronList);
        }
    }

    private void setRecycleViewClick() {
        recycleView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.team_detail_ciceron_head:
                        PhotoActivity.callActivity(getActivity(),teamCiceronInfoAdapter.getData().get(position).getPicUrl());
                        break;
                }
            }
        });


    }
}
