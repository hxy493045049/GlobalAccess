package com.msy.globalaccess.business.travelAgency.settlement.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.travelAgency.settlement.contract.ISettlementDetailContract;
import com.msy.globalaccess.business.travelAgency.settlement.contract.impl.SettlementDetailPresenterImpl;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.RoleBean;
import com.msy.globalaccess.data.bean.settlement.SettlementDetailBean;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.event.SettlementSuccessEvent;
import com.msy.globalaccess.utils.ErrorUtils;
import com.msy.globalaccess.utils.RxBus;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.AlertDialogBuilder;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.StringUtils;
import rx.Subscription;
import rx.functions.Action1;

import static com.msy.globalaccess.R.id.toolbar;

/**
 * Created by hxy on 2017/1/20 0020.
 * <p>
 * description :
 */
public class SettlementDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        ISettlementDetailContract.View {
    private static final String KEY_TEAM_DUDIT_ID = "teamAuditId";
    private static final String KEY_SEARCH_TYPE = "dataType";
    @BindView(R.id.swpSettlementDetail)
    SwipeRefreshLayout mSwpSettlementDetail;

    @Inject
    SettlementDetailPresenterImpl mPresenter;

    @BindView(toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tvSettlementPayNumber)
    AppCompatTextView tvSettlementPayNumber;

    @BindView(R.id.tvSettlementTeamNumber)
    AppCompatTextView tvSettlementTeamNumber;

    @BindView(R.id.tvSettlementType)
    AppCompatTextView tvSettlementType;

    @BindView(R.id.tvSettlementMethod)
    AppCompatTextView tvSettlementMethod;

    @BindView(R.id.tvSettlementExpenditureUnit)
    AppCompatTextView tvSettlementExpenditureUnit;

    @BindView(R.id.tvSettlementIncomeUnit)
    AppCompatTextView tvSettlementIncomeUnit;

    @BindView(R.id.tvSettlementPrepayments)
    AppCompatTextView tvSettlementPrepayments;

    @BindView(R.id.tvSettlementCommitMoney)
    AppCompatTextView tvSettlementCommitMoney;

    @BindView(R.id.tvSettlementConcernedDepartment)
    AppCompatTextView tvSettlementConcernedDepartment;

    @BindView(R.id.tvSettlementConsumptionMoney)
    AppCompatTextView tvSettlementConsumptionMoney;

    @BindView(R.id.tvSettlementStatus)
    AppCompatTextView tvSettlementStatus;

    @BindView(R.id.tvSettlementCreator)
    AppCompatTextView tvSettlementCreator;

    @BindView(R.id.tvSettlementDesc)
    AppCompatTextView tvSettlementDesc;

    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView mTvTitleBarTitle;

    @BindView(R.id.btnSettlement)
    AppCompatButton btnSettlement;
    ArrayList<RoleBean> roleBeanList;
    private String mTeamAuditId;
    private int green, blue, orange, refund, textColor;
    private String holderMoney;
    private Dialog pwdPop;
    private SmallDialog loading;
    private String pwd;
    private EditText etPwd;
    private String mDataType;//查询类型
    private String auditType = "";//结算单类型：0:预支付;1:追加预支付;2:支付;3:退款;

    /**
     * 如果需要更新列表的小圆点,需要传递是哪个列表的type
     *
     * @param teamAuditId id
     */
    public static void callActivity(Context ctx, String teamAuditId, String searchType) {
        Intent intent = new Intent(ctx, SettlementDetailActivity.class);
        intent.putExtra(KEY_TEAM_DUDIT_ID, teamAuditId);
        intent.putExtra(KEY_SEARCH_TYPE, searchType);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Subscription subscription = RxBus.getInstance().toObservable(SettlementSuccessEvent.class)
                .subscribe(new Action1<SettlementSuccessEvent>() {
                    @Override
                    public void call(SettlementSuccessEvent settlementSuccessEvent) {
                        mPresenter.loadSettlementDetail();
                    }
                });
        rxBusCache.add(subscription);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement_detail;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        basePresenter = mPresenter;
        basePresenter.attachView(this);
    }

