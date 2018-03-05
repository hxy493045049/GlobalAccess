package com.msy.globalaccess.business.travelAgency.team.modify;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.TravelAttractionsAdapter;
import com.msy.globalaccess.business.adapter.TravelAttractionsParentAdapter;
import com.msy.globalaccess.business.travelAgency.team.contract.TravelAttractionsContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.TravelAttractionsPresenterImpl;
import com.msy.globalaccess.business.travelAgency.touristSpots.ui.TouristSpotsActivity;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsBean;
import com.msy.globalaccess.data.bean.travel.TravelAttractionsParentBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by WuDebin on 2017/3/15.
 */

public class TravelAttractionsActivity extends BaseActivity implements TravelAttractionsContract.View, TravelAttractionsAdapter.TravelAttractionsCallback, TravelAttractionsParentAdapter.TravelAttractionsParentCallback {

    @Inject
    TravelAttractionsPresenterImpl Presenter;

    @BindView(R.id.rv_travel_attractions_parent)
    RecyclerView rvTravelAttractions;

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;

    @BindView(R.id.tvToolbarRight)
    AppCompatTextView tvToolbarRight;

    @BindView(R.id.layout_title)
    Toolbar mToolbar;

    @BindView(R.id.ll_parent_layout)
    LinearLayout llParentLayout;

    private SmallDialog smallDialog;


    private String teamId;

    private TravelAttractionsParentAdapter mAdapter;

    private TravelAttractionsBean travelAttractionsBean;

    private TravelAttractionsParentBean travelAttractionsParentBean;

    /**
     * 被删除的景点
     */
    private ArrayList<TripScenicBean> tripScenicRemoveList = new ArrayList<>();
    /**
     * 正在操作的item
     */
    private TripScenicBean tripScenicBean;
    /**
     * 团队详情信息
     */
    private TeamDetailBean teamDetail;

    private int parentPosition;

    private int position;

    public static final int OPERATION_EDIT = 1;

    public static final int OPERATION_DELETED = 2;

    public static final int REQUEST_CODE_EDIT = 0x003;
    public static final int REQUEST_CODE_ADD = 0x004;

    public static final String TEAMLINEINFOBEAN = "teamLineInfoBean";
    public static final String TEAMDETAIL = "mTeamDetail";

    private View notDataView;

    public static void callActivity(ChangeTravelMainActivity context, TeamDetailBean teamDetail) {
        Intent intent = new Intent(context, TravelAttractionsActivity.class);
        intent.putExtra(TEAMDETAIL, teamDetail);
        context.startActivity(intent);
    }

