package com.msy.globalaccess.event;

/**
 * Created by pepys on 2017/2/23.
 * description:刷新结算列表小圆点
 */
public class RefreshSettlementBadgeEvent {

    public RefreshSettlementBadgeEvent(String type) {
        this.type = type;
    }

    private String type; //搜索类型

    private String auditType;//结算单类型：0:预支付;1:追加预支付;2:支付;3:退款;

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
