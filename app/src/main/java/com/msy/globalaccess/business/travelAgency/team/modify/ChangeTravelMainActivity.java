package com.msy.globalaccess.business.travelAgency.team.modify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.ChangeTravelLineAdapter;
import com.msy.globalaccess.business.travelAgency.team.guider.GuideListActivity;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.msy.globalaccess.business.travelAgency.team.ui.TeamDetailActivity.ACCOUNT_STATUS;


/**
 * Created by pepys on 2017/3/17.
 * description:变更行程界面
 */
public class ChangeTravelMainActivity extends BaseActivity {

    public static final String TEAMDETAILBEAN = "TeamDetailBean";

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;

    @BindView(R.id.change_travel_recycleView)
    RecyclerView change_travel_recycleView;
    /**
     * 数据源
     */
    private TeamDetailBean mTeamDetail;
    /**
     * 时间线adapter
     */
    private ChangeTravelLineAdapter timeLineAdapter;
    private View recyHeader;
    /**
     * 变更导游
     */
    private RelativeLayout change_travel_rl_changeGuide;
    /**
     * 变更行程
     */
    private RelativeLayout change_travel_rl_lineInfo;
    /**
     * 结算状态
     */
    private String accountStatus = "";

    public static void callActivity(Context context, TeamDetailBean mTeamDetail, String accountStatus) {
        Intent intent = new Intent(context, ChangeTravelMainActivity.class);
        intent.putExtra(TEAMDETAILBEAN, mTeamDetail);
        intent.putExtra(ACCOUNT_STATUS, accountStatus);
        context.startActivity(intent);
    }

    private void getIntentData() {
        if (getIntent().hasExtra(TEAMDETAILBEAN)) {
            mTeamDetail = getIntent().getParcelableExtra(TEAMDETAILBEAN);
        }
        if (getIntent().hasExtra(ACCOUNT_STATUS)) {
            accountStatus = getIntent().getStringExtra(ACCOUNT_STATUS);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_travel_main;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        getIntentData();
        initData();
        initListener();
        initRxBus();
    }
    private void initRxBus() {
        Subscription subscription = RxBus.getInstance().toObservable(RefreshTeamStatusEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RefreshTeamStatusEvent>() {
                    @Override
                    public void call(RefreshTeamStatusEvent refreshTeam) {
                        mTeamDetail.getTeamBaseInfo().setTeamStatus(Integer.parseInt(refreshTeam.getStatus()));
                    }
                });
        rxBusCache.add(subscription);
    }
    private void initData() {
        tvToolbarCenter.setText("变更行程");
        tvToolbarCenter.setVisibility(View.VISIBLE);
        recyHeader = LayoutInflater.from(this).inflate(R.layout.activity_change_travel_main_head, (ViewGroup)
                change_travel_recycleView.getParent(), false);
        change_travel_rl_changeGuide = (RelativeLayout) recyHeader.findViewById(R.id.change_travel_rl_changeGuide);
        change_travel_rl_lineInfo = (RelativeLayout) recyHeader.findViewById(R.id.change_travel_rl_lineInfo);
        timeLineAdapter = new ChangeTravelLineAdapter(this, R.layout.change_travel_timeline_item, new ArrayList<TeamLineInfoBean>());
        timeLineAdapter.addHeaderView(recyHeader);
        change_travel_recycleView.setLayoutManager(new LinearLayoutManager(this));
        change_travel_recycleView.setAdapter(timeLineAdapter);
    }

    private void initListener() {
        change_travel_rl_changeGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTeamDetail == null) {
                    ToastUtils.showToast("暂无数据");
                }
                GuideListActivity.callActivity(mTeamDetail, ChangeTravelMainActivity.this);
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });
        change_travel_rl_lineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int teamStatus = mTeamDetail.getTeamBaseInfo().getTeamStatus();
                if(teamStatus == 4 || teamStatus == 6 || teamStatus == 7 || teamStatus == 10){
                    if(accountStatus.equals("1")){
                        TravelAttractionsActivity.callActivity(ChangeTravelMainActivity.this,mTeamDetail);
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                    }else{
                        ToastUtils.showLongToast("该团队结算状态不能变更");
                    }
                }else{
                    ToastUtils.showLongToast("该团队处于变更状态，请审批过后再次变更");
                }
            }
        });
        change_travel_recycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                int teamStatus = mTeamDetail.getTeamBaseInfo().getTeamStatus();
//                if(teamStatus == 4 || teamStatus == 6 || teamStatus == 7){
//                    TeamLineInfoBean teamLineInfoBean = mTeamDetail.getLineListInfo().get(position);
//                    TravelAttractionsActivity.callActivity(ChangeTravelMainActivity.this,teamLineInfoBean,mTeamDetail);
//                    overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
//                }else{
//                    ToastUtils.showLongToast("该团队处于变更状态，请审批过后再次变更...");
//                }

            }
        });
    }
}
