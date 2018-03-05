package com.msy.globalaccess.business.travelAgency.team.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.TouristInfoAdapter;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchActivity;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.data.bean.team.TeamMemberBean;
import com.msy.globalaccess.data.bean.tourism.TouristsInfoBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by pepys on 2017/2/14.
 * description:查看游客名单
 */
public class CheckTouristInfoActivity extends BaseActivity {
    /**
     * 旅行社
     */
    @BindView(R.id.check_tourist_tv_travelAgentName)
    AppCompatTextView check_tourist_tv_travelAgentName;
    /**
     * 团队编号
     */
    @BindView(R.id.check_tourist_tv_teamCode)
    AppCompatTextView check_tourist_tv_teamCode;
    /**
     * 司机电话
     */
    @BindView(R.id.check_tourist_tv_driverPhones)
    AppCompatTextView check_tourist_tv_driverPhones;
    /**
     * 行程日期
     */
    @BindView(R.id.check_tourist_tv_tripData)
    AppCompatTextView check_tourist_tv_tripData;
    /**
     * 导游
     */
    @BindView(R.id.check_tourist_tv_guide)
    AppCompatTextView check_tourist_tv_guide;
    /**
     * 车牌号
     */
    @BindView(R.id.check_tourist_tv_vehicleInfo)
    AppCompatTextView check_tourist_tv_vehicleInfo;
    /**
     * 总人数
     */
    @BindView(R.id.check_tourist_tv_count)
    AppCompatTextView check_tourist_tv_count;
    /**
     * recycleView
     */
    @BindView(R.id.check_tourist_recycle)
    RecyclerView check_tourist_recycle;
    /**
     * nestedScrollView布局
     */
    @BindView(R.id.activity_check_tourist_nestedScrollView)
    NestedScrollView activity_check_tourist_nestedScrollView;
    /**
     * 游客信息Adapter
     */
    private TouristInfoAdapter touristInfoAdapter;
    /**
     * 游客信息数据源
     */
    private ArrayList<TeamMemberBean> teamMemberList = new ArrayList<>();
    /**
     * 游客名单上面的信息
     */
    private TouristsInfoBean touristsInfo = new TouristsInfoBean();
    /**
     * 无数据界面
     */
    private View notDataView;
    /**
     * 动态权限
     */
    private RxPermissions rxPermissions;
    /**
     * 是否有电话权限
     */
    private boolean callPhonePermission;
    /**
     * 要申请的权限
     */
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    /**
     * 记录电话
     */
    private String telPhone = "";

    @BindView(R.id.ivToolbarRight)
    AppCompatImageView ivToolbarRight;

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;

    public static final String TOURISTSINFO = "touristsInfo";
    public static final String TEAMMEMBERLIST = "teamMemberList";
    private String teamId;

    public static void callActivity(Context context, ArrayList<TeamMemberBean> teamMemberList, TouristsInfoBean touristsInfo, String teamId) {
        Intent intent = new Intent(context, CheckTouristInfoActivity.class);
        intent.putParcelableArrayListExtra(TEAMMEMBERLIST, teamMemberList);
        intent.putExtra(TOURISTSINFO, touristsInfo);
        intent.putExtra("teamId", teamId);
        intent.putExtra(TOURISTSINFO, touristsInfo);
        context.startActivity(intent);
    }

    private void getIntentData() {
        teamId = getIntent().getStringExtra("teamId");
        if (getIntent().hasExtra(TEAMMEMBERLIST)) {
            teamMemberList = getIntent().getParcelableArrayListExtra(TEAMMEMBERLIST);
        }
        if (getIntent().hasExtra(TOURISTSINFO)) {
            touristsInfo = getIntent().getParcelableExtra(TOURISTSINFO);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_tourist_info;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        getIntentData();
        rxPermissions = new RxPermissions(this);
        initPermissions();
        tvToolbarCenter.setText("游客名单");
        ivToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarCenter.setVisibility(View.VISIBLE);
        check_tourist_tv_travelAgentName.setText(touristsInfo.getTravelAgentName());
        check_tourist_tv_teamCode.setText(touristsInfo.getTeamCode());
        check_tourist_tv_driverPhones.setText(touristsInfo.getDriverPhone());
        check_tourist_tv_tripData.setText(touristsInfo.getTripDates());
        check_tourist_tv_guide.setText(touristsInfo.getGuideInfos());
        check_tourist_tv_vehicleInfo.setText(touristsInfo.getVehicleInfos());
        check_tourist_tv_count.setText(touristsInfo.getPeopleCount());

        touristInfoAdapter = new TouristInfoAdapter(R.layout.activity_check_tourist_info_item, teamMemberList);
        touristInfoAdapter.openLoadAnimation();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        check_tourist_recycle.setLayoutManager(linearLayoutManager);
        check_tourist_recycle.setHasFixedSize(true);
        check_tourist_recycle.setNestedScrollingEnabled(false);
        check_tourist_recycle.setAdapter(touristInfoAdapter);
        check_tourist_recycle.setItemAnimator(new DefaultItemAnimator());

        notDataView = LayoutInflater.from(this).inflate(R.layout.empty_look_view, (ViewGroup) check_tourist_recycle.getParent(), false);
        touristInfoAdapter.setEmptyView(notDataView);
        setRecycleViewClick();
    }
    private void initPermissions(){
        rxPermissions.request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        callPhonePermission = granted;
                    }
                });
    }
    private void setRecycleViewClick() {
        check_tourist_recycle.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.check_tourist_item_ll_phone:
                        TeamMemberBean teamMemberBean = (TeamMemberBean) adapter.getData().get(position);
                        telPhone = teamMemberBean.getPhoneNum();
                        if(callPhonePermission){
                            callPhone();
                        }else{
                            showAlertDialog("拨打电话权限不可用",1);
                        }

                        break;
                }
            }
        });


    }

    @OnClick(R.id.ivToolbarRight)
    public void clickSearch() {
        SearchActivity.callActivity(this, SearchResultActivity.TOURIST_SEARCH, teamId);
    }

    private void callPhone(){

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone)));
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showAlertDialog("请在-应用设置-权限中，允许使用",2);
                    } else finish();
                } else {
                    ToastUtils.showToast("权限获取成功");
                    callPhonePermission = true;
                    if(!telPhone.equals("")){
                        callPhone();
                    }
                }
            }
        }
    }

    /**
     * 展示Dialog
     */
    private void showAlertDialog(String message, final int type) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(message, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type){
                    case 1:
                        ActivityCompat.requestPermissions(CheckTouristInfoActivity.this, permissions, 1);
                        break;
                    case 2:
                        goToAppSetting();
                        break;
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
    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 1);
    }
}