    private void getIntentData() {
        if (getIntent().hasExtra(TEAMDETAIL)) {
            teamDetail = getIntent().getParcelableExtra(TEAMDETAIL);
            teamId = teamDetail.getTeamBaseInfo().getTeamId();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_travel_attractions;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        Presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = Presenter;
    }

    @Override
    protected void init() {
        getIntentData();
        rvTravelAttractions.setHasFixedSize(true);
        smallDialog = new SmallDialog(TravelAttractionsActivity.this, App.getResourceString(R.string.small_dialog));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        tvToolbarCenter.setVisibility(View.VISIBLE);
        tvToolbarCenter.setText("行程景点");
        tvToolbarRight.setVisibility(View.GONE);
        tvToolbarRight.setText("提交");
        tvToolbarRight.setTextSize(16);
        LinearLayoutManager manager = new LinearLayoutManager(rvTravelAttractions.getContext(), LinearLayoutManager.VERTICAL, false);
        rvTravelAttractions.setLayoutManager(manager);
        mAdapter = new TravelAttractionsParentAdapter(R.layout.item_parent_travel_attractions, null, this, this);
        rvTravelAttractions.setAdapter(mAdapter);
        initListener();
        notDataView = ViewHelper.getEmptyView(this, rvTravelAttractions);
        if (null != teamId) {
            Presenter.loadTravelAttractionData(teamId);
        }
    }


    private void initListener() {
        rvTravelAttractions.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    //点击确定按钮
    @OnClick(R.id.tvToolbarRight)
    public void onConfirmClick() {
        onCommitClick();
    }


    @Override
    public void showProgress() {
        smallDialog.show();
    }

    @Override
    public void hideProgress() {
        smallDialog.dismisss();
    }

    @Override
    public void editClick(TripScenicBean item, int position, int parentPosition) {
        this.position = position;
        this.tripScenicBean = item;
        this.parentPosition = parentPosition;
        if (item.getTripScenicId() != null) {
            Presenter.queryTripScenicIsOrder(item.getTripScenicId(), OPERATION_EDIT);
        } else {
            CreatScenicSpotActivity.callActivityForResult(TravelAttractionsActivity.this, tripScenicBean, true, false, travelAttractionsParentBean.getAdultsAmount(), travelAttractionsParentBean.getChildrenAmout(), true);
        }
    }

    @Override
    public void deletedClick(final String tripScenicId, final int position, final int parentPosition) {
        this.position = position;
        this.parentPosition = parentPosition;
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage("您确认删除该景点？", 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (tripScenicId != null) {
                    Presenter.queryTripScenicIsOrder(tripScenicId, OPERATION_DELETED);
                } else {
                    travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist().remove(position);
//                    travelAttractionsBean.getTripSceniclist().remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @Override
    public void getTravelAttractionsData(TravelAttractionsParentBean travelAttractionsParentBean) {
        if (travelAttractionsParentBean != null && travelAttractionsParentBean.getTeamTripList() != null) {
            tvToolbarRight.setVisibility(View.VISIBLE);
            this.travelAttractionsParentBean = travelAttractionsParentBean;
            mAdapter.setAreaAdultsAmount(travelAttractionsParentBean.getAdultsAmount());
            mAdapter.setAreaChildrenAmout(travelAttractionsParentBean.getChildrenAmout());
            mAdapter.setNewData(travelAttractionsParentBean.getTeamTripList());
            setListTeamTripId();
            rvTravelAttractions.smoothScrollToPosition(0);
            int top = rvTravelAttractions.getTop();
            rvTravelAttractions.scrollBy(0, top);
        } else {
            tvToolbarRight.setVisibility(View.GONE);
            llParentLayout.addView(notDataView);
            rvTravelAttractions.setVisibility(View.GONE);
            notDataView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getCommitDataStatus(int status) {
        if (status == ResultCode.SUCCESS) {
            ToastUtils.showToast("变更成功");
            RxBus.getInstance().post(new RefreshTeamStatusEvent(teamId, "5", 0, "2"));
            finish();
        }
    }

    @Override
    public void getQueryIsOrderStatus(int status, int operationType) {
        switch (operationType) {
            case OPERATION_EDIT:
                CreatScenicSpotActivity.callActivityForResult(TravelAttractionsActivity.this, tripScenicBean, false, status != ResultCode.SUCCESS, travelAttractionsParentBean.getAdultsAmount(), travelAttractionsParentBean.getChildrenAmout());
                break;
            case OPERATION_DELETED:
                if (status == ResultCode.SUCCESS) {
                    TripScenicBean tripScenicBean = travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist().get(position);
                    if (tripScenicBean.getChangeType() != null && !tripScenicBean.getChangeType().equals("2")) {
                        tripScenicBean.setChangeType("1");
                        tripScenicBean.setDelTag("1");
                        tripScenicRemoveList.add(tripScenicBean);
                    }
                    travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist().remove(position);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_EDIT:
                if (resultCode == RESULT_OK) {
                    TripScenicBean tripScenicBean = data.getParcelableExtra(CreatScenicSpotActivity.TRIPSCENICBEAN);
                    travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist().set(position, tripScenicBean);
                    mAdapter.notifyItemChanged(parentPosition);
                }
                break;
            case REQUEST_CODE_ADD:
                if (resultCode == RESULT_OK) {
                    TripScenicBean tripScenicBean = data.getParcelableExtra(CreatScenicSpotActivity.TRIPSCENICBEAN);
                    tripScenicBean.setTeamTripId(travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTeamTripId());
                    travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist().add(0, tripScenicBean);
                    mAdapter.notifyItemChanged(parentPosition);
                }
                break;
        }
    }

    public void onBackClick() {
        if (checkIsChanged()) {
            PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
            builder.setMessage("您确认放弃当前景点变更？", 18);
            builder.setTitle(null, 0);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        } else {
            finish();
        }
    }

    public void onCommitClick() {
        String hint = "";
        if (checkIsChanged()) {
            hint = "您确认提交当前行程景点信息？";
        } else {
            hint = "您未做任何变更，确认提交吗？";
        }
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(hint, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (travelAttractionsParentBean != null && teamId != null) {
                    commitData();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    public void setListTeamTripId() {
        travelAttractionsParentBean.getTeamTripList();
        for (int i = 0; i < travelAttractionsParentBean.getTeamTripList().size(); i++) {
            ArrayList<TripScenicBean> arrayList = travelAttractionsParentBean.getTeamTripList().get(i).getTripSceniclist();
            if (arrayList != null && arrayList.size() > 0) {
                for (int j = 0; j < arrayList.size(); j++) {
                    arrayList.get(j).setTeamTripId(travelAttractionsParentBean.getTeamTripList().get(i).getTeamTripId());
                }
            }
        }
    }

    public void commitData() {
        boolean canCommit = true;
        ArrayList<TripScenicBean> tripScenicList = new ArrayList<>();
        travelAttractionsParentBean.getTeamTripList();
        for (int i = 0; i < travelAttractionsParentBean.getTeamTripList().size(); i++) {
            ArrayList<TripScenicBean> arrayList = travelAttractionsParentBean.getTeamTripList().get(i).getTripSceniclist();
            if (arrayList == null || arrayList.size() <= 0) {
                canCommit = false;
            }
            tripScenicList.addAll(arrayList);
        }
        tripScenicList.addAll(tripScenicRemoveList);
        if (canCommit) {
            Presenter.commitTravelAttractionData(tripScenicList, teamId);
        } else {
            ToastUtils.showToast("每天行程中至少保留一个景点");
        }
    }

    /**
     * @return true说明数据有改变，false没有改变
     */
    public boolean checkIsChanged() {
        if (travelAttractionsParentBean == null) {
            return false;
        }
        ArrayList<TripScenicBean> tripScenicList = new ArrayList<>();
        travelAttractionsParentBean.getTeamTripList();
        for (int i = 0; i < travelAttractionsParentBean.getTeamTripList().size(); i++) {
            ArrayList<TripScenicBean> arrayList = travelAttractionsParentBean.getTeamTripList().get(i).getTripSceniclist();
            tripScenicList.addAll(arrayList);
        }
        tripScenicList.addAll(tripScenicRemoveList);
        for (int i = 0; i < tripScenicList.size(); i++) {
            if (tripScenicList.get(i).getChangeType() != null && !tripScenicList.get(i).getChangeType().equals("0")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        onBackClick();
    }

    @Override
    public void addAttractionsClick(int parentPosition) {
        this.parentPosition = parentPosition;
        TouristSpotsActivity.callActivity(TravelAttractionsActivity.this, teamDetail.getLineListInfo().get(parentPosition), travelAttractionsParentBean.getTeamTripList().get(parentPosition).getTripSceniclist(), travelAttractionsParentBean.getChildrenAmout(), travelAttractionsParentBean.getAdultsAmount());
    }
}