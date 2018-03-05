package com.msy.globalaccess.business.travelAgency.team.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.travelAgency.team.contract.ApprovalContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.ApprovalPresneterImpl;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.guider.TouristDelegateBean;
import com.msy.globalaccess.event.RefreshDelegateListEvent;
import com.msy.globalaccess.event.RefreshTeamBadgeEvent;
import com.msy.globalaccess.event.RefreshTeamStatusEvent;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.msy.globalaccess.business.travelAgency.team.ui.TeamDetailActivity.TOURISTDELEGATEBEAN;

/**
 * Created by pepys on 2017/2/15.
 * description:审批界面
 */
public class ApprovalActivity extends BaseActivity implements ApprovalContract.View {
    public static final String TEAM_DETAIL = "teamDetail";
    public static final String APPROVAL_TITLE = "approvalTile";
    public static final String APPROVAL_TEAM_STATUS = "approvalTeamStatus";
    public static final String APPROVAL_STATUS = "approvalStatus";
    @Inject
    public ApprovalPresneterImpl approvalPresneter;
    /**
     * 头部标题
     */
    @BindView(R.id.activity_approval_tv_title)
    AppCompatTextView activity_approval_tv_title;
    /**
     * 团队编号
     */
    @BindView(R.id.activity_approval_tv_team_number)
    TextView activity_approval_tv_team_number;
    /**
     * 团队状态
     */
    @BindView(R.id.activity_approval_tv_team_status)
    TextView activity_approval_tv_team_status;
    /**
     * 待处理单位名称
     */
    @BindView(R.id.activity_approval_tv_depName)
    TextView activity_approval_tv_depName;
    /**
     * 审批理由标题
     */
    @BindView(R.id.activity_approval_tv_content_title)
    TextView activity_approval_tv_content_title;
    /**
     * 审批理由
     */
    @BindView(R.id.activity_approval_edt_content)
    EditText activity_approval_edt_content;
    /**
     * 委派导游名称
     */
    @BindView(R.id.activity_approval_tv_guide_name)
    TextView activity_approval_tv_guide_name;
    /**
     * 单位标题
     */
    @BindView(R.id.activity_approval_tv_dep)
    TextView activity_approval_tv_dep;



    /**
     * 团队详情
     */
    private TeamDetailBean mTeamDetail;
    /**
     * 团队编号
     */
    private String teamCode = "";
    /**
     * 单位
     */
    private String travelAgentName="";
    /**
     * 审批界面头部
     */
    private String approvalTitle = "";
    /**
     * 审批界面状态
     */
    private String approvalTeamStatus = "";
    /**
     * loading图
     */
    private SmallDialog smallDialog;
    /**
     * 请求时候的审批状态。拒绝或者同意
     */
    private int approvalRequestStatus;
    /**
     * 请求时候的类型 0:出团审批;1:作废审批;2:变更审批;3:作废申请;4:出团
     */
    private int approvalRequestType;
    /**
     * 从哪个Fragment进来的。
     */
    private String fragment_Type = "";
    /**
     * 导游信息
     */
    private TouristDelegateBean mTouristDelegateBean = new TouristDelegateBean();
    /**
     * 是否是审批导游委派
     */
    private boolean isDelegateTourist = false;
    /**
     * 委派导游名称
     */
    private String guide_name = "";
    /**
     * 团队审批
     * @param context
     * @param mTeamDetail
     * @param approvalTitle
     * @param approvalTeamStatus
     * @param approvalRequestStatus
     * @param fragmentType
     */
    public static void callActivity(Context context, TeamDetailBean mTeamDetail, String approvalTitle, String
            approvalTeamStatus, int approvalRequestStatus, String fragmentType) {
        Intent intent = new Intent(context, ApprovalActivity.class);
        intent.putExtra(TEAM_DETAIL, mTeamDetail);
        intent.putExtra(APPROVAL_TITLE, approvalTitle);
        intent.putExtra(APPROVAL_TEAM_STATUS, approvalTeamStatus);
        intent.putExtra(APPROVAL_STATUS, approvalRequestStatus);
        intent.putExtra(TeamDetailActivity.FRAGMENT_TYPE, fragmentType);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    /**
     * 导游委派审批
     * @param context
     * @param touristDelegateBean
     */
    public static void callActivity(Context context,TouristDelegateBean touristDelegateBean){
        Intent intent = new Intent(context, ApprovalActivity.class);
        intent.putExtra(TOURISTDELEGATEBEAN, touristDelegateBean);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TEAM_DETAIL)) {
            mTeamDetail = intent.getParcelableExtra(TEAM_DETAIL);
            teamCode = mTeamDetail.getTeamBaseInfo().getTeamCode();
            travelAgentName = mTeamDetail.getTeamBaseInfo().getTravelAgentName();
        }
        if (intent.hasExtra(APPROVAL_TITLE)) {
            approvalTitle = intent.getStringExtra(APPROVAL_TITLE);
        }
        if (intent.hasExtra(APPROVAL_TEAM_STATUS)) {
            approvalTeamStatus = intent.getStringExtra(APPROVAL_TEAM_STATUS);
        }
        if (intent.hasExtra(APPROVAL_STATUS)) {
            approvalRequestType = intent.getIntExtra(APPROVAL_STATUS, -1);
        }
        if (intent.hasExtra(TeamDetailActivity.FRAGMENT_TYPE)) {
            fragment_Type = intent.getStringExtra(TeamDetailActivity.FRAGMENT_TYPE);
        }
        if(getIntent().hasExtra(TOURISTDELEGATEBEAN)){
            mTouristDelegateBean = getIntent().getParcelableExtra(TOURISTDELEGATEBEAN);
            isDelegateTourist = true;
            approvalTitle = "导游委派处理";
            if(mTouristDelegateBean.getOpType().equals("0")){
                approvalTeamStatus = "申请类型：申请委派";
            }else{
                approvalTeamStatus = "申请类型：取消委派";
            }
            teamCode = mTouristDelegateBean.getTeamCode();
            travelAgentName = mTouristDelegateBean.getOperName();
            guide_name = mTouristDelegateBean.getTeamGuideName();

            activity_approval_tv_guide_name.setVisibility(View.VISIBLE);
            activity_approval_tv_dep.setText("挂靠机构：");
            activity_approval_tv_content_title.setText("处理说明");
            activity_approval_edt_content.setHint("请输入处理说明.....");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_approval;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        approvalPresneter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = approvalPresneter;
    }

