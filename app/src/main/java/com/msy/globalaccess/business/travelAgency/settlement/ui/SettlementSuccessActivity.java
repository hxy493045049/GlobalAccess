package com.msy.globalaccess.business.travelAgency.settlement.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.main.ui.AgentMainActivity;
import com.msy.globalaccess.event.RefreshSettlementBadgeEvent;
import com.msy.globalaccess.event.SettlementSuccessEvent;
import com.msy.globalaccess.utils.RxBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hxy on 2017/2/17.
 * class description:
 */

public class SettlementSuccessActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void callActivity(Context ctx, String teamAuditId, String type,String auditType) {
        Intent intent = new Intent(ctx, SettlementSuccessActivity.class);
        intent.putExtra("TeamAuditId", teamAuditId);
        intent.putExtra("dataType", type);
        intent.putExtra("auditType", auditType);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement_success;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        String type = getIntent().getStringExtra("dataType");
        String teamAuditId = getIntent().getStringExtra("TeamAuditId");
        String auditType = getIntent().getStringExtra("auditType");
        RxBus.getInstance().post(new SettlementSuccessEvent(teamAuditId).setAuditStatus("2"));//发送消息更新列表
        RefreshSettlementBadgeEvent event=new RefreshSettlementBadgeEvent(type);
        event.setAuditType(auditType);
        RxBus.getInstance().post(event);//发送消息更新列表

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_return)
    public void clickBtn() {
        AgentMainActivity.callActivityClearTop(this);
    }
}
