package com.msy.globalaccess.business.travelAgency.team.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.data.bean.team.TeamBaseInfoBean;
import com.msy.globalaccess.listener.SetTeamDetailBaseInfoListener;
import com.msy.globalaccess.widget.customView.CustomViewPager;

import butterknife.BindView;

/**
 * Created by pepys on 2017/2/7.
 * description: 团队详情基本信息
 */
public class TeamEssentialInfoFragment extends BaseFragment implements SetTeamDetailBaseInfoListener{

    private CustomViewPager vp;
    /**
     * 团队编号
     */
    @BindView(R.id.baseinfo_tv_teamCode)
    TextView baseinfo_tv_teamCode;
    /**
     * 部门
     */
    @BindView(R.id.baseinfo_tv_travelDepName)
    TextView baseinfo_tv_travelDepName;
    /**
     * 部门标题
     */
    @BindView(R.id.baseinfo_tv_travel)
    TextView baseinfo_tv_travel;
    /**
     * 团队类型
     */
    @BindView(R.id.baseinfo_tv_teamTypeName)
    TextView baseinfo_tv_teamTypeName;
    /**
     * 线路名称
     */
    @BindView(R.id.baseinfo_tv_lineName)
    TextView baseinfo_tv_lineName;
    /**
     * 组团社
     */
    @BindView(R.id.baseinfo_tv_teamGroup)
    TextView baseinfo_tv_teamGroup;
    /**
     * 领队全陪
     */
    @BindView(R.id.baseinfo_tv_leader)
    TextView baseinfo_tv_leader;
    /**
     * 到达方式和时间
     */
    @BindView(R.id.baseinfo_tv_departureInfo)
    TextView baseinfo_tv_departureInfo;
    /**
     * 离开方式和时间
     */
    @BindView(R.id.baseinfo_tv_returnInfo)
    TextView baseinfo_tv_returnInfo;
    /**
     * 保险
     */
    @BindView(R.id.baseinfo_tv_insInfo)
    TextView baseinfo_tv_insInfo;


    public TeamEssentialInfoFragment() {
    }

    public void setVp(CustomViewPager vp) {
        this.vp = vp;
    }

    public static TeamEssentialInfoFragment newInstance(){
        TeamEssentialInfoFragment fragment = new TeamEssentialInfoFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void init(View view) {
        vp.setObjectForPosition(view,0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_team_essential_info;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    @Override
    public void setTeamDetailBaseInfo(TeamBaseInfoBean teamBaseInfo) {
        baseinfo_tv_teamCode.setText(teamBaseInfo.getTeamNum().trim());
        if ("2".equals(App.userHelper.getUser().getUserRoleType())) {
            baseinfo_tv_travel.setText("旅行社");
            baseinfo_tv_travelDepName.setText(teamBaseInfo.getTravelAgentName().trim());
        }else{
            baseinfo_tv_travel.setText("部门");
            baseinfo_tv_travelDepName.setText(teamBaseInfo.getTravelDepName().trim());
        }
        baseinfo_tv_teamTypeName.setText(teamBaseInfo.getTeamTypeName().trim());
        baseinfo_tv_teamGroup.setText(teamBaseInfo.getTeamGroup().trim());
        baseinfo_tv_lineName.setText(teamBaseInfo.getLineName().trim());
        baseinfo_tv_leader.setText(teamBaseInfo.getLeader().trim());
        baseinfo_tv_departureInfo.setText(teamBaseInfo.getDepartureInfo().trim());
        baseinfo_tv_returnInfo.setText(teamBaseInfo.getReturnInfo().trim());
        baseinfo_tv_insInfo.setText(teamBaseInfo.getInsInfo().trim());
    }
}