    @Override
    protected void init() {
        getIntentData();
        smallDialog = new SmallDialog(this, getResources().getString(R.string.small_dialog));
        activity_approval_tv_title.setText(approvalTitle);
        activity_approval_tv_team_number.setText(teamCode);
        activity_approval_tv_team_status.setText(approvalTeamStatus);
        activity_approval_tv_depName.setText(travelAgentName);
        activity_approval_tv_guide_name.setText("委派导游："+guide_name);
    }

    @OnClick({R.id.activity_approval_button_back, R.id.activity_approval_tv_refuse, R.id.activity_approval_tv_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_approval_button_back:
                finish();
                break;
            case R.id.activity_approval_tv_refuse:
                showAlertDialog(1);
                break;
            case R.id.activity_approval_tv_agree:
                showAlertDialog(0);
                break;
        }
    }

    @Override
    public String getTeamID() {
        return mTeamDetail.getTeamBaseInfo().getTeamId();
    }

    @Override
    public int getOpType() {
        return approvalRequestType;
    }

    @Override
    public int getOpStatus() {
        return approvalRequestStatus;
    }

    @Override
    public String getRemark() {
        return activity_approval_edt_content.getText().toString().trim();
    }

    @Override
    public void approvalSuccess(String message) {
        String teamStatus = "";
        String operType = "";
        int operStatus = 0;
        int badgeChange = 0;
        switch (getOpType()) {
            case 0:  //这里是审批出团申请
                operType = "0";
                if (getOpStatus() == 0) {
                    //同意申请
                    teamStatus = "2";//已提交（通过审核）
                    operStatus = 1;
                } else if (getOpStatus() == 1) {
                    //拒绝申请
                    teamStatus = "0";//已提交（审核失败）变成编辑中。
                    operStatus = 2;
                }
                break;
            case 1:  //审批作废申请
                operType = "1";
                if (getOpStatus() == 0) {
                    //同意申请
                    teamStatus = "9";//已提交（通过审核）
                    operStatus = 1;
                } else if (getOpStatus() == 1) {
                    //拒绝申请
                    teamStatus = "10";//已提交（审核失败）
                    operStatus = 2;
                }
                break;
            case 2: //审批变更申请
                operType = "2";
                if (getOpStatus() == 0) {
                    //同意申请
                    teamStatus = "6";//已提交（通过审核）
                    operStatus = 1;
                } else if (getOpStatus() == 1) {
                    //拒绝申请
                    teamStatus = "7";//已提交（审核失败）
                    operStatus = 2;
                }
                break;
            case 3:  //这里是提交作废申请
                teamStatus = "8";
                operStatus = 0;
                operType = "1";
                badgeChange = 1;
                break;
            case 4:  //这里是出团按钮
                teamStatus = "4";
                operStatus = 1;
                operType = "0";
                break;
        }
        RxBus.getInstance().post(new RefreshTeamStatusEvent(getTeamID(), teamStatus, operStatus,operType));
        RxBus.getInstance().post(new RefreshTeamBadgeEvent(fragment_Type,operType,badgeChange));
        showAlertDialog(message);

    }

    @Override
    public void approvalFailure() {
        finish();
    }

    @Override
    public String getTeamGuideID() {
        return mTouristDelegateBean.getTeamGuideId();
    }

    @Override
    public String getApprovalType() {
        return approvalRequestStatus+"";
    }

    @Override
    public void approvalDelegateTouristSuccess(String message) {
        if(approvalRequestStatus == 1){//拒绝 未通过
            RxBus.getInstance().post(new RefreshDelegateListEvent(getTeamGuideID(),"1"));
        }else{//通过
            RxBus.getInstance().post(new RefreshDelegateListEvent(getTeamGuideID(),"2"));
        }
        showAlertDialog(message);
    }

    @Override
    public void approvalDelegateTouristFailure() {

    }

    @Override
    public void showProgress() {
        smallDialog.shows();
    }

    @Override
    public void hideProgress() {
        smallDialog.hide();
    }

    /**
     * 展示Dialog
     *
     * @param approvalStatus 审批类型
     */
    private void showAlertDialog(final int approvalStatus) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        String title = approvalStatus == 0 ? "确认同意审批？" : "确认拒绝审批？";
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                approvalRequestStatus = approvalStatus;
                if(isDelegateTourist){
                    approvalPresneter.loadApprovalDelegateTourist();
                }else{
                    approvalPresneter.loadApproval();
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

    /**
     * 展示Dialog
     */
    private void showAlertDialog(String title) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                finish();
            }
        });
        builder.create().show();
    }
}
