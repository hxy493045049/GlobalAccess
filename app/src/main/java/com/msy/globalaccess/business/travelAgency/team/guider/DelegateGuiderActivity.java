package com.msy.globalaccess.business.travelAgency.team.guider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.business.travelAgency.BrowerActivity;
import com.msy.globalaccess.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.msy.globalaccess.business.travelAgency.team.guider.GuideListActivity.KEY_GUIDER_DELEGATE;

/**
 * 导游委派界面
 * Created by shawn on 2017/3/20 0020.
 * <p>
 * description :
 */

public class DelegateGuiderActivity extends BaseActivity {
    @BindView(R.id.tvDelegateConfirm)
    AppCompatTextView tvDelegateConfirm;
    @BindView(R.id.tvProtocolLink)
    AppCompatTextView tvProtocolLink;//H5连接
    @BindView(R.id.tvDelegateOperate)
    AppCompatTextView tvDelegateOperate;//委派操作
    @BindView(R.id.tvDelegateStatus)
    AppCompatTextView tvDelegateStatus;//委派状态
    @BindView(R.id.tvAffiliateUnit)
    AppCompatTextView tvAffiliateUnit;//旅行社
    @BindView(R.id.tvDelegateEnd)
    AppCompatTextView tvDelegateEnd;//委派结束时间
    @BindView(R.id.tvDelegateBegin)
    AppCompatTextView tvDelegateBegin;//委派开始时间
    @BindView(R.id.tvGuiderPhone)
    AppCompatTextView tvGuiderPhone;//手机号
    @BindView(R.id.tvGuiderGender)
    AppCompatTextView tvGuiderGender;//性别
    @BindView(R.id.tvGuiderName)
    AppCompatTextView tvGuiderName;//姓名
    @BindView(R.id.tvGuiderID)
    AppCompatTextView tvGuiderID;//新增导游id
    @BindView(R.id.ckProtocol)
    AppCompatCheckBox ckProtocol;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;//标题
    private GuiderListBean bean;//变更导游实体类

    public static void callActivity(GuiderListBean bean, Activity ctx, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(ctx, DelegateGuiderActivity.class);
        intent.putExtra(KEY_GUIDER_DELEGATE, bean);
        ctx.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_delegate_guider;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        tvToolbarCenter.setVisibility(View.VISIBLE);
        tvToolbarCenter.setText(R.string.guider_delegate_title);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(KEY_GUIDER_DELEGATE)) {
            bean = bundle.getParcelable(KEY_GUIDER_DELEGATE);
        }

        if (bean != null) {
            tvGuiderID.setText(bean.getGuideCode());
            tvGuiderName.setText(bean.getName());
            String sex = "";
            if ("0".equals(bean.getSex())) {
                sex = "女";
            } else {
                sex = "男";
            }
            tvGuiderGender.setText(sex);
            tvGuiderPhone.setText(bean.getPhoneNum());
            tvDelegateBegin.setText(bean.getAppointStartDate());
            tvDelegateEnd.setText(bean.getAppointEndDate());
            tvAffiliateUnit.setText(bean.getTravelAgentName());//
            tvDelegateStatus.setText(getOpType());
            tvDelegateOperate.setText(getStatus());
        }
        tvDelegateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ckProtocol.isChecked()) {
                    ToastUtils.showToast(R.string.guider_delegate_read_info);
                } else {
                    if (bean != null) {
                        bean.setOpType("0");
                        Intent intent = new Intent(DelegateGuiderActivity.this, GuideListActivity.class);
                        intent.putExtra(KEY_GUIDER_DELEGATE, bean);
                        setResult(RESULT_OK, intent);
                    }
                    DelegateGuiderActivity.this.finish();
                }
            }
        });

    }

    /**
     * 获取导游委派状态
     *
     * @return
     */
    private String getStatus() {
        String status = "";
        if ("0".equals(bean.getOpStatus())) {
            status = "处理中";
        } else if ("1".equals(bean.getOpStatus())) {
            status = "未通过";
        } else if ("2".equals(bean.getOpStatus())) {
            status = "已通过";
        }
        return status;
    }


    /**
     * 获取导游委派操作
     *
     * @return
     */
    private String getOpType() {
        String type = "";
        if ("0".equals(bean.getOpType())) {
            type = "委派申请";
        } else if ("1".equals(bean.getOpType())) {
            type = "取消委派";
        } else if ("-1".equals(bean.getOpType())) {
            type = "初始状态";
        }
        return type;
    }

    @OnClick(R.id.tvProtocolLink)
    public void LinkH5() {
        BrowerActivity.callactivity(DelegateGuiderActivity.this, getString(R.string.delegate_pact));
    }
}