    @Override
    protected void init() {
        loading = new SmallDialog(this, App.getResourceString(R.string.small_dialog));
        mToolbar.setNavigationIcon(R.mipmap.back);
        //如果需要toolbar的title居中显示,不要使用系统的title
        //        mToolbar.setTitle(App.getResourceString(R.string.settlement_detail_title));
        mTvTitleBarTitle.setVisibility(View.VISIBLE);
        mTvTitleBarTitle.setText(App.getResourceString(R.string.settlement_detail_title));
        getArgument();
        mSwpSettlementDetail.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        mSwpSettlementDetail.setOnRefreshListener(this);
        green = App.getResource().getColor(R.color.green);
        blue = App.getResource().getColor(R.color.blue);
        orange = App.getResource().getColor(R.color.orange);
        refund = App.getResource().getColor(R.color.red_light);
        textColor = App.getResource().getColor(R.color.text_primary_dark);
        holderMoney = App.getResourceString(R.string.holder_money_double);

        pwdPop = new AlertDialogBuilder(this)
                .setContentLayout(R.layout.dialog_pwd)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pwdPop.dismiss();
                        if (etPwd != null) {
                            pwd = etPwd.getText().toString();
                        }
                        mPresenter.settlement();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pwdPop.dismiss();
                    }
                }).create();

        etPwd = (EditText) pwdPop.findViewById(R.id.etPwd);
        pwdPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (etPwd != null) {
                    etPwd.setText("");
                }
            }
        });
        pwdPop.setCancelable(true);
        pwdPop.setCanceledOnTouchOutside(false);
    }

    @OnClick(R.id.btnSettlement)
    public void clickSettlement() {
        pwdPop.show();
    }

    @Override
    public void onRefresh() {
        mPresenter.loadSettlementDetail();
    }

    @Override
    public void showProgress() {
        mSwpSettlementDetail.post(new Runnable() {
            @Override
            public void run() {
                mSwpSettlementDetail.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideProgress() {
        mSwpSettlementDetail.setRefreshing(false);
    }

    @Override
    public void handlerDetailData(SettlementDetailBean bean) {
        auditType = bean.getAuditType();
        tvSettlementPayNumber.setText(bean.getPayCode());//支付编号
        tvSettlementTeamNumber.setText(bean.getTeamCode());//团队编号
        tvSettlementExpenditureUnit.setText(bean.getOutComeUnitName());//支出单位
        tvSettlementIncomeUnit.setText(bean.getInComeUnitName());//收入单位
        tvSettlementConcernedDepartment.setText(bean.getLinkUnitName());//相关单位

        tvSettlementCreator.setText(bean.getCreater() + "\t(" + bean.getCreateTime() + ")");//支付创建者

        //预付款（分）
        tvSettlementPrepayments.setText(String.format(holderMoney, Double.parseDouble(bean.getPrePayMoney()) / 100.0));
        //提交结算金额（分）
        tvSettlementCommitMoney.setText(String.format(holderMoney, Double.parseDouble(bean.getAuditMoney()) / 100.0));

        StringBuilder sb = new StringBuilder();
        sb.append(bean.getAuditMethod().equals("0") ?
                App.getResourceString(R.string.settlement_method_real_time) :
                App.getResourceString(R.string.settlement_method_delay));
        sb.append("\t\t");
        sb.append(App.getResourceString(R.string.settlement_bank));
        sb.append(bean.getBankName());
        //结算方式:0:实时结算;1:暂延支付;
        tvSettlementMethod.setText(sb.toString());

        //变更文本和字体颜色
        changeAuditType(bean);
        chageTextColor();
        //变更文本
        changeSettlementStatus(bean);
        //描述文本,如果字符长度超过18个就换行
        addDescText(bean, 18);
        //变更结算按钮状态
        changeBtnStatus(bean);
    }

    @Override
    public void onError(@ResultCode int code, String errorMessage) {
        ToastUtils.showToast(errorMessage);
    }

    @Override
    public void showLoading() {
        loading.show();
        loading.setCanceledOnTouchOutside(false);
    }

    @Override
    public void dismissLoading() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    public void handlerSettlementData(NoDataBean bean) {
        if (bean.getStatus() == ResultCode.SUCCESS) {
            SettlementSuccessActivity.callActivity(this, mTeamAuditId, mDataType, auditType);
        } else {
            ErrorUtils.getErrorMessage(bean, "");
        }
    }

    @Override
    public String getTeamAuditId() {
        return mTeamAuditId;
    }

    @Override
    public String getUserId() {
        return App.userHelper.getUser().getUserId();
    }

    @Override
    public String getPassWord() {
        return pwd;
    }

    //-----------------------------private---------------------------
    //获取参数
    private void getArgument() {
        mTeamAuditId = getIntent().getStringExtra(KEY_TEAM_DUDIT_ID);
        mDataType = getIntent().getStringExtra(KEY_SEARCH_TYPE);
    }

    //设置描述文本最大字符
    private void addDescText(SettlementDetailBean bean, int limitSize) {
        //支付描述
        String temp = bean.getPayMemo();
        if (TextUtils.isEmpty(temp)) {
            return;
        }
        tvSettlementDesc.setText(StringUtils.addBreakLineAtIndex(limitSize, temp));
    }

    //根据类型变更文本
    private void changeAuditType(SettlementDetailBean bean) {
        String type = bean.getAuditType();
        String consumptionMoney = String.format(holderMoney, 0.0);
        switch (type) {
            case "0"://0:预支付
                tvSettlementType.setText(App.getResourceString(R.string.order_type_prepay));
                textColor = blue;
                break;
            case "1"://1:追加预支付;
                tvSettlementType.setText(App.getResourceString(R.string.order_type_append));
                textColor = green;
                break;
            case "2"://2:支付;
                tvSettlementType.setText(App.getResourceString(R.string.order_type_pay));
                consumptionMoney = String.format(holderMoney, Double.parseDouble(bean.getAuditMoney()) / 100.0);
                textColor = orange;
                break;
            case "3"://3:退款;
                tvSettlementType.setText(App.getResourceString(R.string.order_type_refund));
                textColor = refund;
                break;
            default:
        }
        tvSettlementConsumptionMoney.setText(consumptionMoney);
    }

    /**
     * @param bean 用于获取结算单状态
     */
    private void changeSettlementStatus(SettlementDetailBean bean) {
        String status = bean.getAuditStatus();
        switch (status) {
            case "0"://0:待结算;
                tvSettlementStatus.setText(App.getResourceString(R.string.order_status_waitting));
                break;
            case "1"://1:结算中;
                tvSettlementStatus.setText(App.getResourceString(R.string.order_status_calculating));
                break;
            case "2"://2:结算成功;
                tvSettlementStatus.setText(App.getResourceString(R.string.order_status_success));
                break;
            case "3"://3:结算失败;
                tvSettlementStatus.setText(App.getResourceString(R.string.order_status_failed));
                break;
        }
    }

    //变更文本颜色
    private void chageTextColor() {
        tvSettlementType.setTextColor(textColor);
        tvSettlementPrepayments.setTextColor(textColor);
        tvSettlementCommitMoney.setTextColor(textColor);
        tvSettlementConsumptionMoney.setTextColor(textColor);
        tvSettlementStatus.setTextColor(textColor);
    }

    //设置结算按钮状态
    private void changeBtnStatus(SettlementDetailBean bean) {
        boolean isVisible;

        if (bean.getAuditStatus().equals("2") || bean.getAuditType().equals("2") || !isRole(RoleBean.AUDITTEAMAUDIT)) {
            isVisible = false;
        } else {
            isVisible = true;
        }

        String auditType = bean.getAuditType();
        if (auditType.equals("0")) {//预支付需要额外判断日期,行程日期要大于当天
            boolean timeFlag = false;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            try {
                Date travelBegin = sdf.parse(bean.getTripStartDate());
                Date now = sdf.parse(sdf.format(new Date()));
                timeFlag = travelBegin.after(now) || travelBegin.equals(now);
            } catch (ParseException e) {
                Logger.e(e, "形成日期格式异常:" + bean.getTripStartDate());
            }
            isVisible = isVisible & timeFlag;
        }

        btnSettlement.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private boolean isRole(String roleTag) {
        if (roleBeanList == null) {
            roleBeanList = new Gson().fromJson(UserHelper.getInstance().getUser().getRoleList(), new
                    TypeToken<ArrayList<RoleBean>>() {
                    }.getType());
        }

        for (RoleBean roleBean : roleBeanList) {
            if (roleTag.equals(roleBean.getRoleTag())) {
                return true;
            }
        }
        return false;
    }

}
